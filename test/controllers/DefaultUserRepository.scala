package controllers

import model.{User, UserCreation}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repository.UserRepository

import scala.concurrent.Future

trait DefaultUserRepositoryProvider {

  class UserRepoImpl extends DefaultUserRepository
  val repo = new UserRepoImpl

}

trait DefaultUserRepository extends UserRepository {

  def create(user: UserCreation): Future[Boolean] = Future {
    true
  }

  def delete(id: Long): Future[Boolean] = Future {
    true
  }

  def findById(id: Long): Future[Option[User]] = Future {
    None
  }

  def findAll: Future[List[Option[User]]] = Future {
    List()
  }

}
