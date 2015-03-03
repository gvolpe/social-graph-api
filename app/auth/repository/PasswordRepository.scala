package auth.repository

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO

import scala.concurrent.Future

trait PasswordRepository {

  def savePwd(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo]

  def findPwd(loginInfo: LoginInfo): Future[Option[PasswordInfo]]

}

class PasswordRepositoryImpl extends DelegableAuthInfoDAO[PasswordInfo] {

  self: PasswordRepository =>

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = savePwd(loginInfo, authInfo)

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = findPwd(loginInfo)

}

trait DefaultPasswordRepository extends PasswordRepository {

  def savePwd(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    InMemoryData.pwd += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  def findPwd(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    Future.successful(InMemoryData.pwd.get(loginInfo))
  }

}

trait RedisPasswordRepository extends PasswordRepository {

  def savePwd(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = ???

  def findPwd(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = ???

}