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

  class DefaultUserController extends BaseUserController
  with DefaultAuthenticatorIdentityModule with DefaultUserRepositoryProvider {

    val identity = UserIdentity("gvolpe@github.com", LoginInfo("gvolpe", "gvolpe@github.com"))
    implicit val env = FakeEnvironment[UserIdentity, JWTAuthenticator](Seq(identity.loginInfo -> identity))

  }

  "UserController" should {

    "Create an user" in new WithApplication {
      val controller = new DefaultUserController
      val body = UserCreation("gvolpe", "gvolpe@github.com")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.UserController.createUser().url)
        .withBody(body)
      val result = controller.createUser(fakeRequest)

      status(result) must be_==(CREATED)
    }

    "Delete an user" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.DELETE, controllers.routes.UserController.deleteUser(2).url)
      val result = controller.deleteUser(2)(fakeRequest)

      status(result) must be_==(OK)
    }

    "Find all users" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUsers().url)
      val result = controller.findUsers(fakeRequest)

      status(result) must be_==(OK)
    }

    "Find user by Id" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUserById(3).url)
      val result = controller.findUserById(3)(fakeRequest)

      status(result) must be_==(NOT_FOUND)
    }

    // TODO: When I get a response on how to test with JWTAuth create a fake request with authenticator
    // https://github.com/merle-/silhouette-rest-seed/issues/3
    // https://groups.google.com/forum/#!topic/play-silhouette/lL7h2hXDYkQ

//    "Find user by Id" in new WithApplication {
//      val identity = UserIdentity("gvolpe@github.com", LoginInfo("gvolpe", "gvolpe@github.com"))
//      implicit val env = FakeEnvironment[UserIdentity, JWTAuthenticator](Seq(identity.loginInfo -> identity))
//
//      val controller = new DefaultUserController
//      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUserById(3).url)
//        .withAuthenticator(controller.identity.loginInfo)
//      val result = controller.findUserById(3)(fakeRequest)
//
//      status(result) must be_==(OK)
//    }
    //.withHeaders("X-Auth-Token" -> token)

  }

}