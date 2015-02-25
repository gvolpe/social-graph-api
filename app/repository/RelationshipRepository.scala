package repository

import model.{RelationshipTypeFinder, RelationshipType}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

trait RelationshipRepository {

  def create(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean]

  def delete(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean]

  def find(relationshipType: RelationshipType, id: Long): Future[List[Option[Long]]]

  def findAll: Future[List[Option[Long]]]

}

object NeoRelationshipRepository extends RelationshipRepository {

  import org.anormcypher._

  implicit val connection = Neo4jREST()

  val socialTag = "Social"

  def findAll: Future[List[Option[Long]]] = Future {
    val req: CypherStatement = Cypher( """ MATCH (a:{tag}) RETURN a.id """).on("tag" -> socialTag)
    userListFromStream(req)
  }

  def find(relationshipType: RelationshipType, id: Long): Future[List[Option[Long]]] = Future {
    val relationship = relationshipType.toString.toUpperCase
    // TODO: Report issue to AnormCypher
    //    val req: CypherStatement = Cypher( """ MATCH (a:{tag})-[:`{relationship}`]->(b) WHERE a.id={userId} RETURN u.id """)
    //      .on("tag" -> socialTag)
    //      .on("relationship" -> relationship)
    //      .on("userId" -> id)
    val query = "MATCH (a:" + socialTag + ")-[:" + relationship + "]->(b) WHERE a.id={userId} RETURN b.id"
    val req: CypherStatement = Cypher(query).on("userId" -> id)
    userListFromStream(req)
  }

  private def userListFromStream(req: CypherStatement): List[Option[Long]] = {
    val stream: Stream[CypherResultRow] = req()
    stream.map(row => { row[Option[Long]]("b.id") }).toList
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

    println("QUERY >> " + st.query)

    st.execute()
//    Cypher(query)
//      .on("id1" -> id1)
//      .on("id2" -> id2)
//      .execute()
  }

  def delete(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean] = Future {
    false
  }

}