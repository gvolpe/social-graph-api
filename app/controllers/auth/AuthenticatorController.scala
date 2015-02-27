package controllers.auth

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.Environment
import model.{TokenUser, User}
import repository.DefaultUserRepository
import service.DefaultUserService

trait AuthenticatorController extends BaseAuthenticatorController[User, TokenUser] {

  lazy val identityService = new DefaultUserService with DefaultUserRepository

  implicit lazy val env: Environment[User, CachedCookieAuthenticator] =
    Environment[User, CachedCookieAuthenticator](identityService, authenticatorService, Map(), eventBus)

}
