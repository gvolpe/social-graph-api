package repository

import com.mohiva.play.silhouette.core.LoginInfo
import model.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

trait UserRepository {

  def find(loginInfo: LoginInfo): Future[Option[User]]

  def save(user: User): Future[Boolean]

}

trait DefaultUserRepository extends UserRepository {

  def find(loginInfo: LoginInfo): Future[Option[User]] = Future {
    Some(User("gvolpe@github.com", LoginInfo("some-id", "some-key")))
  }

  def save(user: User): Future[Boolean] = Future(true)

}
