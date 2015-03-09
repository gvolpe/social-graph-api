package controllers

import java.util.concurrent.atomic.AtomicLong

import auth.repository.InMemoryData
import model.{User, UserCreation}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repository.UserRepository

import scala.concurrent.Future

trait DefaultUserRepositoryProvider {

  class UserRepoImpl extends DefaultUserRepository
  val repo = new UserRepoImpl

}

trait DefaultUserRepository extends UserRepository {

  def create(userCreation: UserCreation): Future[Option[Long]] = Future {
    val id = new AtomicLong().incrementAndGet()
    val user = User(id, userCreation.username, userCreation.email)
    InMemoryData.users.put(user.id, user)
    Some(id)
  }

  def delete(id: Long): Future[Boolean] = Future {
    InMemoryData.users.remove(id).exists(u => true)
  }

  def findById(id: Long): Future[Option[User]] = Future {
    InMemoryData.users.get(id)
  }

  def findAll: Future[List[Option[User]]] = Future {
    InMemoryData.users.values.toList.map(u => Option(u))
  }

}
