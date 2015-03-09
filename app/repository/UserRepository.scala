package repository

import model.{UserCreation, User}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait UserRepository {

  def create(user: UserCreation): Future[Option[Long]]

  def delete(id: Long): Future[Boolean]

  def findById(id: Long): Future[Option[User]]

  def findAll: Future[List[Option[User]]]

}

trait NeoUserRepository extends UserRepository with NeoBaseRepository {

  import org.anormcypher._

  def findAll: Future[List[Option[User]]] = Future {
    val query = "MATCH (s:" + socialTag + ") RETURN s.id, s.username, s.email"
    val req: CypherStatement = Cypher(query)
    userListFromStream(req)
  }

  // https://github.com/AnormCypher/AnormCypher/issues/34
  def findById(id: Long): Future[Option[User]] = Future {
    val query = "MATCH (s:" + socialTag + ") WHERE s.id = {userId} RETURN s.id, s.username, s.email"
    val req: CypherStatement = Cypher(query).on("userId" -> id)
    userListFromStream(req).head
  }

  def create(user: UserCreation): Future[Option[Long]] = Future {
    // Create users with a Unique Id
    val query = " MERGE (id:UniqueId{name:'" + socialTag + "'}) " +
      "ON CREATE SET id.count = 1 " +
      "ON MATCH SET id.count = id.count + 1 " +
      "WITH id.count AS uid " +
      "CREATE (s:Social { id: uid, username: '" + user.username + "', email: '" + user.email + "' }) " +
      "RETURN s.id"

    val st: CypherStatement = Cypher(query)
    userIdFromStream(st)
  }

  def delete(id: Long): Future[Boolean] = Future {
    // Delete user and his relationships
    val query = "MATCH (a:" + socialTag + ")-[r]-(b:Social) " +
      "WHERE a.id = {userId} DELETE a, r"
    val st: CypherStatement = Cypher(query).on("userId" -> id)
    st.execute()
  }

}