package services.user

import javax.inject.{Inject, Singleton}

import com.myob.models.User
import com.myob.requests.bodies.{AuthenticateUserRequest, CreateUserRequest}
import com.myob.utils.ConfigUtils
import constants.EnvValueNames.USER_SERVICE_URL
import play.api.libs.json.{Json, Writes}
import play.api.libs.ws.WSClient
import utils.ResponseUtils

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserServiceImpl @Inject()(wsClient: WSClient)(implicit executionContext: ExecutionContext)
  extends UserService
{
  private def getUserServiceUrl(): Future[String] =
    Future.fromTry(ConfigUtils.getEnvValueAsTry(USER_SERVICE_URL))

  private def makeRequest[A](path: String, requestBody: A)(implicit writes: Writes[A]): Future[User] = for
    {
      userServiceUrl <- getUserServiceUrl()

      urlEndPoint = s"$userServiceUrl/$path"

      response <- wsClient.url(urlEndPoint).post(Json.toJson(requestBody))

      user <- Future.fromTry(ResponseUtils.getData[User](response))
    }
    yield user

  override def createUser(createUserRequest: CreateUserRequest): Future[User] =
    makeRequest(UserServiceImpl.CREATE_USER_PATH, createUserRequest)

  override def authenticate(authenticateUserRequest: AuthenticateUserRequest): Future[User] =
    makeRequest(UserServiceImpl.AUTHENTICATE_USER_PATH, authenticateUserRequest)
}

object UserServiceImpl
{
  val CREATE_USER_PATH = "user"

  val AUTHENTICATE_USER_PATH = "authenticate"
}