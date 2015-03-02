package auth

import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import play.api.libs.json.Json

case class User(email: String, password: String, loginInfo: LoginInfo) extends Identity
case class SignUp(password: String, identifier: String)

object ModelImplicits {

  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val userFormat = Json.format[User]
  implicit val signUpFormat = Json.format[SignUp]
  implicit val credentialsFormat = Json.format[Credentials]

}
