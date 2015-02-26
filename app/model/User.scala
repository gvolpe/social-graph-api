package model

import com.mohiva.play.silhouette.core.LoginInfo

case class User(email: String, loginInfo: LoginInfo)

object User {

}
