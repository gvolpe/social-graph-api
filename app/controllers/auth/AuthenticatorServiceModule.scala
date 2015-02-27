package controllers.auth

import com.mohiva.play.silhouette.contrib.services.{CachedCookieAuthenticatorSettings, CachedCookieAuthenticatorService, CachedCookieAuthenticator}
import com.mohiva.play.silhouette.core.services.AuthenticatorService
import com.mohiva.play.silhouette.core.utils.{IDGenerator, CacheLayer, Clock}
import play.api.Play
import play.api.Play.current

trait AuthenticatorServiceModule {

  lazy val authenticatorService: AuthenticatorService[CachedCookieAuthenticator] = new CachedCookieAuthenticatorService(
    CachedCookieAuthenticatorSettings(
      cookieName = Play.configuration.getString("silhouette.authenticator.cookieName").get,
      cookiePath = Play.configuration.getString("silhouette.authenticator.cookiePath").get,
      cookieDomain = Play.configuration.getString("silhouette.authenticator.cookieDomain"),
      secureCookie = Play.configuration.getBoolean("silhouette.authenticator.secureCookie").get,
      httpOnlyCookie = Play.configuration.getBoolean("silhouette.authenticator.httpOnlyCookie").get,
      cookieIdleTimeout = Play.configuration.getInt("silhouette.authenticator.cookieIdleTimeout").get,
      cookieAbsoluteTimeout = Play.configuration.getInt("silhouette.authenticator.cookieAbsoluteTimeout"),
      authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").get
    ),
    cacheLayer,
    idGenerator,
    Clock()
  )

  def cacheLayer: CacheLayer
  def idGenerator: IDGenerator

}
