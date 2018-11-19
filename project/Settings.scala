
//Imports
import Common.seqBaseProjectTemplate
import sbt.Def
import Versions._

object Settings {

  //Settings
  val seedSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(seedVersion)
  val envGrokSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(envGrokVersion)
  val randomnessSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(randomnessVersion)

}
