package auth.repository

import auth.User
import com.mohiva.play.silhouette.api.LoginInfo
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait UserRepository {

  def find(loginInfo: LoginInfo): Future[Option[User]]

  def save(user: User): Future[Any]

}

trait DefaultUserRepository extends UserRepository {

  def find(loginInfo: LoginInfo): Future[Option[User]] = Future {
    InMemoryRepository.users.get(loginInfo)
  }

  def save(user: User): Future[Any] = Future {
    InMemoryRepository.users.put(user.loginInfo, user)
  }

}
