package model

import java.util.UUID
import com.mohiva.play.silhouette.core.Token
import scala.concurrent.Future

case class TokenUser(id: String, email: String) extends Token

object TokenUser {

  def apply(email: String): TokenUser = TokenUser(UUID.randomUUID().toString, email)

  val tokens = scala.collection.mutable.HashMap[String, TokenUser]()

  def findById(id: String): Future[Option[TokenUser]] = {
    Future.successful(tokens.get(id))
  }

  def save(token: TokenUser): Future[TokenUser] = {
    tokens += (token.id -> token)
    Future.successful(token)
  }

  def delete(id: String): Unit = {
    tokens.remove(id)
  }

}
