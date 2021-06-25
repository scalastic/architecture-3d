package repositories

import play.api.Logging
import play.api.libs.json.Reads._
import play.api.libs.json.{JsObject, JsString, JsValue, _}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult

// BSON-JSON conversions/collection
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

class DocRepository @Inject()(
                               implicit ec: ExecutionContext,
                               reactiveMongoApi: ReactiveMongoApi) extends Logging {

  import reactivemongo.play.json.compat
  import compat.json2bson._

  private def documentsCollection: Future[BSONCollection] =
    reactiveMongoApi.database.map(_.collection("docs"))

  def getAll: Future[Seq[Document]] =
    documentsCollection.flatMap(_.find(BSONDocument.empty).
      cursor[Document]().collect[Seq](100))

  def getDocument(id: String): Future[Option[Document]] =
    documentsCollection.flatMap(_.find(BSONDocument("id" -> id)).one[Document])

  def addDocument(document: Document): Future[WriteResult] =
    documentsCollection.flatMap(_.insert.one(
      document.copy())) //(_id = Some(ArraySeq(UUID.randomUUID().toString)))))

  def updateDocumentContent(transactions: Transactions): Future[Any] = getDocument(transactions.docId).map {
    case Some(doc) => {
      val updatedContent = processTransactions(doc.content, transactions)
      val updateModifier = BSONDocument(
        f"$$set" -> BSONDocument(
          "content" -> updatedContent)
      )
      documentsCollection.flatMap(_.findAndUpdate(
        selector = BSONDocument("id" -> doc.id),
        update = updateModifier,
        fetchNewObject = true).map(_.result[Document])
      )
    }
    case _ => logger.error(s"Updating document failed for transactions: $transactions")
  }

  private def processTransactions(content: Map[String, JsValue], transactions: Transactions) =  {

    val documentContent: collection.mutable.Map[String, JsValue] = collection.mutable.Map[String, JsValue]() ++= content
    transactions.transactions.foreach { transaction =>
      transaction.action match {
        case "create" => documentContent += createComponent(transaction)
        case "delete" => documentContent -= deleteComponent(transaction)
        case "update" => documentContent += updateComponent(documentContent(transaction.id), transaction)
      }
    }
    documentContent
  }

  private def createComponent(transaction: Transaction) = {
    (transaction.id -> JsObject(Seq("type" -> JsString(transaction.`type`.get), "data" -> transaction.data.get)))
  }

  private def deleteComponent(transaction: Transaction) = {
    (transaction.id)
  }

  private def updateComponent(component: JsValue, transaction: Transaction) = {
    (transaction.id -> mergeComponentWithTransaction(component, transaction))
  }

  private def mergeComponentWithTransaction(component: JsValue, transaction: Transaction): JsObject = {

    val transactionData = transaction.data.getOrElse(JsObject.empty).as[JsObject]

    var updatedComponent = component
    transactionData.keys.map { key =>
      updatedComponent = updateComponentByKey(updatedComponent, transactionData, key)
    }

    updatedComponent.as[JsObject]
  }

  private def updateComponentByKey(component: JsValue, transactionData: JsObject, key: String) = {
    (component \ "data" \ key) match {
      case JsDefined(d) =>
        val jsonUpdater = (__ \ "data" \ key).json.update(of[JsValue].map { case obj: JsValue => transactionData(key) })
        transformComponent(component, jsonUpdater)
      case JsUndefined() =>
        val jsonAdditionner = (__ \ "data").json.update(__.read[JsObject].map { o => o ++ Json.obj(key -> transactionData(key)) })
        transformComponent(component, jsonAdditionner)
    }
  }

  private def transformComponent(component: JsValue, jsonModifier: Reads[JsObject]): JsValue = {
    component.transform(jsonModifier) match {
      case JsSuccess(v,p) => v
      case JsError(e) =>
        logger.error(e.toString())
        component
    }
  }

  def deleteDocument(id: String): Future[Option[Document]] =
    documentsCollection.flatMap(_.findAndRemove(
      selector = BSONDocument("id" -> id)).map(_.result[Document]))

}
