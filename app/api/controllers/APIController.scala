package api.controllers

import play.api.Logging
import play.api.libs.json.{JsArray, JsValue, Json, __}
import play.api.mvc._
import reactivemongo.api.bson.BSONObjectID
import repositories.{DocRepository, Transactions, UserRepository}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class APIController @Inject()(
                               implicit executionContext: ExecutionContext,
                               cc: ControllerComponents,
                               val userRepository: UserRepository,
                               val docRepository: DocRepository
                             ) extends AbstractController(cc) with Logging {

  // PRIVATE UTILS

  private def printGetJsonRequest(body: AnyContent, name: String) = {
    body.asJson.map { json =>
      logger.info("[" + name + "]: " + json.toString)
    }.getOrElse {
      logger.error("[" + name + "]: Expecting JSON request body")
    }
  }

  private def printGetRequest(request: Request[AnyContent], name: String) = {
    logger.info("[" + name + "]: " + request.toString())
  }

  private def printPostRequest(request: Request[AnyContent], name: String) = {
    val value = request.body.asFormUrlEncoded.getOrElse("{EMPTY}")
    logger.info("[" + name + "]: " + value)
  }

  private def printPostJsonRequest(request: Request[JsValue], name: String) = {
    val value = request.body
    logger.info("[" + name + "]: " + value.toString())
  }

  // KEY

  def getGetKey() = Action { implicit request: Request[AnyContent] =>
    printGetRequest(request, "GET /api/v1/get-key")
    Ok(Json.obj("apiKey" -> "1IP84UTvzJKds1Jomx8gIbTXcEEJSUilGqpxCcmnx"))
  }

  def postCreateKey() = Action { implicit request: Request[AnyContent] =>
    printPostRequest(request, "POST /api/v1/create-key")
    Ok(Json.obj("apiKey" -> "1IP84UTvzJKds1Jomx8gIbTXcEEJSUilGqpxCcmnx"))
  }

  // ACCOUNT

  def getGetUserData(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    printGetRequest(request, request.method + " " + request.path)

    val userId = "f3cb0f2a-d25f-48ec-b75c-b62273709cfe"

    userRepository.findOne(userId).map {
      user => Ok(Json.toJson(user))
    }

  }

  def postPusherChannelAuth() = Action { request: Request[AnyContent] =>
    printPostRequest(request, "POST /api/account/pusher-channel-auth")
    Ok(Json.obj())
  }

  // USER-SETTINGS

  def getUserSettingsAll() = Action { implicit request: Request[AnyContent] =>
    printGetRequest(request, "GET /api/user-settings/all")
    Ok(Json.parse(
      """{
          "lastActiveDocId":"d341ef2f-9f9f-4ad8-900b-7c1715d3324a",
          "sidebarView":"add",
          "cameraPosition":[586.8742260283846,450,254.88363049278337],
          "cameraRotation":0.7853981633974483,
          "zoom":8.66666,
          "cameraIsTopDown":false,
          "cameraEuler":[-0.7853981633974483,0.6154797086703875,0.5235987755982989,"XYZ"],
          "tipOfDayIndex":1
        }"""))
  }

  def postUserSettingsSet() = Action { request: Request[AnyContent] =>
    printGetJsonRequest(request.body, "POST /api/user-settings/set")
    Ok(Json.parse("""{ "success": true }"""))
  }

  // DOCS

  def getDocsList() = Action { implicit request: Request[AnyContent] =>
    printGetRequest(request, "GET /api/docs/list")
    Ok(
      Json.parse(
        """{
        "folders":[{"folderId":"40a0c77b-5a32-4aed-bbb6-1295b83b38fb",
                    "title":"répertoire de travail",
                    "items":[{
                      "id":"d341ef2f-9f9f-4ad8-900b-7c1715d3324a",
                      "isOpen":true,
                      "title": "Google Cloud Reference Architecture"
                    }]
                  }],
        "sharedDocs":[{}]
      }"""))
  }

  def getDoc(docId: String) = Action.async { implicit request: Request[AnyContent] =>
    printGetRequest(request, request.method + " " + request.path)

    docRepository.getDocument(docId).map {
      doc => Ok(Json.toJson(doc))
    }
  }

  def postProcessTransaction(): Action[JsValue] = Action(controllerComponents.parsers.json) { implicit request =>
    printPostJsonRequest(request, request.method + " " + request.path)

    docRepository.updateDocument(request.body.as[Transactions])

    Ok(Json.parse("""{ "success": true }"""))
  }

}
