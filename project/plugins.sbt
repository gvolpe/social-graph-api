resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.8")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")
