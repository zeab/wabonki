package zeab.aenea

import zeab.aenea.models._


object Main {

  def main(args: Array[String]): Unit = {

    //scala.collection.immutable.$colon$colon

    val primitives = Primitives("moose", 9, 4.6, 3F, 2L)
    val simpleList = SimpleList("alpha", List("jo", "bert", "liz"), "omega")

    val simpleOption = SimpleOption(None)
    val complexOption = ComplexOption(None)

    val complexList = ComplexList(List(primitives,primitives))
    println(XmlSerializer.xmlSerialize(complexList))

  }

}
