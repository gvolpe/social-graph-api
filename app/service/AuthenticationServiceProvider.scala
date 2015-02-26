package service

import com.mohiva.play.silhouette.contrib.services.{CachedCookieAuthenticator, CachedCookieAuthenticatorService, CachedCookieAuthenticatorSettings}
import com.mohiva.play.silhouette.contrib.utils.{PlayCacheLayer, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.core.services.AuthenticatorService
import com.mohiva.play.silhouette.core.utils.Clock
import play.api.Play

object AuthenticationServiceProvider {

  val cacheLayer = new PlayCacheLayer
  val idGenerator = new SecureRandomIDGenerator()

  val userService: UserService = new DefaultUserService

  val authenticatorService: AuthenticatorService[CachedCookieAuthenticator] = {
    new CachedCookieAuthenticatorService(CachedCookieAuthenticatorSettings(
      cookieName = Play.configuration.getString("silhouette.authenticator.cookieName").get,
      cookiePath = Play.configuration.getString("silhouette.authenticator.cookiePath").get,
      cookieDomain = Play.configuration.getString("silhouette.authenticator.cookieDomain"),
      secureCookie = Play.configuration.getBoolean("silhouette.authenticator.secureCookie").get,
      httpOnlyCookie = Play.configuration.getBoolean("silhouette.authenticator.httpOnlyCookie").get,
      cookieIdleTimeout = Play.configuration.getInt("silhouette.authenticator.cookieIdleTimeout").get,
      cookieAbsoluteTimeout = Play.configuration.getInt("silhouette.authenticator.cookieAbsoluteTimeout"),
      authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").get
    ), cacheLayer, idGenerator, Clock())
  }

}
