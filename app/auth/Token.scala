package auth

import org.joda.time.DateTime
import play.api.libs.json.{JsString, JsValue, Json, Writes}

case class Token(token: String, expiresAt: DateTime)

object Token {

  implicit val jodaDateWrites: Writes[org.joda.time.DateTime] = new Writes[org.joda.time.DateTime] {
    def writes(d: org.joda.time.DateTime): JsValue = JsString(d.toString)
  }
  implicit val restFormat = Json.format[Token]

}
