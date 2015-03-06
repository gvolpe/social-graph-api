package controllers

import auth.UserIdentity
import auth.module.{AuthenticatorIdentityModule, DefaultAuthenticatorIdentityModule, JWTAuthenticatorController}
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.UserController._
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.Action
import repository.{NeoRelationshipRepositoryProvider, RelationshipRepository}

object RelationshipController extends BaseRelationshipController
  with DefaultAuthenticatorIdentityModule with NeoRelationshipRepositoryProvider {

  implicit lazy val env: Environment[UserIdentity, JWTAuthenticator] =
    Environment[UserIdentity, JWTAuthenticator](identityService, authenticatorService, providers, eventBus)

}

// TODO: Use SecuredAction later instead of Action
trait BaseRelationshipController extends JWTAuthenticatorController {

  self: AuthenticatorIdentityModule =>

  def repo: RelationshipRepository

  def findFollowers(id: Long) = Action.async { implicit request =>
    val followers = repo.find(Follower, id)
    followers.map(ids => Ok(Json.toJson(ids)))
  }

  def findFriends(id: Long) = Action.async { implicit request =>
    val friends = repo.find(Friend, id)
    friends.map(ids => Ok(Json.toJson(ids)))
  }

  def createFriendship = Action.async(parse.json[Friendship]) { implicit request =>
    val friendship: Friendship = request.body
    for {
      friend <- repo.create(Friend, friendship.me, friendship.friend)
      follower <- repo.create(Follower, friendship.friend, friendship.me)
    } yield (Ok)
  }

  def deleteFriendship = Action.async(parse.json[Friendship]) { implicit request =>
    val friendship: Friendship = request.body
    for {
      friend <- repo.delete(Friend, friendship.me, friendship.friend)
      follower <- repo.delete(Follower, friendship.friend, friendship.me)
    } yield (Ok)
  }

}