package service

import auth.{SignUp, UserIdentity}
import auth.repository.DefaultUserIdentityRepository
import auth.service.{DefaultUserIdentityService, UserIdentityService}
import com.mohiva.play.silhouette.api.LoginInfo
import org.specs2.matcher.FutureMatchers
import org.specs2.mutable._

import scala.concurrent.Future

class UserIdentityServiceTest extends Specification with FutureMatchers {

  "DefaultUserIdentityService" should {

    "Find some user" in {
      val service: UserIdentityService = new DefaultUserIdentityService with DefaultUserIdentityRepository
      val user: Future[Option[UserIdentity]] = service.retrieve(LoginInfo("id", "key"))
      user must be_==(None).await
    }

    "Save and find the user" in {
      val service: UserIdentityService = new DefaultUserIdentityService with DefaultUserIdentityRepository
      val loginInfo = LoginInfo("some-id", "some-key")
      val signUp = SignUp("gvolpe@github.com", "123456")
      val user = UserIdentity(signUp.identifier, signUp.password, loginInfo)

      val result: Future[UserIdentity] = service.add(loginInfo, signUp)
      result must be_==(user).await

      val foundUser: Future[Option[UserIdentity]] = service.retrieve(loginInfo)
      foundUser must be_==(Some(user)).await
    }

  }

}
