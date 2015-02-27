package repository

import com.mohiva.play.silhouette.contrib.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.providers.PasswordInfo
import scala.collection.mutable
import scala.concurrent.Future

class PasswordRepository extends DelegableAuthInfoDAO[PasswordInfo] {

  var data: mutable.HashMap[LoginInfo, PasswordInfo] = mutable.HashMap()

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    data += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] =
    Future.successful(data.get(loginInfo))

}