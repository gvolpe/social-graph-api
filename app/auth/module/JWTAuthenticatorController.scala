package auth.module

import auth.User
import auth.repository._
import auth.service.DefaultUserService
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

trait JWTAuthenticatorController extends BaseJWTAuthenticatorController[User, JWTAuthenticator] {

  lazy val identityService = new DefaultUserService with RedisUserRepository
  lazy val passwordInfoDAO = new PasswordRepositoryImpl with RedisPasswordRepository

  implicit lazy val env: Environment[User, JWTAuthenticator] =
    Environment[User, JWTAuthenticator](identityService, authenticatorService, providers, eventBus)

}
