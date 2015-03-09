package controllers

import auth.UserIdentity
import auth.module.DefaultAuthenticatorIdentityModule
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.test._
import model.UserCreation
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, WithApplication}

class UserControllerSpec extends Specification {

  val identity = UserIdentity("gvolpe@github.com", LoginInfo("gvolpe", "gvolpe@github.com"))
  implicit val fakeEnv = FakeEnvironment[UserIdentity, JWTAuthenticator](Seq(identity.loginInfo -> identity))

  class DefaultUserController extends BaseUserController
  with DefaultAuthenticatorIdentityModule with DefaultUserRepositoryProvider {
    override implicit lazy val env = fakeEnv
  }

  "UserController" should {

    "Create an user" in new WithApplication {
      val controller = new DefaultUserController
      val body = UserCreation("gvolpe", "gvolpe@github.com")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.UserController.createUser().url)
        .withBody(body)
        .withAuthenticator(identity.loginInfo)
      val result = controller.createUser(fakeRequest)

      status(result) must be_==(CREATED)
    }

    "Delete an user" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.DELETE, controllers.routes.UserController.deleteUser(2).url)
        .withAuthenticator(identity.loginInfo)
      val result = controller.deleteUser(2)(fakeRequest)

      status(result) must be_==(OK)
    }

    "Find all users" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUsers().url)
        .withAuthenticator(identity.loginInfo)
      val result = controller.findUsers(fakeRequest)

      status(result) must be_==(OK)
    }

    "Find user by Id" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUserById(3).url)
        .withAuthenticator(identity.loginInfo)
      val result = controller.findUserById(3)(fakeRequest)

      status(result) must be_==(NOT_FOUND)
    }

    "Find user by Id without authentication" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUserById(3).url)
      val result = controller.findUserById(3)(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

    "Create an user and find by Id" in new WithApplication {
      val controller = new DefaultUserController
      val body = UserCreation("jpetrucci", "jpetrucci@github.com")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.UserController.createUser().url)
        .withBody(body)
        .withAuthenticator(identity.loginInfo)
      val result = controller.createUser(fakeRequest)

      val userId: Option[Long] = contentAsJson(result).asOpt[Long]
      userId.isDefined must be_==(true)

      val fakeRequest2 = FakeRequest(Helpers.GET, controllers.routes.UserController.findUserById(userId.get).url)
        .withAuthenticator(identity.loginInfo)
      val result2 = controller.findUserById(userId.get)(fakeRequest2)

      status(result2) must be_==(OK)
    }

  }

}