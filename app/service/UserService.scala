package service

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.services.IdentityService
import model.User

import scala.concurrent.Future

trait UserService extends IdentityService[User] {

  def save(user: User): Future[User]

}

class DefaultUserService extends UserService {

  def save(user: User): Future[User] = ???

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = ???

}
