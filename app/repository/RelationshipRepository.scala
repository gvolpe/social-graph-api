package repository

import model.RelationshipType
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

trait RelationshipRepository {

  def create(relationshipType: RelationshipType, userId: Long): Future[Boolean]

  def delete(relationshipType: RelationshipType, userId: Long): Future[Boolean]

  def find(relationshipType: RelationshipType, userId: Long): Future[List[Long]]

  def findAll(userId: Long): Future[List[Long]]

}

object DefaultRelationshipRepository {

  import org.anormcypher._

  implicit val connection = Neo4jREST()

  def findAll(userId: Long): Future[List[Option[Long]]] = Future {
    val req: CypherStatement = Cypher( """ MATCH (u:Social) RETURN u.id """)
    val stream: Stream[CypherResultRow] = req()
    stream.map(row => {
      row[Option[Long]]("u.id")
    }).toList
  }

  def find(relationshipType: RelationshipType, userId: Long): Future[List[Option[Long]]] = Future {
    // TODO: Take relationship from the param
    val req: CypherStatement = Cypher( """ MATCH (a:Social)-[:`{relationship}`]->(b) RETURN u.id """)
      .on("relationship" -> "FRIEND")
    val stream: Stream[CypherResultRow] = req()
    stream.map(row => {
      row[Option[Long]]("u.id")
    }).toList
  }

  def create(relationshipType: RelationshipType, userId: Long): Future[Boolean] = Future {
    Cypher( """ CREATE (u:Social { id: {userId}, email: "gvolpe@github.com", company: "GitHub"}) """)
    true
  }

  def delete(relationshipType: RelationshipType, userId: Long): Future[Boolean] = Future {
    false
  }

  // create some test nodes
  //  Cypher(
  //    """ CREATE (a:User { username: "gvolpe", email: "gvolpe@github.com", company: "GitHub"}),
  //               (b:User { username: "hcarbone", email: "hcarbone@github.com", company: "GitHub"}),
  //               (c:User { username: "ncavallo", email: "ncavallo@github.com", company: "GitHub"})
  //    """).execute()

}