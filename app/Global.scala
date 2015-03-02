import com.mohiva.play.silhouette.api.SecuredSettings
import play.api.{Mode, GlobalSettings}
import play.api.i18n.Lang
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{RequestHeader, Result}
import play.api.mvc.Results._

import scala.concurrent.Future

object Global extends GlobalSettings with SecuredSettings {

  /**
   * Called when a user is not authenticated.
   *
   * As defined by RFC 2616, the status code of the response should be 401 Unauthorized.
   *
   * @param request The request header.
   * @param lang The currently selected language.
   * @return The result to send to the client.
   */
  override def onNotAuthenticated(request: RequestHeader, lang: Lang): Option[Future[Result]] = {
    Some(Future { Unauthorized(Json.toJson("credentials not correct")) })
  }

  /**
   * Called when a user is authenticated but not authorized.
   *
   * As defined by RFC 2616, the status code of the response should be 403 Forbidden.
   *
   * @param request The request header.
   * @param lang The currently selected language.
   * @return The result to send to the client.
   */
  override def onNotAuthorized(request: RequestHeader, lang: Lang): Option[Future[Result]] = {
    Some(Future { Unauthorized(Json.toJson("credentials not correct")) })
  }

  /**
   * When an exception occurs in your application, the onError operation
   * will be called. The default is to use the internal framework error page:
   */
  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful {
      if (play.api.Play.current.mode == Mode.Dev)
        InternalServerError(Json.toJson("Internal server error " + ex.getMessage))
      else
        InternalServerError(Json.toJson("Oh oh o.O"))
    }
  }

}
