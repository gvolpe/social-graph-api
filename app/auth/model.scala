package auth

import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import org.joda.time.DateTime
import play.api.libs.json.{JsString, JsValue, Writes, Json}

case class User(email: String, password: String, loginInfo: LoginInfo) extends Identity
case class SignUp(password: String, identifier: String)
case class Token(token: String, expiresAt: DateTime)

object Implicits {

  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val userFormat = Json.format[User]
  implicit val signUpFormat = Json.format[SignUp]
  implicit val credentialsFormat = Json.format[Credentials]
  implicit val jodaDateWrites: Writes[org.joda.time.DateTime] = new Writes[org.joda.time.DateTime] {
    def writes(d: org.joda.time.DateTime): JsValue = JsString(d.toString)
  }
  implicit val tokenFormat = Json.format[Token]

}