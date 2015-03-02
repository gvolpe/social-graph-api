package auth.repository

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO

import scala.concurrent.Future

class PasswordRepository extends DelegableAuthInfoDAO[PasswordInfo] {

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    InMemoryRepository.pwd += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    Future.successful(InMemoryRepository.pwd.get(loginInfo))
  }

}