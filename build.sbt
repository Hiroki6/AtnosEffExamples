
name := "SampleAtnosEff"

version := "0.1"

scalaVersion := "2.13.1"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-Ymacro-annotations"
)

lazy val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % "2.0.0"
)

lazy val circeDependencies = {
  val circeVersion = "0.12.2"
  List(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion
  )
}

lazy val effDependencies = {
  val effVersion = "5.5.2"
  List (
    "org.atnos" %% "eff" % effVersion,
    "org.atnos" %% "eff-doobie" % effVersion,
    "org.atnos" %% "eff-cats-effect" % effVersion
  )
}

lazy val doobieDependencies = {
  List(
    "org.tpolecat" %% "doobie-core"  % "0.8.4"
  )
}

lazy val http4sDependencies = {
  val http4sVersion = "0.21.0-M5"
  List(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-argonaut" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    // Optional for auto-derivation of JSON codecs
    "com.github.alexarchambault" %% "argonaut-shapeless_6.2" % "1.2.0-M11",
    "org.http4s" %% "http4s-json4s-native" % http4sVersion,
    "org.http4s" %% "http4s-json4s-jackson" % http4sVersion
  )
}

lazy val defaultDependencies = {
  List(
    "mysql" % "mysql-connector-java" % "5.1.35",
    "com.github.pureconfig" %% "pureconfig" % "0.12.1",
    "ch.qos.logback"  %  "logback-classic"    % "1.2.+"
  )
}

libraryDependencies ++= defaultDependencies ++ catsDependencies ++ effDependencies ++ doobieDependencies ++ http4sDependencies ++ circeDependencies

scalafmtConfig := Some(file(".scalafmt.conf"))
scalafmtOnCompile := true
