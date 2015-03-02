package service

import auth.{SignUp, User}
import auth.repository.DefaultUserRepository
import auth.service.{DefaultUserService, UserService}
import com.mohiva.play.silhouette.api.LoginInfo
import org.specs2.matcher.FutureMatchers
import org.specs2.mutable._

import scala.concurrent.Future

class ServiceProviderTest extends Specification with FutureMatchers {

  "UserService" should {

    "Find some user" in {
      val service: UserService = new DefaultUserService with DefaultUserRepository
      val user: Future[Option[User]] = service.retrieve(LoginInfo("id", "key"))
      user must be_==(Some(User("gvolpe@github.com", "123456", LoginInfo("some-id", "some-key")))).await
    }

    "Save the user" in {
      val service: UserService = new DefaultUserService with DefaultUserRepository
      val loginInfo = LoginInfo("some-id", "some-key")
      val signUp = SignUp("gvolpe@github.com", "123456")
      val user = User(signUp.identifier, signUp.password, loginInfo)

      val result: Future[User] = service.add(loginInfo, signUp)
      result must be_==(user).await
    }

  }

}
