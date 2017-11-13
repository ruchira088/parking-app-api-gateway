package utils

import com.myob.exceptions.JsonDeserializationException
import exceptions.HttpResponseException
import play.api.http.Status
import play.api.libs.json.Reads
import play.api.libs.ws.WSResponse

import scala.util.{Failure, Success, Try}

object ResponseUtils
{
  def getData[A](response: WSResponse)(implicit reads: Reads[A]): Try[A] =
    if (response.status >= Status.BAD_REQUEST)
      Failure(HttpResponseException(response.status, response.body))
    else
      response.json.validate[A]
        .fold(
          validationErrors => Failure(JsonDeserializationException(validationErrors)),
          Success(_)
        )
}
