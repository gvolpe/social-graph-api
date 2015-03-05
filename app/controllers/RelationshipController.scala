package controllers

import auth.module.{AuthenticatorIdentityModule, DefaultAuthenticatorIdentityModule, JWTAuthenticatorController}
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import repository.{NeoRelationshipRepository, RelationshipRepository}

object RelationshipController extends BaseRelationshipController with DefaultAuthenticatorIdentityModule {

  class NeoRepoImpl extends NeoRelationshipRepository
  val repo = new NeoRepoImpl

}

trait BaseRelationshipController extends JWTAuthenticatorController {

  self: AuthenticatorIdentityModule =>

  def repo: RelationshipRepository

  def findFollowers(id: Long) = SecuredAction.async { implicit request =>
    val followers = repo.find(Follower, id)
    followers.map(ids => Ok(Json.toJson(ids)))
  }

  def findFriends(id: Long) = SecuredAction.async { implicit request =>
    val friends = repo.find(Friend, id)
    friends.map(ids => Ok(Json.toJson(ids)))
  }

  def createFriendship = SecuredAction.async(parse.json[Friendship]) { implicit request =>
    val friendship: Friendship = request.body
    for {
      friend <- repo.create(Friend, friendship.me, friendship.friend)
      follower <- repo.create(Follower, friendship.friend, friendship.me)
    } yield (Ok)
  }

  def deleteFriendship = SecuredAction.async(parse.json[Friendship]) { implicit request =>
    val friendship: Friendship = request.body
    for {
      friend <- repo.delete(Friend, friendship.me, friendship.friend)
      follower <- repo.delete(Follower, friendship.friend, friendship.me)
    } yield (Ok)
  }

}