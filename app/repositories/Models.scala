package repositories

import play.api.libs.json.JsValue

import java.util.Date

case class Document( //_id: Option[ArraySeq[String]], // Avoid using BSONObjectID, not to couple model with DB
                     id: Option[String],
                     title: String,
                     created: Date,
                     lastChange: Date,
                     user: ShortUser,
                     folder: ShortFolder,
                     version: String,
                     content: Map[String, JsValue])

object Document {

  import play.api.libs.json._

  implicit val documentFormat: OFormat[Document] = Json.format[Document]
}

case class Transactions( //_id: Option[ArraySeq[String]], // Avoid using BSONObjectID, not to couple model with DB
                         docId: String,
                         transactions: Seq[Transaction])

object Transactions {

  import play.api.libs.json._

  implicit val transactionsFormat: OFormat[Transactions] = Json.format[Transactions]
}

case class Transaction( //_id: Option[ArraySeq[String]], // Avoid using BSONObjectID, not to couple model with DB
                        action: String,
                        `type`: Option[String],
                        id: String,
                        isTransient: Option[Boolean],
                        data: Option[JsValue])

object Transaction {

  import play.api.libs.json._

  implicit val transactionFormat: OFormat[Transaction] = Json.format[Transaction]
}




case class ShortUser(
                      id: Option[String],
                      email: String
                    )

object ShortUser {

  import play.api.libs.json._

  implicit val shortUserFormat: OFormat[ShortUser] = Json.format[ShortUser]
}

case class ShortFolder(
                        id: Option[String],
                        title: String
                      )

object ShortFolder {

  import play.api.libs.json._

  implicit val shortFolderFormat: OFormat[ShortFolder] = Json.format[ShortFolder]
}

