name := """api-gateway"""
organization := "com.myob"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(guice, ws)
libraryDependencies += "com.myob" % "scala-common_2.12" % "0.0.2"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.myob.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.myob.binders._"

resolvers += "Artifactory" at "http://artifactory.myobparking.io/artifactory/sbt-dev/"