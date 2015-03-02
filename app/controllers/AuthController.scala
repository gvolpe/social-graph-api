package controllers

import _root_.auth.module.AuthenticatorController
import _root_.auth.{ModelImplicits, SignUp, Token}
import com.mohiva.play.silhouette.api.exceptions.AuthenticationException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, SignUpEvent}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, Json}
import play.api.mvc._

import scala.concurrent.Future

object AuthController extends AuthenticatorController {

  def index = Action {
    Ok(views.html.index("Social Graph API"))
  }

  import auth.ModelImplicits._

  def signUp = Action.async(parse.json) { implicit request =>
    request.body.validate[SignUp].map { signUp =>
      val loginInfo = LoginInfo(CredentialsProvider.ID, signUp.identifier)
      (identityService.retrieve(loginInfo).flatMap {
        case None => /* user not already exists */
          val authInfo = passwordHasher.hash(signUp.password)
          for {
            user <- identityService.add(loginInfo, signUp)
            authInfo <- authInfoService.save(loginInfo, authInfo)
            authenticator <- env.authenticatorService.create(loginInfo)
            token <- env.authenticatorService.init(authenticator)
            result <- env.authenticatorService.embed(token, Future.successful {
              Ok(Json.toJson(Token(token = token, expiresAt = authenticator.expirationDate)))
            })
          } yield {
            env.eventBus.publish(SignUpEvent(user, request, request2lang))
            env.eventBus.publish(LoginEvent(user, request, request2lang))
            result
          }
        case Some(u) => /* user already exists! */
          Future.successful(Conflict(Json.toJson("user already exists")))
      })
    }.recoverTotal {
      case error =>
        Future.successful(BadRequest(Json.toJson(JsError.toFlatJson(error))))
    }
  }

  def signIn = Action.async(parse.json) { implicit request =>
    request.body.validate[Credentials].map { credentials =>
      (env.providers.get(CredentialsProvider.ID) match {
        case Some(p: CredentialsProvider) => p.authenticate(credentials)
        case _ => Future.failed(new AuthenticationException(s"Cannot find credentials provider"))
      }).flatMap { loginInfo =>
        identityService.retrieve(loginInfo).flatMap {
          case Some(user) => env.authenticatorService.create(user.loginInfo).flatMap { authenticator =>
            env.eventBus.publish(LoginEvent(user, request, request2lang))
            env.authenticatorService.init(authenticator).flatMap { token =>
              env.authenticatorService.embed(token, Future.successful {
                Ok(Json.toJson(Token(token = token, expiresAt = authenticator.expirationDate)))
              })
            }
          }
          case None =>
            Future.failed(new AuthenticationException("Couldn't find user"))
        }
      }.recoverWith(exceptionHandler)
    }.recoverTotal {
      case error => Future.successful(BadRequest(Json.obj("message" -> JsError.toFlatJson(error))))
    }
  }

}