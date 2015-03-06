package controllers

import auth.module.DefaultAuthenticatorIdentityModule
import model.UserCreation
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, WithApplication}

class UserControllerSpec extends Specification {

  class DefaultUserController extends BaseUserController
  with DefaultAuthenticatorIdentityModule with DefaultUserRepositoryProvider

  "UserController" should {

    "Create an user" in new WithApplication {
      val controller = new DefaultUserController
      val body = UserCreation("gvolpe", "gvolpe@github.com")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.UserController.createUser().url)
        .withBody(body)

        //.withHeaders("X-Auth-Token" -> token)

      val result = controller.createUser(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

    "Delete an user" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.DELETE, controllers.routes.UserController.deleteUser(2).url)
      val result = controller.deleteUser(2)(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

    "Find all users" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUsers().url)
      val result = controller.findUsers(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

    "Find user by Id" in new WithApplication {
      val controller = new DefaultUserController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.UserController.findUserById(3).url)
      val result = controller.findUserById(3)(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

  }

}