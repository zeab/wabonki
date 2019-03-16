package zeab.aenea

import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.primitives.{MyDoubleClass, MyFloatClass}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scala.xml.Elem

object Main {

  def main(args: Array[String]): Unit = {

    val e = MyDoubleClass(1.2)
    val o = xmlSerialize(e)
    println(o)

//    val xml = "<myDoubleClass><myDouble>1.2</myDouble></myDoubleClass>"
//    val w = xmlDeserialize[MyDoubleClass](xml)
//    println(w)
  }

}
