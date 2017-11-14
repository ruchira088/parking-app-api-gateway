package utils

import com.myob.exceptions.HttpResponseException
import com.myob.utils.JsonUtils
import play.api.http.Status
import play.api.libs.json.Reads
import play.api.libs.ws.WSResponse

import scala.util.{Failure, Try}

object ResponseUtils
{
  def getData[A](response: WSResponse)(implicit reads: Reads[A]): Try[A] =
    if (response.status >= Status.BAD_REQUEST)
      Failure(HttpResponseException(response.status, response.body))
    else
      JsonUtils.deserialize(response.json)
}
