package controllers

import play.api.mvc._

// Authenticated
object RelationshipController extends Controller {

  def findFollowers = TODO

  // Types: FRIENDS, KNOWS
  def findRelationships(`type`: Option[String]) = TODO

  def createRelationship(`type`: String, userId: Long) = TODO

  def deleteRelationship(`type`: String, userId: Long) = TODO

}