enablePlugins(GitPlugin)

name := "sbt-github"

organization := "works.mesh"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test" withSources() withJavadoc(),
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "net.caoticode.buhtig" %% "buhtig" % "0.3.1"
)

// sbt plugin dependencies of this plugin
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")

initialCommands := "import org.gestern.sbtgithub._"

sbtPlugin := true