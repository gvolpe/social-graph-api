package controllers

import auth.module.{AuthenticatorIdentityModule, JWTAuthenticatorController, RedisAuthenticatorIdentityModule}
import model.User._
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import repository.{NeoUserRepository, UserRepository}

object UserController extends BaseUserController
  with RedisAuthenticatorIdentityModule with NeoUserRepository

trait BaseUserController extends JWTAuthenticatorController {

  self: AuthenticatorIdentityModule with UserRepository =>

  def findUserById(id: Long) = SecuredAction.async { implicit request =>
    findById(id) map {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound
    }
  }

  def findUsers = SecuredAction.async { implicit request =>
    findAll map (user => Ok(Json.toJson(user)))
  }

  def createUser = SecuredAction.async(parse.json[UserCreation]) { implicit request =>
    val user: UserCreation = request.body
    create(user) map (id => Created(Json.toJson(id)))
  }

  def deleteUser(id: Long) = SecuredAction.async { implicit request =>
    delete(id) map (_ => Ok)
  }

}