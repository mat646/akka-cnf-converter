name := "akka-cnf-converter"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-remote" % "2.5.19",
  "io.spray" %%  "spray-json" % "1.3.5"
)
