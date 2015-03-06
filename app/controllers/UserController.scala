package controllers

import auth.UserIdentity
import auth.module.{AuthenticatorIdentityModule, BaseJWTAuthenticatorController, DefaultAuthenticatorIdentityModule}
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import model._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.Action
import repository.{NeoUserRepositoryProvider, UserRepository}

object UserController extends BaseUserController with DefaultAuthenticatorIdentityModule with NeoUserRepositoryProvider {

  implicit lazy val env: Environment[UserIdentity, JWTAuthenticator] =
    Environment[UserIdentity, JWTAuthenticator](identityService, authenticatorService, providers, eventBus)

}

// TODO: Use SecuredAction later instead of Action
trait BaseUserController extends BaseJWTAuthenticatorController[UserIdentity, JWTAuthenticator] {

  self: AuthenticatorIdentityModule =>

  import model.User._

  def repo: UserRepository

  def findUserById(id: Long) = Action.async { implicit request =>
    repo.findById(id) map {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound
    }
  }

  def findUsers = Action.async { implicit request =>
    repo.findAll map (user => Ok(Json.toJson(user)))
  }

  def createUser = Action.async(parse.json[UserCreation]) { implicit request =>
    val user: UserCreation = request.body
    repo.create(user) map (_ => Created)
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    repo.delete(id) map (_ => Ok)
  }

}