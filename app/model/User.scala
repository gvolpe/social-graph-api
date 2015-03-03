package model

import play.api.libs.json.Json

case class UserCreation(username: String, email: String)
case class User(id: Long, username: String, email: String)

object User {

  implicit val userFormat = Json.format[User]
  implicit val userCreationFormat = Json.format[UserCreation]

}
