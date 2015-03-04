package controllers

import _root_.auth.module.JWTAuthenticatorController
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import repository.NeoUserRepository

import scala.concurrent.Future

object UserController extends JWTAuthenticatorController with NeoUserRepository {

  import User._

  def findUserById(id: Long) = SecuredAction.async { implicit request =>
    findById(id) map (user => Ok(Json.toJson(user)))
  }

  def findUsers = SecuredAction.async { implicit request =>
    findAll map (user => Ok(Json.toJson(user)))
  }

  def createUser = SecuredAction.async(parse.json[UserCreation]) { implicit request =>
    val user: UserCreation = request.body
    create(user) map (_ => Created)
  }

  def deleteUser(userId: Long) = TODO

}