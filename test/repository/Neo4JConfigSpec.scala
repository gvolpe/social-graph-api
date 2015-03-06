package repository

import org.specs2.mutable._
import play.api.test.WithApplication

class Neo4JConfigSpec extends Specification {

  "Neo4JConfig" should {

    "Provide Neo4j configuration from file" in new WithApplication {
      val config: Neo4JConfig = Neo4JConfig()
      config.host must not beEmpty
    }

  }

}
