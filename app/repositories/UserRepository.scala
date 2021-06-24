package repositories

import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection

//import play.api.libs.json.Constraints._
//import akka.dispatch.Future

// BSON-JSON conversions/collection
import reactivemongo.play.json.compat._
import reactivemongo.play.json.compat.json2bson.{toDocumentReader, toDocumentWriter}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi
                               ) {

  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("users"))

  def findOne(id: String): Future[Option[JsObject]] = {
    collection.flatMap(_.find(BSONDocument("id" -> id), Option.empty[JsObject]).one[JsObject])
  }
}