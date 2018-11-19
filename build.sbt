
//Imports
import ModuleNames._
import Settings._
import Dependencies._

lazy val seed = (project in file(seedKey))
  .settings(seedSettings: _*)
  .settings(libraryDependencies ++= seedDependencies)

lazy val envgrok = (project in file(envGrokKey))
  .settings(envGrokSettings: _*)
  .settings(libraryDependencies ++= envGrokDependencies)

lazy val randomness = (project in file(randomnessKey))
  .settings(randomnessSettings: _*)
  .settings(libraryDependencies ++= randomnessDependencies)

lazy val terminalclient = (project in file(terminalClientKey))
  .settings(terminalClientSettings: _*)
  .settings(libraryDependencies ++= terminalClientDependencies)
  .dependsOn(seed)

lazy val j2sjavanethttpclient = (project in file(j2sJavaNetHttpClientKey))
  .settings(j2sJavaNetHttpClientSettings: _*)
  .settings(libraryDependencies ++= j2sJavaNetHttpClientDependencies)
  .dependsOn(seed)