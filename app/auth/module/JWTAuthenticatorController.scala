package auth.module

import auth.UserIdentity
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

trait JWTAuthenticatorController extends BaseJWTAuthenticatorController[UserIdentity, JWTAuthenticator] {

  self: AuthenticatorIdentityModule =>

}