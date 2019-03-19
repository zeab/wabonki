package zeab.aenea

import zeab.aenea.modelsfortest.complexclasses.{Horse, Item, Person}

object Main {

  def main(args: Array[String]): Unit = {

    val mount: Horse = Horse("daisy", 1.4, List.empty)
    val backpack: List[Item] = List(Item("sword", "attack"), Item("shield", "defense"))
    val person: Person = Person("bob", "warlock", 9, 87.3, None, Some(mount), backpack, List("Hogwarts", "Yavin 4"))
    val x = XmlSerializer.xmlSerialize(person)

    val xml = "<person><name>bob</name><class>warlock</class><level>9</level><health>87.3</health><soulStone/><mount><name>daisy</name><speed>1.4</speed><backpack></backpack></mount><backpack><name>sword</name><type>attack</type></backpack><backpack><name>shield</name><type>defense</type></backpack><previousDestinations>Hogwarts</previousDestinations><previousDestinations>Yavin 4</previousDestinations></person>"
    val w = XmlSerializer.xmlDeserialize[Person](xml)
    println(w)
  }

}
