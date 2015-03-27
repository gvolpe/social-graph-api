package controllers

import auth.module.{AuthenticatorIdentityModule, JWTAuthenticatorController, RedisAuthenticatorIdentityModule}
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import repository.{NeoRelationshipRepository, RelationshipRepository}

object RelationshipController extends BaseRelationshipController
  with RedisAuthenticatorIdentityModule with NeoRelationshipRepository

trait BaseRelationshipController extends JWTAuthenticatorController {

  self: AuthenticatorIdentityModule with RelationshipRepository =>

  def findFollowers(id: Long) = SecuredAction.async { implicit request =>
    find(Follower, id) map (ids => Ok(Json.toJson(ids)))
  }

  def findFriends(id: Long) = SecuredAction.async { implicit request =>
    find(Friend, id) map (ids => Ok(Json.toJson(ids)))
  }

  def createFriendship = SecuredAction.async(parse.json[Friendship]) { implicit request =>
    val friendship: Friendship = request.body
    for {
      friend <- create(Friend, friendship.me, friendship.friend)
      follower <- create(Follower, friendship.friend, friendship.me)
    } yield (Ok)
  }

  def deleteFriendship = SecuredAction.async(parse.json[Friendship]) { implicit request =>
    val friendship: Friendship = request.body
    for {
      friend <- delete(Friend, friendship.me, friendship.friend)
      follower <- delete(Follower, friendship.friend, friendship.me)
    } yield (Ok)
  }

}