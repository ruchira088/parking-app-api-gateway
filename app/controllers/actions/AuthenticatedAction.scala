package controllers.actions

import javax.inject.Inject

import play.api.http.HeaderNames
import play.api.mvc._
import services.authentication.AuthenticationService
import com.myob.utils.ScalaUtils.fromOptionToTry
import controllers.requests.AuthenticatedRequest
import exceptions.{AuthorizationHeaderNotFoundException, InvalidBearerTokenException}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

class AuthenticatedAction @Inject()(authenticationService: AuthenticationService, parser: BodyParsers.Default)
                                   (implicit executionContext: ExecutionContext) extends ActionBuilderImpl[AnyContent](parser)
{
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]) = for
    {
      bearerToken <- Future.fromTry(AuthenticatedAction.getBearerToken(request))

      authToken <- authenticationService.verify(bearerToken)

      output <- block(AuthenticatedRequest(authToken, request))
    }
    yield output
}

object AuthenticatedAction
{
  type AuthorizationToken = String

  private val AUTHORIZATION_SCHEME = "Bearer"

  private val tokenRegex: Regex = s"$AUTHORIZATION_SCHEME (\\S+)".r

  def extractToken(authorizationHeader: String): Try[AuthorizationToken] = authorizationHeader match {
    case tokenRegex(authorizationToken) => Success(authorizationToken.trim)
    case _ => Failure(InvalidBearerTokenException)
  }

  def getBearerToken[_](request: Request[_]): Try[AuthorizationToken] = for {
    authorizationHeader <- fromOptionToTry(request.headers.get(HeaderNames.AUTHORIZATION), AuthorizationHeaderNotFoundException)

    bearerToken <- extractToken(authorizationHeader)
  }
    yield bearerToken
}