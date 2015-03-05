package controllers

import auth.module.DefaultAuthenticatorIdentityModule
import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, WithApplication}

object RelationshipControllerSpec extends Specification {

  class DefaultRelationshipController extends BaseRelationshipController with DefaultAuthenticatorIdentityModule {

    class DefaultRepoImpl extends DefaultRelationshipRepository

    val repo = new DefaultRepoImpl
  }

  "RelationshipController" should {

    "Create a relationship" in new WithApplication {
      val controller = new DefaultRelationshipController
      val jsonBody = Json.obj("me" -> 1, "friend" -> 2)
      val fakeRequest = FakeRequest(Helpers.POST, controllers.routes.RelationshipController.createFriendship().url).withBody(jsonBody)
      // TODO: Bug with fake request on secured actions return an Iteratee[Array[Byte], Result] instead of Future[Result]
      //val result = controller.createFriendship(fakeRequest)
      val result = controller.createFriendship(fakeRequest).run

      status(result) must be_==(BAD_REQUEST)
      //status(result) must be_==(UNAUTHORIZED)
    }

    "Delete a relationship" in new WithApplication {
      val controller = new DefaultRelationshipController
      val jsonBody = Json.obj("identifier" -> "foobar@github.com", "password" -> "123456")
      // TODO: Bug with fake request on secured actions return an Iteratee[Array[Byte], Result] instead of Future[Result]
      val fakeRequest = FakeRequest(Helpers.DELETE, controllers.routes.RelationshipController.deleteFriendship().url).withBody(jsonBody)
      val result = controller.deleteFriendship(fakeRequest).run

      status(result) must be_==(BAD_REQUEST)
      //status(result) must be_==(UNAUTHORIZED)
    }

    "Find friends" in new WithApplication {
      val controller = new DefaultRelationshipController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.RelationshipController.findFriends(3).url)
      val result = controller.findFriends(3)(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

    "Find followers" in new WithApplication {
      val controller = new DefaultRelationshipController
      val fakeRequest = FakeRequest(Helpers.GET, controllers.routes.RelationshipController.findFollowers(3).url)
      val result = controller.findFollowers(3)(fakeRequest)

      status(result) must be_==(UNAUTHORIZED)
    }

  }

}