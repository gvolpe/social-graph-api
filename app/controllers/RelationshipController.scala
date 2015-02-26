package controllers

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import repository.NeoRelationshipRepository

// TODO: Authenticated
object RelationshipController extends Silhouette[User, CachedCookieAuthenticator] {

  implicit val env: Environment[User, CachedCookieAuthenticator] = ???

  // TODO: Get user id from the session
  val defaultUserId: Long = 1
  val repo = NeoRelationshipRepository

  def findFollowers = Action.async { implicit request =>
    val followers = repo.find(Follower, defaultUserId)
    followers.map( ids => Ok(Json.toJson(ids)) )
  }

  def findFriends = Action.async { implicit request =>
    val friends = repo.find(Friend, defaultUserId)
    friends.map( ids => Ok(Json.toJson(ids)) )
  }

  def createRelationship(userId: Long) = Action.async { implicit request =>
    for {
      friend <- repo.create(Friend, defaultUserId, userId)
      follower <- repo.create(Follower, userId, defaultUserId)
    } yield (Ok)
  }

  def deleteRelationship(userId: Long) = TODO

}