package controllers

import auth.module.DefaultAuthenticatorIdentityModule
import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, WithApplication}

object AuthControllerSpec extends Specification {

  class DefaultAuthController extends BaseAuthController with DefaultAuthenticatorIdentityModule

  "AuthController" should {

    "Show index page" in new WithApplication {
      val controller = new DefaultAuthController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.AuthController.index().url)
      val result = controller.index(fakeRequest)

      status(result) must be_==(OK)
    }

    "SignUp an user" in new WithApplication {
      val controller = new DefaultAuthController
      val jsonBody = Json.obj("identifier" -> "gvolpe@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(OK)
      contentAsString(result) must contain("token")
      contentAsString(result) must contain("expiresAt")
    }

    "SignIn an user" in new WithApplication {
      val controller = new DefaultAuthController
      val jsonBody = Json.obj("identifier" -> "foobar@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signIn().url).withBody(jsonBody)
      val result = controller.signIn(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

    "SignUp & SignIn a valid user" in new WithApplication {
      val controller = new DefaultAuthController
      val jsonBody = Json.obj("identifier" -> "sga@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(OK)
      contentAsString(result) must contain("token")
      contentAsString(result) must contain("expiresAt")

      val fakeRequest2 = FakeRequest(Helpers.POST, controllers.routes.AuthController.signIn().url).withBody(jsonBody)
      val result2 = controller.signIn(fakeRequest2)

      status(result2) must be_==(OK)
      contentAsString(result2) must contain("token")
      contentAsString(result2) must contain("expiresAt")
    }

  }

}