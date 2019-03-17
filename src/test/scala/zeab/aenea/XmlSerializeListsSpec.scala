package zeab.aenea

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.complexclasses.{Game, GameConsole}
import zeab.aenea.modelsfortest.singleclasses.collections.lists.MyListDoubleClass
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializeListsSpec extends FunSuite {

  test("List: Double Serialize") {
    val xml = xmlSerialize(MyListDoubleClass(List(1.0, 5.3, 7, 3, 2.1)))
    assert(xml == "<myListDoubleClass><myListDouble>1.0</myListDouble><myListDouble>5.3</myListDouble><myListDouble>7.0</myListDouble><myListDouble>3.0</myListDouble><myListDouble>2.1</myListDouble></myListDoubleClass>")
  }

  test("List: GameConsole Serialize") {
    val xml = xmlSerialize(GameConsole("xbox", List(Game("halo", 0), Game("division", 6), Game("other", 2))))
    assert(xml == "<gameConsole><name>xbox</name><games><name>halo</name><level>0</level></games><games><name>division</name><level>6</level></games><games><name>other</name><level>2</level></games></gameConsole>")
  }

}