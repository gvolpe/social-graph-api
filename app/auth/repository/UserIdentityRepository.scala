package auth.repository

import auth.UserIdentity
import auth.repository.redis.{WriteKey, ReadKey, RedisConnectionManager, UserRedisKey}
import com.mohiva.play.silhouette.api.LoginInfo
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

import scala.concurrent.Future

trait UserIdentityRepository {

  def find(loginInfo: LoginInfo): Future[Option[UserIdentity]]

  def save(user: UserIdentity): Future[Any]

}

trait DefaultUserIdentityRepository extends UserIdentityRepository {

  def find(loginInfo: LoginInfo): Future[Option[UserIdentity]] = Future {
    InMemoryData.users.get(loginInfo)
  }

  def save(user: UserIdentity): Future[Any] = Future {
    InMemoryData.users.put(user.loginInfo, user)
  }

}

trait RedisUserIdentityRepository extends UserIdentityRepository {

  val redis = RedisConnectionManager.connection

  def find(loginInfo: LoginInfo): Future[Option[UserIdentity]] = {
    val redisInfo: ReadKey = UserRedisKey(loginInfo)
    redis.get(redisInfo.key) map {
      case Some(json) => fromJson(json)
      case None => None
    }
  }

  def save(user: UserIdentity): Future[Any] = {
    val redisInfo: WriteKey = UserRedisKey(user)
    redis.set(redisInfo.key, redisInfo.value)
  }

  private def fromJson(json: String): Option[UserIdentity] = {
    import auth.Implicits._
    val value = Json.parse(json)
    Json.fromJson[UserIdentity](value).asOpt
  }

}