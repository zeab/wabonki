package zeab.aenea.deserialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.complexclasses.person.{Horse, Item, Person}
//ScalaTest
import org.scalatest.FunSuite

class XmlDeserializeComplexClassesSpec extends FunSuite {

  test("List: Person Deserialize") {
    val deserializedClass = xmlDeserialize[Person]("<person><name>bob</name><class>warlock</class><level>10</level><backpack><name>sword</name><type>attack</type></backpack><backpack><name>shield</name><type>defence</type></backpack><mount>7</mount></person>")
    assert("Person" == deserializedClass.getClass.getSimpleName)
  }

}