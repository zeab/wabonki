
//Imports
import Common.seqBaseProjectTemplate
import Versions._
import sbt.Def

object Settings {

  //Settings
  val wabonkiSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(wabonkiVersion)

}
