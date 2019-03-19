package zeab.aenea.serialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.complexclasses.{Horse, Item, Person}
import zeab.aenea.modelsfortest.singleclasses.options.MyOptionListClass
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializeComplexClassesSpec extends FunSuite {

  test("Person Serialize") {
    val mount: Horse = Horse("daisy", 1.4, List.empty)
    val backpack: List[Item] = List(Item("sword", "attack"), Item("shield", "defense"))
    val person: Person = Person("bob", "warlock", 9, 87.3, None, Some(mount), backpack, List("Hogwarts", "Yavin 4"))
    val obj: Person = person
    val serializedXml: String = xmlSerialize(obj)
    val expectedXml: String = "<person><name>bob</name><class>warlock</class><level>9</level><health>87.3</health><soulStone/><mount><name>daisy</name><speed>1.4</speed><backpack></backpack></mount><backpack><name>sword</name><type>attack</type></backpack><backpack><name>shield</name><type>defense</type></backpack><previousDestinations>Hogwarts</previousDestinations><previousDestinations>Yavin 4</previousDestinations></person>"
    assert(serializedXml == expectedXml)
  }

}