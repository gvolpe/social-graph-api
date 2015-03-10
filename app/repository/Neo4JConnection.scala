package repository

import org.anormcypher.Neo4jREST
import play.api.Play
import play.api.Play.current

object Neo4JConnection {

  def apply(): Neo4jREST = {
    val config = Neo4JConfig()
    if (config.path.isDefined && config.username.isDefined)
      Neo4jREST(
        host = config.host,
        port = config.port,
        path = config.path.getOrElse("/db/data/"),
        username = config.username.getOrElse(""),
        password = config.password.getOrElse(""),
        cypherEndpoint = config.cypherEndpoint.getOrElse("cypher"),
        https = config.https.getOrElse(false))
    else
      Neo4jREST()
  }

  def apply(default: Boolean): Neo4jREST = {
    if (default) Neo4jREST()
    else apply()
  }

}

case class Neo4JConfig(val host: String,
                       val port: Int,
                       val path: Option[String],
                       val username: Option[String],
                       val password: Option[String],
                       val cypherEndpoint: Option[String],
                       val https: Option[Boolean])

object Neo4JConfig {

  def apply(): Neo4JConfig = Neo4JConfig(
    host = Play.configuration.getString("neo.host").getOrElse("localhost"),
    port = Play.configuration.getInt("neo.port").getOrElse(7474),
    path = Play.configuration.getString("neo.datapath"),
    username = Play.configuration.getString("neo.username"),
    password = Play.configuration.getString("neo.password"),
    cypherEndpoint = Play.configuration.getString("neo.cypherEndpoint"),
    https = Play.configuration.getBoolean("neo.https")
  )

}
