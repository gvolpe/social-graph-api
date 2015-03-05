package controllers

import auth.module.{DefaultAuthenticatorIdentityModule, JWTAuthenticatorController}
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import repository.NeoRelationshipRepository

object RelationshipController extends JWTAuthenticatorController with DefaultAuthenticatorIdentityModule with NeoRelationshipRepository {

  def findFollowers(id: Long) = SecuredAction.async { implicit request =>
    val followers = find(Follower, id)
    followers.map(ids => Ok(Json.toJson(ids)))
  }

  def findFriends(id: Long) = SecuredAction.async { implicit request =>
    val friends = find(Friend, id)
    friends.map(ids => Ok(Json.toJson(ids)))
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