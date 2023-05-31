
name := "word-counter"
organization := "uk.co.company"
version := "1.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)

def commonSettings = Seq(
  scalaVersion := "2.13.10"
)

val root = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(`word-counter`)
  .settings(
    commonSettings,
    libraryDependencies += guice
  )

lazy val translator = (project in file("lib/translator"))
  .settings(
    commonSettings
  )

lazy val `word-counter` = (project in file("lib/word-counter"))
  .dependsOn(translator)
  .settings(
    commonSettings
  )

