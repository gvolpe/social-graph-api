package auth.role

import auth.UserIdentity
import com.mohiva.play.silhouette.api.Authorization
import play.api.i18n.Lang
import play.api.mvc.RequestHeader

case class WithRole(role: Role) extends Authorization[UserIdentity] {
  def isAuthorized(user: UserIdentity)(implicit request: RequestHeader, lang: Lang): Boolean = {
    user.roles.contains(role)
  }
}

trait Role{
  def name: String
}

object Role {

  def apply(role: String): Role = role match {
    case Admin.name => Admin
    case SimpleUser.name => SimpleUser
    case _ => Unknown
  }

  def unapply(role: Role): Option[String] = Some(role.name)

}

object Admin extends Role{
  val name = "admin"
}

object SimpleUser extends Role{
  val name = "user"
}

object Unknown extends Role{
  val name = "-"
}
