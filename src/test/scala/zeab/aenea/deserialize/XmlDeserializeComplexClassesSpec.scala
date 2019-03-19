package zeab.aenea.deserialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.complexclasses.{Horse, Item, Person}
//ScalaTest
import org.scalatest.FunSuite

class XmlDeserializeComplexClassesSpec extends FunSuite {

  test("Person Deserialize") {
    val expectedType: String = "Person"
    val xml: String = "<person><name>bob</name><class>warlock</class><level>9</level><health>87.3</health><soulStone/><mount><name>daisy</name><speed>1.4</speed><backpack></backpack></mount><backpack><name>sword</name><type>attack</type></backpack><backpack><name>shield</name><type>defense</type></backpack><previousDestinations>Hogwarts</previousDestinations><previousDestinations>Yavin 4</previousDestinations></person>"
    val deserializedObj: Person = xmlDeserialize[Person](xml)
    assert(deserializedObj.name == "bob")
    assert(deserializedObj.`class` == "warlock")
    assert(deserializedObj.level == 9)
    assert(deserializedObj.health == 87.3)
    assert(deserializedObj.soulStone.isEmpty)
    assert(deserializedObj.mount.getOrElse(Horse("",0, List.empty)).name == "daisy")
    assert(deserializedObj.mount.getOrElse(Horse("",0, List.empty)).speed == 1.4)
    assert(deserializedObj.mount.getOrElse(Horse("",0, List.empty)).backpack.isEmpty)
    assert(deserializedObj.previousDestinations.headOption.getOrElse("") == "Hogwarts")
    assert(deserializedObj.previousDestinations.lastOption.getOrElse("") == "Yavin 4")
    assert(deserializedObj.backpack.headOption.getOrElse(Item("", "")).name == "sword")
    assert(deserializedObj.backpack.headOption.getOrElse(Item("", "")).`type` == "attack")
    assert(deserializedObj.backpack.lastOption.getOrElse(Item("", "")).name == "shield")
    assert(deserializedObj.backpack.lastOption.getOrElse(Item("", "")).`type` == "defense")
    assert(expectedType == deserializedObj.getClass.getSimpleName)
  }

}