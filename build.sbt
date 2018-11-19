
//Imports
import ModuleNames._
import Settings._

lazy val seed = (project in file(seedKey))
  .settings(seedSettings: _*)

lazy val envgrok = (project in file(envGrokKey))
  .settings(envGrokSettings: _*)

lazy val randomness = (project in file(randomnessKey))
  .settings(randomnessSettings: _*)

