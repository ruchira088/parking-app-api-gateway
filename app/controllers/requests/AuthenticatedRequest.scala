package controllers.requests

import play.api.mvc.{Request, WrappedRequest}
import services.authentication.AuthToken

case class AuthenticatedRequest[A](authToken: AuthToken, request: Request[A]) extends WrappedRequest(request)