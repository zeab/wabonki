package zeab.aenea2

object Main {

  def main(args: Array[String]): Unit = {

    val xml = "<thingy><myThingy>4</myThingy></thingy>"

    val in = XmlDeserialize.xmlDeserialize[Thingy](xml)
    println(in)

  }

}
