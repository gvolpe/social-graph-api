package auth.module

import auth.UserIdentity
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

trait JWTAuthenticatorController extends BaseJWTAuthenticatorController[UserIdentity, JWTAuthenticator] {

  self: AuthenticatorIdentityModule =>

  implicit lazy val env: Environment[UserIdentity, JWTAuthenticator] =
    Environment[UserIdentity, JWTAuthenticator](identityService, authenticatorService, providers, eventBus)

}