package auth.service

import auth.repository.UserIdentityRepository
import auth.{SignUp, User}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait UserIdentityService extends IdentityService[User] {

  def add(loginInfo: LoginInfo, signUp: SignUp): Future[User]

  def retrieve(loginInfo: LoginInfo): Future[Option[User]]

}

class DefaultUserIdentityService extends UserIdentityService {

  self: UserIdentityRepository =>

  def add(loginInfo: LoginInfo, signUp: SignUp): Future[User] = {
    val user = User(signUp.identifier, signUp.password, loginInfo)
    save(user).map(_ => user)
  }

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = {
    find(loginInfo)
  }

}
