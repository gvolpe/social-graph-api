package controllers.auth

import com.mohiva.play.silhouette.contrib.services.{CachedCookieAuthenticator, DelegableAuthInfoService}
import com.mohiva.play.silhouette.contrib.utils.{BCryptPasswordHasher, PlayCacheLayer, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.core._
import com.mohiva.play.silhouette.core.providers.CredentialsProvider
import com.mohiva.play.silhouette.core.services.{AuthInfoService, IdentityService}
import com.mohiva.play.silhouette.core.utils.{CacheLayer, IDGenerator, PasswordHasher}
import repository.PasswordRepository

trait BaseAuthenticatorController[I <: Identity, T <: Token] extends Silhouette[I, CachedCookieAuthenticator] with AuthenticatorServiceModule {

  lazy val eventBus = EventBus()
  lazy val cacheLayer: CacheLayer = new PlayCacheLayer
  lazy val idGenerator: IDGenerator = new SecureRandomIDGenerator()
  lazy val passwordHasher: PasswordHasher = new BCryptPasswordHasher()

  lazy val authInfoService: AuthInfoService = new DelegableAuthInfoService(new PasswordRepository)
  lazy val credentialsProvider: CredentialsProvider = new CredentialsProvider(authInfoService, passwordHasher, Seq(passwordHasher))
  lazy val providers = Map(credentialsProvider.id -> credentialsProvider)

  def identityService: IdentityService[I]

  def env: Environment[I, CachedCookieAuthenticator]

}
