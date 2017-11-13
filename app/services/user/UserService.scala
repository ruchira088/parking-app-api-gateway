package services.user

import com.myob.models.User
import com.myob.requests.bodies.{AuthenticateUserRequest, CreateUserRequest}

import scala.concurrent.Future

trait UserService
{
  def createUser(createUserRequest: CreateUserRequest): Future[User]

  def authenticate(authenticateUserRequest: AuthenticateUserRequest): Future[User]
}
