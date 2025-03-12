import Dependencies._

name         := """playframework-uploadFile"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.1"

libraryDependencies ++= Seq(
  ws,
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play"    % "7.0.1" % Test,
  "org.playframework"      %% "play-slick"            % slick,
  "org.playframework"      %% "play-slick-evolutions" % slick,
  "org.postgresql"          % "postgresql"            % postgres,
  "org.mindrot"             % "jbcrypt"               % jbcrypt,
  "org.typelevel"          %% "cats-core"             % catsCore,
  "org.playframework"      %% "play-mailer"           % playMailer,
  "org.playframework"      %% "play-mailer-guice"     % playMailer
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

PlayKeys.devSettings ++= Seq(
  "slick.dbs.default.db.keepAliveConnection" -> "true"
)
