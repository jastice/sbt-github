name := "sbt-github"

organization := "org.gestern"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test" withSources() withJavadoc(),
  "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"
)

initialCommands := "import org.gestern.sbtgithub._"

