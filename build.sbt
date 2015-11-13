name := "sbt-github"

organization := "org.gestern"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test" withSources() withJavadoc(),
  "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "io.spray" %%  "spray-json" % "1.3.2",
  "org.scala-lang" % "scala-swing" % "2.10.+",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"
)

initialCommands := "import org.gestern.sbtgithub._"

sbtPlugin := true