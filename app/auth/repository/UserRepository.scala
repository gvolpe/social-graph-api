package auth.repository

import auth.User
import auth.repository.redis.{WriteKey, ReadKey, RedisConnectionManager, UserRedisKey}
import com.mohiva.play.silhouette.api.LoginInfo
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

import scala.concurrent.Future

trait UserRepository {

  def find(loginInfo: LoginInfo): Future[Option[User]]

  def save(user: User): Future[Any]

}

trait DefaultUserRepository extends UserRepository {

  def find(loginInfo: LoginInfo): Future[Option[User]] = Future {
    InMemoryData.users.get(loginInfo)
  }

  def save(user: User): Future[Any] = Future {
    InMemoryData.users.put(user.loginInfo, user)
  }

}

trait RedisUserRepository extends UserRepository {

  val redis = RedisConnectionManager.connection

  def find(loginInfo: LoginInfo): Future[Option[User]] = {
    val redisInfo: ReadKey = UserRedisKey(loginInfo)
    redis.get(redisInfo.key) map {
      case Some(json) => fromJson(json)
      case None => None
    }
  }

  def save(user: User): Future[Any] = {
    val redisInfo: WriteKey = UserRedisKey(user)
    redis.set(redisInfo.key, redisInfo.value)
  }

  private def fromJson(json: String): Option[User] = {
    import auth.Implicits._
    val value = Json.parse(json)
    Json.fromJson[User](value).asOpt
  }

}