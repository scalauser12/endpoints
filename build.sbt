ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val http4sVersion = "0.23.18"

lazy val root = (project in file("."))
  .settings(
    name := "Endpoints",
    libraryDependencies ++= Seq(
      "org.endpoints4s" %% "algebra" % "1.9.0",
      "org.endpoints4s" %% "json-schema-generic" % "1.9.0",
      "org.endpoints4s" %% "http4s-server" % "10.1.0",
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-ember-client" % http4sVersion
    )
  )
