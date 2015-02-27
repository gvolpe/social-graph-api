package service

import com.mohiva.play.silhouette.core.LoginInfo
import model.User
import org.specs2.matcher.FutureMatchers
import org.specs2.mutable._
import repository.DefaultUserRepository

import scala.concurrent.Future

class ServiceProviderTest extends Specification with FutureMatchers {

  "UserService" should {

    "Find some user" in {
      val service: UserService = new DefaultUserService with DefaultUserRepository
      val user: Future[Option[User]] = service.retrieve(LoginInfo("id", "key"))
      user must be_==(Some(User("gvolpe@github.com", LoginInfo("some-id", "some-key")))).await
    }

    "Save the user" in {
      val service: UserService = new DefaultUserService with DefaultUserRepository
      val result: Future[Boolean] = service.add(User("gvolpe@github.com", LoginInfo("some-id", "some-key")))
      result must be_==(true).await
    }

  }

}
