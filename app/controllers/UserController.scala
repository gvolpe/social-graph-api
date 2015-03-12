package controllers

import auth.module.{AuthenticatorIdentityModule, JWTAuthenticatorController, RedisAuthenticatorIdentityModule}
import model.User._
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import repository.{NeoUserRepositoryProvider, UserRepository}

object UserController extends BaseUserController
  with RedisAuthenticatorIdentityModule with NeoUserRepositoryProvider

trait BaseUserController extends JWTAuthenticatorController {

  self: AuthenticatorIdentityModule =>

  def repo: UserRepository

  def findUserById(id: Long) = SecuredAction.async { implicit request =>
    repo.findById(id) map {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound
    }
  }

  def findUsers = SecuredAction.async { implicit request =>
    repo.findAll map (user => Ok(Json.toJson(user)))
  }

  def createUser = SecuredAction.async(parse.json[UserCreation]) { implicit request =>
    val user: UserCreation = request.body
    repo.create(user) map (id => Created(Json.toJson(id)))
  }

  def deleteUser(id: Long) = SecuredAction.async { implicit request =>
    repo.delete(id) map (_ => Ok)
  }

}