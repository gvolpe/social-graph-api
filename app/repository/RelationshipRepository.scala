package repository

import model.{User, RelationshipType}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait RelationshipRepository {

  def create(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean]

  def delete(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean]

  def find(relationshipType: RelationshipType, id: Long): Future[List[Option[User]]]

  def findAll: Future[List[Option[User]]]

}

trait NeoRelationshipRepository extends RelationshipRepository with SocialBaseRepository {

  import org.anormcypher._

  def findAll: Future[List[Option[User]]] = Future {
    val req: CypherStatement = Cypher( """ MATCH (s:{tag}) RETURN RETURN s.id, s.username, s.email """).on("tag" -> socialTag)
    userListFromStream(req)
  }

  // https://github.com/AnormCypher/AnormCypher/issues/34
  def find(relationshipType: RelationshipType, id: Long): Future[List[Option[User]]] = Future {
    val relationship = relationshipType.toString.toUpperCase
    val query = "MATCH (a:" + socialTag + ")-[:" + relationship + "]->(s) WHERE a.id={userId} RETURN s.id, s.username, s.email"
    val req: CypherStatement = Cypher(query).on("userId" -> id)
    userListFromStream(req)
  }

  def create(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean] = Future {
    val relationship = relationshipType.toString.toUpperCase
    val query = "MATCH (a:" + socialTag + "),(b:" + socialTag + ") " +
      "WHERE a.id={id1} AND b.id={id2} " +
      "CREATE (a)-[r:" + relationship + "]->(b) " +
      "RETURN b"
    val st: CypherStatement = Cypher(query)
      .on("id1" -> id1)
      .on("id2" -> id2)
    st.execute()
  }

  def delete(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean] = Future {
    val relationship = relationshipType.toString.toUpperCase
    val query = "MATCH (a:" + socialTag +
      ")-[r: " + relationship +
      "]-(b:" + socialTag + ") " +
      "WHERE a.id={id1} AND b.id={id2} " +
      "DELETE r "
    val st: CypherStatement = Cypher(query)
      .on("id1" -> id1)
      .on("id2" -> id2)
    st.execute()
  }

}