package controllers

import javax.inject.{Inject, Singleton}

import com.myob.models.User
import com.myob.requests.bodies.{AuthenticateUserRequest, CreateUserRequest}
import com.myob.utils.JsonUtils
import play.api.libs.json.{JsValue, Reads}
import play.api.mvc._
import services.user.UserService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(userService: UserService, parsers: PlayBodyParsers, controllerComponents: ControllerComponents)(implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  private def userServiceAction[A](action: A => Future[User])(implicit request: Request[JsValue], reads: Reads[A]): Future[Result] =
    for {
      body <- Future.fromTry(JsonUtils.deserialize[A])

      user <- action(body)
    }
      yield Ok(user.toSanitizedJson)

  def create(): Action[JsValue] = Action.async(parsers.json) {
    implicit request: Request[JsValue] => userServiceAction[CreateUserRequest](userService.createUser)
  }

  def login(): Action[JsValue] = Action.async(parsers.json) {
    implicit request: Request[JsValue] => userServiceAction[AuthenticateUserRequest](userService.authenticate)
  }
}