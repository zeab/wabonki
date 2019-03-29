package zeab.aenea2

import zeab.aenea.XmlSerialize

object Main {

  def main(args: Array[String]): Unit = {

    val xml = "<thingy><myThingy>4</myThingy></thingy>"
    val myThingy = Thingy(4)

    val out = XmlSerialize.xmlSerialize(myThingy)

    val in = XmlDeserialize.xmlDeserialize[Thingy](xml)
    println(in)

    println()

  }

}
