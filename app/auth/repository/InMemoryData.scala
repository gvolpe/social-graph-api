package auth.repository

import auth.UserIdentity
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import model.User

import scala.collection.mutable

object InMemoryData {

  val usersIdentity = mutable.HashMap[LoginInfo, UserIdentity]()
  var pwd = mutable.HashMap[LoginInfo, PasswordInfo]()
  val users = mutable.HashMap[Long, User]()

}
