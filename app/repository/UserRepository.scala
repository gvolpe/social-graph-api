package repository

import model.{UserCreation, User}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait UserRepository {

  def create(user: UserCreation): Future[Boolean]

  def delete(id: Long): Future[Boolean]

  def findById(id: Long): Future[List[Option[User]]]

  def findAll: Future[List[Option[User]]]

}

trait NeoUserRepository extends UserRepository with SocialBaseRepository {

  import org.anormcypher._

  def findAll: Future[List[Option[User]]] = Future {
    val query = "MATCH (s:" + socialTag + ") RETURN s.id, s.username, s.email"
    val req: CypherStatement = Cypher(query)
    userListFromStream(req)
  }

  // https://github.com/AnormCypher/AnormCypher/issues/34
  def findById(id: Long): Future[List[Option[User]]] = Future {
    val query = "MATCH (s:" + socialTag + ") WHERE s.id = {userId} RETURN s.id, s.username, s.email"
    val req: CypherStatement = Cypher(query).on("userId" -> id)
    userListFromStream(req)
  }

  def create(user: UserCreation): Future[Boolean] = Future {
    // Create users with a Unique Id
    val query = " MERGE (id:UniqueId{name:'" + socialTag + "'}) " +
      "ON CREATE SET id.count = 1 " +
      "ON MATCH SET id.count = id.count + 1 " +
      "WITH id.count AS uid " +
      "CREATE (s:Social { id: uid, username: '" + user.username + "', email: '" + user.email + "' }) " +
      "RETURN s AS social"

    val st: CypherStatement = Cypher(query)
    st.execute()
  }

  def delete(id: Long): Future[Boolean] = Future {
    val query = "MATCH (s:" + socialTag + ") WHERE s.id = {userId} DELETE s"
    val st: CypherStatement = Cypher(query).on("userId" -> id)
    st.execute()
  }

}