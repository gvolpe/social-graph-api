package auth.module

import auth.User
import auth.repository._
import auth.service.DefaultUserIdentityService
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

trait JWTAuthenticatorController extends BaseJWTAuthenticatorController[User, JWTAuthenticator] {

  lazy val identityService = new DefaultUserIdentityService with RedisUserIdentityRepository
  lazy val passwordInfoDAO = new PasswordRepositoryImpl with RedisPasswordRepository

  implicit lazy val env: Environment[User, JWTAuthenticator] =
    Environment[User, JWTAuthenticator](identityService, authenticatorService, providers, eventBus)

}
