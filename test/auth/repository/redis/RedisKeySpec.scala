package auth.repository.redis

import auth.UserIdentity
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import org.specs2.mutable._

class RedisKeySpec extends Specification {

  val providerID = "credentials"
  val providerKey = "gvolpe@github.com"
  val loginInfo = LoginInfo(providerID, providerKey)

  "PasswordRedisKey" should {

    val pwdInfo = PasswordInfo("bcrypt", "123456")
    val pwdAsJson = """ {"hasher":"bcrypt","password":"123456"} """.trim

    "Generate a HashedReadKey" in {
      val readKey: HashedReadKey = PasswordRedisKey(loginInfo)
      readKey.key must be_==(providerID)
      readKey.field must be_==(providerKey)
    }

    "Generate a HashedWriteKey" in {
      val writeKey: HashedWriteKey[String] = PasswordRedisKey(loginInfo, pwdInfo)
      writeKey.key must be_==(providerID)
      writeKey.field must be_==(providerKey)
      writeKey.value must be_==(pwdAsJson)
    }

  }

  "UserRedisKey" should {

    "Generate a ReadKey" in {
      val readKey: ReadKey = UserRedisKey(loginInfo)
      readKey.key must be_==(providerID + ":" + providerKey)
    }

    "Generate a WriteKey" in {
      val userIdentity = UserIdentity("gvolpe@github.com", loginInfo)
      val userJson = """ {"email":"gvolpe@github.com","loginInfo":{"providerID":"credentials","providerKey":"gvolpe@github.com"}} """.trim
      val writeKey: WriteKey = UserRedisKey(userIdentity)
      writeKey.key must be_==(providerID + ":" + providerKey)
      writeKey.value must be_==(userJson)
    }

  }

}
