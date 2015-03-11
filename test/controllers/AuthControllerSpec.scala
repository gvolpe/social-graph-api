package controllers

import auth.UserIdentity
import auth.module.DefaultAuthenticatorIdentityModule
import auth.repository.InMemoryData
import auth.role.{Admin, SimpleUser}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.test._
import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, WithApplication}

class AuthControllerSpec extends Specification {

  val baseUser = UserIdentity(Set(), LoginInfo("baseuser", "baseuser@github.com"))
  val simpleUser = UserIdentity(Set(SimpleUser), LoginInfo("noadmin", "noadmin@github.com"))
  val admin = UserIdentity(Set(Admin), LoginInfo("admin", "admin@github.com"))
  val credentials = Seq(simpleUser.loginInfo -> simpleUser, admin.loginInfo -> admin)
  implicit val fakeEnv = FakeEnvironment[UserIdentity, JWTAuthenticator](credentials)

  class DefaultAuthController extends BaseAuthController with DefaultAuthenticatorIdentityModule
  class FakeAuthController extends BaseAuthController with DefaultAuthenticatorIdentityModule {
    override implicit lazy val env = fakeEnv
  }

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

    "SignUp the same user twice" in new WithApplication {
      val controller = new DefaultAuthController
      val jsonBody = Json.obj("identifier" -> "modersky@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(OK)
      contentAsString(result) must contain("token")
      contentAsString(result) must contain("expiresAt")

      val fakeRequest2 = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result2 = controller.signUp(fakeRequest2)

      status(result2) must be_==(CONFLICT)
    }

    "SignUp with an invalid json body" in new WithApplication {
      val controller = new DefaultAuthController
      val jsonBody = Json.obj("wrongkey" -> "lalalala")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(BAD_REQUEST)
    }

    "SignIn with an invalid json body" in new WithApplication {
      val controller = new DefaultAuthController
      val jsonBody = Json.obj("wrongkey" -> "lalalala")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signIn().url).withBody(jsonBody)
      val result = controller.signIn(fakeRequest)

      status(result) must be_==(BAD_REQUEST)
    }

    "SignUp an user with SimpleUser role and access an admin resource" in new WithApplication {
      val controller = new FakeAuthController
      val jsonBody = Json.obj("identifier" -> "noadmin@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(OK)
      contentAsString(result) must contain("token")
      contentAsString(result) must contain("expiresAt")

      val fakeRequest2 = FakeRequest(Helpers.GET, controllers.routes.AuthController.adminAction().url)
        .withAuthenticator(simpleUser.loginInfo)
      val result2 = controller.adminAction(fakeRequest2)

      status(result2) must be_==(UNAUTHORIZED)
    }

    "SignUp an user with Admin role and access an admin resource" in new WithApplication {
      val controller = new FakeAuthController
      val jsonBody = Json.obj("identifier" -> "admin@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(OK)
      contentAsString(result) must contain("token")
      contentAsString(result) must contain("expiresAt")

      val fakeRequest2 = FakeRequest(Helpers.GET, controllers.routes.AuthController.adminAction().url)
        .withAuthenticator(admin.loginInfo)
      val result2 = controller.adminAction(fakeRequest2)

      status(result2) must be_==(OK)
    }

    "SignUp an user without roles and access an admin resource" in new WithApplication {
      val controller = new FakeAuthController
      val jsonBody = Json.obj("identifier" -> "baseuser@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(OK)
      contentAsString(result) must contain("token")
      contentAsString(result) must contain("expiresAt")

      val fakeRequest2 = FakeRequest(Helpers.GET, controllers.routes.AuthController.adminAction().url)
        .withAuthenticator(baseUser.loginInfo)
      val result2 = controller.adminAction(fakeRequest2)

      status(result2) must be_==(UNAUTHORIZED)
    }

    "SignIn an user within an environment without credentials" in new WithApplication {
      implicit val fakeEnvWithoutCredentials = FakeEnvironment[UserIdentity, JWTAuthenticator](Seq())
      class FakeAuthWithoutCredentialsController extends BaseAuthController with DefaultAuthenticatorIdentityModule {
        override implicit lazy val env = fakeEnvWithoutCredentials
      }

      val controller = new FakeAuthWithoutCredentialsController
      val jsonBody = Json.obj("identifier" -> "foobar@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signIn().url).withBody(jsonBody)
      val result = controller.signIn(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

    "SignIn an user within an environment with wrong credentials" in new WithApplication {
      val controller = new DefaultAuthController
      val jsonBody = Json.obj("identifier" -> "wronguser@github.com", "password" -> "123456")
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.AuthController.signUp().url).withBody(jsonBody)
      val result = controller.signUp(fakeRequest)

      status(result) must be_==(OK)
      contentAsString(result) must contain("token")
      contentAsString(result) must contain("expiresAt")

      InMemoryData.usersIdentity.clear()
      val fakeRequest2 = FakeRequest(Helpers.POST, controllers.routes.AuthController.signIn().url).withBody(jsonBody)
      val result2 = controller.signIn(fakeRequest)

      status(result2) must be_==(UNAUTHORIZED)
    }

    "Global on Error" in new WithApplication {
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.AuthController.adminAction().url)
      val result = play.api.Play.current.global.onError(fakeRequest, new IllegalArgumentException("Testing onError."))

      status(result) must be_==(INTERNAL_SERVER_ERROR)
    }

  }

}