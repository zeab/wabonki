package zeab.aenea

import zeab.aenea.modelsfortest.complexclasses.person.{Horse, Item, Person}

import scala.util.Try
import scala.xml.XML.loadString

object Main {

  def main(args: Array[String]): Unit = {

    val x = <backpack><name>sword</name><type>attack</type></backpack>
    val y = loadString(x.toString)

    val mount = Horse("daisy", 1.4)
    val backpack = List(Item("sword", "attack"), Item("shield", "defense"))
    val p = Person("bob", "warlock", 9, 87.3, Some(true), Some(mount), backpack)

    val xml = XmlSerializer.xmlSerialize(p)
    println(xml)

    val person = XmlSerializer.xmlDeserialize[Person](xml)
    println(person)

    Try()
  }

}
