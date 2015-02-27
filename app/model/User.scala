package model

import com.mohiva.play.silhouette.core.{Identity, LoginInfo}
import play.api.libs.json.Json

case class User(email: String, loginInfo: LoginInfo) extends Identity

object ModelImplicits {

  implicit val loginInfoFormat = Json.format[LoginInfo]
  implicit val userFormat = Json.format[User]

}
