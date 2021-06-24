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

  def updateDocument(transactions: Transactions) = {

    getDocument(transactions.docId).map {
      value => value match {
        case Some(doc) => {
          val updatedContent = processTransaction(doc.content, transactions)
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
        case other => logger.error(s"Updating document failed with transactions: $transactions")
      }
    }

  }

  def processTransaction(content: Map[String, JsValue], transactions: Transactions) =  {

    val updatedContent: collection.mutable.Map[String, JsValue] = collection.mutable.Map[String, JsValue]() ++= content
    transactions.transactions.foreach { item =>
      item.action match {
        case "create" => updatedContent += (item.id -> JsObject(Seq("type" -> JsString(item.`type`.get), "data" -> item.data.get)))
        case "delete" => updatedContent -= (item.id)
        case "update" => updatedContent += (item.id -> mergeUpdateTransaction(content(item.id), item))
      }
    }
    updatedContent
  }

  def mergeUpdateTransaction(content: JsValue, item: Transaction): JsObject = {

    val data = item.data.get.as[JsObject]
    val keyToSearch = data.keys.head
    val valueToUpdate = data.values.toList.head match {
      case js: JsObject => js.as[JsObject]
      case str : JsString => str.as[JsString]
      case nb: JsNumber => nb.as[JsNumber]
      case ar: JsArray => ar.as[JsArray]
    }

    logger.warn(s"UPDATE key [$keyToSearch] with value [$valueToUpdate]" )

    val jsonUpdater = (__ \ "data" \ keyToSearch).json.update(of[JsValue].map{ case obj: JsValue => valueToUpdate })
    val jsonAdditionner = (__ \ "data").json.update(__.read[JsObject].map{ o => o ++ Json.obj( keyToSearch -> valueToUpdate ) }
    )

    content.transform(jsonUpdater) match {
      case JsSuccess(value, path) => {
        logger.warn(s"Update field [$keyToSearch] succeed with value [$valueToUpdate] and result in $value" )
        value
      }
      case JsError(errors) => {
        logger.error(s"Error $errors when Updating field [$keyToSearch] with value [$valueToUpdate]")
        content.transform(jsonAdditionner).get.as[JsObject]
      }

    }
  }

  def deleteDocument(id: String): Future[Option[Document]] =
    documentsCollection.flatMap(_.findAndRemove(
      selector = BSONDocument("id" -> id)).map(_.result[Document]))

}
