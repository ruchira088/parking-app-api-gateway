package services.authentication

import com.myob.models.User
import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json, OFormat}
import com.myob.utils.JsonFormatters.dateTimeObjectFormat

case class AuthToken(user: User, tokenString: String, issuedAt: DateTime)
{
  def clientToken: JsObject = Json.obj("token" -> tokenString)
}

object AuthToken
{
  implicit def objectFormat: OFormat[AuthToken] = Json.format[AuthToken]
}
