package auth.repository

import auth.Implicits._
import auth.repository.redis.{HashedReadKey, HashedWriteKey, PasswordRedisKey, RedisConnectionManager}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import play.api.libs.json.Json

import scala.concurrent.Future

trait PasswordRepository {

  def savePwd(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo]

  def findPwd(loginInfo: LoginInfo): Future[Option[PasswordInfo]]

}

class PasswordRepositoryImpl extends DelegableAuthInfoDAO[PasswordInfo] {

  self: PasswordRepository =>

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = savePwd(loginInfo, authInfo)

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = findPwd(loginInfo)

}

trait DefaultPasswordRepository extends PasswordRepository {

  def savePwd(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    InMemoryData.pwd += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  def findPwd(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    Future.successful(InMemoryData.pwd.get(loginInfo))
  }

}

trait RedisPasswordRepository extends PasswordRepository {

  val redis = RedisConnectionManager.connection
  import redis.dispatcher

  def savePwd(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val redisInfo: HashedWriteKey[String] = PasswordRedisKey(loginInfo, authInfo)
    redis.hSet(redisInfo.key, redisInfo.field, redisInfo.value) map (_ => authInfo)
  }

  def findPwd(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    val redisInfo: HashedReadKey = PasswordRedisKey(loginInfo)
    redis.hGet[String](redisInfo.key, redisInfo.field) map {
      case Some(json) => fromJson(json)
      case None => None
    }
  }

  private def fromJson(json: String): Option[PasswordInfo] = {
    val value = Json.parse(json)
    Json.fromJson[PasswordInfo](value).asOpt
  }

}