package auth.module

import auth.UserIdentity
import auth.repository._
import auth.service.DefaultUserIdentityService
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

trait JWTAuthenticatorController extends BaseJWTAuthenticatorController[UserIdentity, JWTAuthenticator] {

  lazy val identityService = new DefaultUserIdentityService with RedisUserIdentityRepository
  lazy val passwordInfoDAO = new PasswordRepositoryImpl with RedisPasswordRepository

  implicit lazy val env: Environment[UserIdentity, JWTAuthenticator] =
    Environment[UserIdentity, JWTAuthenticator](identityService, authenticatorService, providers, eventBus)

}
