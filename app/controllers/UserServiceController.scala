package controllers

import javax.inject.{Inject, Singleton}

import com.myob.requests.bodies.CreateUserRequest
import com.myob.utils.JsonUtils
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents, PlayBodyParsers, Request}
import services.user.UserService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserServiceController @Inject()(userService: UserService, parsers: PlayBodyParsers, controllerComponents: ControllerComponents)(implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def createUser() = Action.async(parsers.json) {

    implicit request: Request[JsValue] => for
      {
        createUserRequest <- Future.fromTry(JsonUtils.deserialize[CreateUserRequest])
        user <- userService.createUser(createUserRequest)
      }
        yield Ok(user.toSanitizedJson)
  }
}