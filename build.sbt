name := """social-graph-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

resolvers ++= Seq(
  "Atlassian Releases" at "https://maven.atlassian.com/public/",
  "anormcypher" at "http://repo.anormcypher.org/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "org.anormcypher" %% "anormcypher" % "0.6.0",
  "com.mohiva" %% "play-silhouette" % "2.0-RC1",
  "com.livestream" %% "scredis" % "2.0.6"
)
