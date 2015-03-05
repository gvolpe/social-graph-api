name := """social-graph-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

resolvers := ("anormcypher" at "http://repo.anormcypher.org/") +: resolvers.value

libraryDependencies ++= Seq(
  "org.anormcypher" %% "anormcypher" % "0.6.0",
  "com.mohiva" %% "play-silhouette" % "2.0-RC1",
  "com.livestream" %% "scredis" % "2.0.6",
  "org.scalatestplus" %% "play" % "1.2.0" % "test"
)

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := ".*RedisConnectionManager.*;.*Neo4JConnection.*"