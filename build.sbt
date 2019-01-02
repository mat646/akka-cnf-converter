name := "akka-cnf-converter"

version := "0.1"

scalaVersion := "2.12.8"

val circeVersion = "0.11.0"
val akkaVersion = "2.5.19"
val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)
