package auth.module

import auth.repository._
import auth.service.{UserIdentityService, DefaultUserIdentityService}
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO

trait AuthenticatorIdentityModule {

  def identityService: UserIdentityService
  def passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo]

}

trait DefaultAuthenticatorIdentityModule extends AuthenticatorIdentityModule {

  lazy val identityService = new DefaultUserIdentityService with DefaultUserIdentityRepository
  lazy val passwordInfoDAO = new PasswordRepositoryImpl with DefaultPasswordRepository

}

trait RedisAuthenticatorIdentityModule extends AuthenticatorIdentityModule {

  lazy val identityService = new DefaultUserIdentityService with RedisUserIdentityRepository
  lazy val passwordInfoDAO = new PasswordRepositoryImpl with RedisPasswordRepository

}
