package controllers

import _root_.auth.module.JWTAuthenticatorController
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import repository.NeoRelationshipRepository

object RelationshipController extends JWTAuthenticatorController with NeoRelationshipRepository {

  // TODO: Get user id from the session
  val defaultUserId: Long = 1

  def findFollowers = SecuredAction.async { implicit request =>
    val followers = find(Follower, defaultUserId)
    followers.map( ids => Ok(Json.toJson(ids)) )
  }

  def findFriends = SecuredAction.async { implicit request =>
    val friends = find(Friend, defaultUserId)
    friends.map( ids => Ok(Json.toJson(ids)) )
  }

  def createRelationship(userId: Long) = Action.async { implicit request =>
    for {
      friend <- create(Friend, defaultUserId, userId)
      follower <- create(Follower, userId, defaultUserId)
    } yield (Ok)
  }

  def deleteRelationship(userId: Long) = TODO

}