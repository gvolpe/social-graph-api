package repository

import model.User
import org.anormcypher.{CypherResultRow, CypherStatement}

trait NeoBaseRepository {

  implicit val connection = Neo4JConnection(default = true)

  val socialTag = "Social"

  def userListFromStream(req: CypherStatement): List[Option[User]] = {
    val stream: Stream[CypherResultRow] = req()
    stream.map(row => {
      val id = row[Option[Long]]("s.id")
      val username = row[Option[String]]("s.username")
      val email = row[Option[String]]("s.email")
      if (id.isDefined) Some(User(id.get, username.get, email.get))
      else None
    }).toList
  }

}
