package zeab.aenea.deserialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.complexclasses.person.Person
//ScalaTest
import org.scalatest.FunSuite

class XmlDeserializeComplexClassesSpec extends FunSuite {

  test("Person Deserialize") {
    val expectedType: String = "Person"
    val xml: String = "<person><name>bob</name><class>warlock</class><level>9</level><health>87.3</health><soulStone/><mount><name>daisy</name><speed>1.4</speed></mount><backpack><name>sword</name><type>attack</type></backpack><backpack><name>shield</name><type>defense</type></backpack></person>"
    val deserializedObj: Person = xmlDeserialize[Person](xml)
    assert(deserializedObj.name == "bob")
    assert(deserializedObj.`class` == "warlock")
    assert(deserializedObj.level == 9)
    assert(deserializedObj.health == 87.3)
    assert(deserializedObj.soulStone.isEmpty)
    //TODO Update the rest of the fields
    assert(expectedType == deserializedObj.getClass.getSimpleName)
  }

}