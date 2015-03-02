package auth.module

import auth.User
import auth.repository.{DefaultUserRepository, PasswordRepository}
import auth.service.DefaultUserService
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

trait AuthenticatorController extends BaseAuthenticatorController[User, JWTAuthenticator] {

  lazy val identityService = new DefaultUserService with DefaultUserRepository
  lazy val passwordInfoDAO = new PasswordRepository

  implicit lazy val env: Environment[User, JWTAuthenticator] =
    Environment[User, JWTAuthenticator](identityService, authenticatorService, providers, eventBus)

}
