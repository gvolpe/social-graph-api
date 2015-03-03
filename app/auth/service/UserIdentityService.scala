package auth.service

import auth.repository.UserIdentityRepository
import auth.{SignUp, UserIdentity}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait UserIdentityService extends IdentityService[UserIdentity] {

  def add(loginInfo: LoginInfo, signUp: SignUp): Future[UserIdentity]

  def retrieve(loginInfo: LoginInfo): Future[Option[UserIdentity]]

}

class DefaultUserIdentityService extends UserIdentityService {

  self: UserIdentityRepository =>

  def add(loginInfo: LoginInfo, signUp: SignUp): Future[UserIdentity] = {
    val user = UserIdentity(signUp.identifier, signUp.password, loginInfo)
    save(user).map(_ => user)
  }

  def retrieve(loginInfo: LoginInfo): Future[Option[UserIdentity]] = {
    find(loginInfo)
  }

}
