package zeab.aenea

import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.primitives.{MyDoubleClass, MyFloatClass}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import zeab.aenea.modelsfortest.complexclasses.{Game, GameConsole, Person, PhoneNumber}
import zeab.aenea.modelsfortest.singleclasses.collections.lists.MyListDoubleClass

import scala.xml.Elem

object Main {

  def main(args: Array[String]): Unit = {

    //Objects
    val p = Person("bob", 8 , List(PhoneNumber("456", "345624566"), PhoneNumber("123", "12123124324")), Some(true))
    val e = MyDoubleClass(1.2)
    val r = MyListDoubleClass(List(1.0, 5.3, 7, 3, 2.1))
    val gc = GameConsole("xbox", List(Game("halo", 0), Game("division", 6), Game("other", 2)))

    //Xml
    val o = xmlSerialize(p)
    println(o)

    //Json
    val ss = r.asJson.noSpaces
    println(ss)

    val xml = "<person><nickName>bob</nickName><age>8</age><contacts><areaCode>456</areaCode><number>345624566</number></contacts><contacts><areaCode>123</areaCode><number>12123124324</number></contacts><insurance>true</insurance></person>"
    //val xml = "<myDoubleClass><myDouble>1.2</myDouble></myDoubleClass>"
    val w = xmlDes[Person](xml)
    println(w)
  }

}
