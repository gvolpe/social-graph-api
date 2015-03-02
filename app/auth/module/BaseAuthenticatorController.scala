package auth.module

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.services.{AuthInfoService, IdentityService}
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import com.mohiva.play.silhouette.impl.util.{BCryptPasswordHasher, PlayCacheLayer, SecureRandomIDGenerator}

trait BaseAuthenticatorController[I <: Identity, T <: Authenticator] extends Silhouette[I, JWTAuthenticator] with AuthenticatorServiceModule {

  lazy val eventBus = EventBus()
  lazy val cacheLayer: CacheLayer = new PlayCacheLayer
  lazy val httpLayer: HTTPLayer = new PlayHTTPLayer
  lazy val idGenerator: IDGenerator = new SecureRandomIDGenerator()
  lazy val passwordHasher: PasswordHasher = new BCryptPasswordHasher()

  lazy val authInfoService: AuthInfoService = new DelegableAuthInfoService(passwordInfoDAO)
  lazy val credentialsProvider: CredentialsProvider = new CredentialsProvider(authInfoService, passwordHasher, Seq(passwordHasher))
  lazy val providers = Map(credentialsProvider.id -> credentialsProvider)

  def identityService: IdentityService[I]
  def passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo]

  def env: Environment[I, JWTAuthenticator]

}
