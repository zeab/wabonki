package zeab.aenea.serialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.complexclasses.person.{Horse, Item, Person}
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializeComplexClassesSpec extends FunSuite {

  //TODO Update the results so they are actually correct...
  test("List: Person Serialize") {
    //val xml = xmlSerialize(Person("bob", "warlock", 10, List(Item("sword", "attack"), Item("shield", "defence")), Some(7)))
    //assert(xml == "<person><name>bob</name><class>warlock</class><level>10</level><backpack><name>sword</name><type>attack</type></backpack><backpack><name>shield</name><type>defence</type></backpack><mount><name>bert</name><speed>1.5</speed></mount></person>")
    assert(false)
  }

}