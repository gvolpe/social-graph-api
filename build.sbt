name := """social-graph-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

resolvers := ("anormcypher" at "http://repo.anormcypher.org/") +: resolvers.value

val silhouetteVersion = "2.0-RC1"

libraryDependencies ++= Seq(
  "org.anormcypher" %% "anormcypher" % "0.6.0",
  "com.mohiva" %% "play-silhouette" % silhouetteVersion,
  "com.livestream" %% "scredis" % "2.0.6",
  "com.mohiva" %% "play-silhouette-testkit" % silhouetteVersion % "test",
  "com.newrelic.agent.java" % "newrelic-agent" % "3.14.0"
)

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := ".*Neo4JConnection.*;.*RedisConnectionManager.*;.*Redis.*Repository.*;.*Redis.*Module.*;.*Neo.*Repository.*;.*index.*;.*main.*;.*Reverse.*;.*Routes.*"