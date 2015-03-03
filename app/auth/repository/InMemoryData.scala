package auth.repository

import auth.User
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo

import scala.collection.mutable

object InMemoryData {

  val users = mutable.HashMap[LoginInfo, User]()
  var pwd: mutable.HashMap[LoginInfo, PasswordInfo] = mutable.HashMap()

}
