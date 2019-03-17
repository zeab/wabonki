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

    val p = Person("bob", 8 , List(PhoneNumber("456", "345624566"), PhoneNumber("123", "12123124324")), None)
    val e = MyDoubleClass(1.2)
    val r = MyListDoubleClass(List(1.0, 5.3, 7, 3, 2.1))
    val gc = GameConsole("xbox", List(Game("halo", 0), Game("division", 6), Game("other", 2)))
    val o = xmlSerialize(r)
    println(o)

    val ss = r.asJson.noSpaces
    println(ss)

    //val xml = "<myDoubleClass><myDouble>1.2</myDouble></myDoubleClass>"
    val xml = "<person><nickName>bob</nickName><age>8</age><contacts><phoneNumber><areaCode>456</areaCode><number>345624566</number></phoneNumber></contacts><contacts><phoneNumber><areaCode>123</areaCode><number>12123124324</number></phoneNumber></contacts><insurance/></person>"
    //val xml = "<myListDoubleClass><myListDouble>1.0</myListDouble><myListDouble>5.3</myListDouble></myListDoubleClass>"
    //val xml = "<myListDoubleClass><myListDouble>1</myListDouble><myListDouble>5.3</myListDouble></myListDoubleClass>"
    val w = xmlDeserialize[MyListDoubleClass](xml)
    println(w)
  }

}
