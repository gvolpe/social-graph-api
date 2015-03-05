package controllers

import auth.module.DefaultAuthenticatorIdentityModule
import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, WithApplication}

object UserControllerSpec extends Specification {

  class DefaultUserController extends BaseUserController with DefaultAuthenticatorIdentityModule with DefaultUserRepositoryProvider

  "UserController" should {

    "Create an user" in new WithApplication {
      val controller = new DefaultUserController
      val jsonBody = Json.obj("username" -> "gvolpe", "email" -> "gvolpe@github.com")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.UserController.createUser().url).withBody(jsonBody)
      // TODO: Bug with fake request on secured actions return an Iteratee[Array[Byte], Result] instead of Future[Result]
      //val result = controller.createFriendship(fakeRequest)
      val result = controller.createUser(fakeRequest).run

      status(result) must be_==(BAD_REQUEST)
      //status(result) must be_==(UNAUTHORIZED)
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