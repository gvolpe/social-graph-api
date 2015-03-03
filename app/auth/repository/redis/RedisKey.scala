package auth.repository.redis

import auth.User
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import play.api.libs.json.Json

sealed trait RedisKey
case class WriteKey(val key: String, val value: String) extends RedisKey
case class ReadKey(val key: String) extends RedisKey
case class HashedWriteKey[T](val key: String, val field: String, val value: T) extends RedisKey
case class HashedReadKey(val key: String, val field: String) extends RedisKey

object PasswordRedisKey {

  def apply(loginInfo: LoginInfo): HashedReadKey = {
    val key = loginInfo.providerID
    val field = loginInfo.providerKey
    HashedReadKey(key, field)
  }

  def apply(loginInfo: LoginInfo, authInfo: PasswordInfo): HashedWriteKey[String] = {
    import auth.Implicits._
    val key = loginInfo.providerID
    val field = loginInfo.providerKey
    val json = Json.toJson(authInfo).toString()
    HashedWriteKey[String](key, field, json)
  }

}

object UserRedisKey {

  def apply(loginInfo: LoginInfo): ReadKey = {
    val key = loginInfo.providerID + ":" + loginInfo.providerKey
    ReadKey(key)
  }

  def apply(user: User): WriteKey = {
    import auth.Implicits._
    val key = user.loginInfo.providerID + ":" + user.loginInfo.providerKey
    val value = Json.toJson(user).toString()
    WriteKey(key, value)
  }

}