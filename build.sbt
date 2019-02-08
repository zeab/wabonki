
//Imports
import Settings._
import Dependencies._
import Resolvers.allResolvers

//Sbt Log Level
logLevel := Level.Debug

//Add all the command alias's
CommandAlias.allCommandAlias

lazy val wabonki = (project in file("."))
  .settings(wabonkiSettings: _*)
  .settings(libraryDependencies ++= wabonkiDependencies)
  .settings(allResolvers: _*)