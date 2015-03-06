package controllers

import auth.UserIdentity
import auth.module.DefaultAuthenticatorIdentityModule
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.test._
import model.Friendship
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, WithApplication}

class RelationshipControllerSpec extends Specification {

  val identity = UserIdentity("gvolpe@github.com", LoginInfo("gvolpe", "gvolpe@github.com"))
  implicit val fakeEnv = FakeEnvironment[UserIdentity, JWTAuthenticator](Seq(identity.loginInfo -> identity))

  class DefaultRelationshipController extends BaseRelationshipController
  with DefaultAuthenticatorIdentityModule with DefaultRelationshipRepositoryProvider {
    override implicit lazy val env = fakeEnv
  }

  "RelationshipController" should {

    "Create a relationship" in new WithApplication {
      val controller = new DefaultRelationshipController
      val body = Friendship(me = 1, friend = 2)
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.RelationshipController.createFriendship().url)
        .withBody(body)
        .withAuthenticator(identity.loginInfo)
      val result = controller.createFriendship(fakeRequest)

      status(result) must be_==(OK)
    }

    "Delete a relationship" in new WithApplication {
      val controller = new DefaultRelationshipController
      val body = Friendship(me = 1, friend = 2)
      val fakeRequest = FakeRequest(Helpers.DELETE, controllers.routes.RelationshipController.deleteFriendship().url)
        .withBody(body)
        .withAuthenticator(identity.loginInfo)
      val result = controller.deleteFriendship(fakeRequest)

      status(result) must be_==(OK)
    }

    "Find friends" in new WithApplication {
      val controller = new DefaultRelationshipController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.RelationshipController.findFriends(3).url)
        .withAuthenticator(identity.loginInfo)
      val result = controller.findFriends(3)(fakeRequest)

      status(result) must be_==(OK)
    }

    "Find followers" in new WithApplication {
      val controller = new DefaultRelationshipController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.RelationshipController.findFollowers(3).url)
        .withAuthenticator(identity.loginInfo)
      val result = controller.findFollowers(3)(fakeRequest)

      status(result) must be_==(OK)
    }

    "Find followers without authentication" in new WithApplication {
      val controller = new DefaultRelationshipController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.RelationshipController.findFollowers(3).url)
      val result = controller.findFollowers(3)(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

  }

}