package utils

import exceptions.HttpResponseException
import play.api.http.Status
import play.api.libs.ws.{BodyReadable, WSResponse}

import scala.concurrent.Future

object ResponseUtils
{
  def getData[A](response: WSResponse)(implicit bodyReadable: BodyReadable[A]): Future[A] =
    if (response.status >= Status.BAD_REQUEST)
      Future.failed(HttpResponseException(response.status, response.body))
    else
      Future.successful(response.body[A])
}
