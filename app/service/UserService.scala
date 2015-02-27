package service

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.services.IdentityService
import model.User
import repository.UserRepository

import scala.concurrent.Future

trait UserService extends IdentityService[User] {

  def add(user: User): Future[Boolean]

}

class DefaultUserService extends UserService {

  self: UserRepository =>

  def add(user: User): Future[Boolean] = save(user)

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = find(loginInfo)

}
