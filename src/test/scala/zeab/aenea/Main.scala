package zeab.aenea

import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.options.{MyOptionByteClass, MyOptionCharClass, MyOptionIntClass, MyOptionStringClass}

object Main {

  def main(args: Array[String]): Unit = {

    //

    val xml = xmlSerialize(MyOptionCharClass(Some('c')))
    println(xml)

  }

}
