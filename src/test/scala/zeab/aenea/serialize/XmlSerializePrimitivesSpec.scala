package zeab.aenea.serialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.primitives._
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializePrimitivesSpec extends FunSuite {

  test("Double Serialize") {
    val xml = xmlSerialize(MyDoubleClass(1.1))
    assert(xml == validateXml("Double", 1.1.toString))
  }

  test("Float Serialize") {
    val xml = xmlSerialize(MyFloatClass(6.1F))
    assert(xml == validateXml("Float", 6.1.toString))
  }

  test("Long Serialize") {
    val xml = xmlSerialize(MyLongClass(6L))
    assert(xml == validateXml("Long", 6.toString))
  }

  test("Int Serialize") {
    val xml = xmlSerialize(MyIntClass(8))
    assert(xml == validateXml("Int", 8.toString))
  }

  test("Short Serialize") {
    val xml = xmlSerialize(MyShortClass(8))
    assert(xml == validateXml("Short", 8.toString))
  }

  test("Byte Serialize") {
    val xml = xmlSerialize(MyByteClass('c'.toByte))
    assert(xml == validateXml("Byte", 'c'.toByte.toString))
  }

  test("Char Serialize") {
    val xml = xmlSerialize(MyCharClass('c'))
    assert(xml == validateXml("Char", 'c'.toString))
  }

  test("Boolean Serialize") {
    val xml = xmlSerialize(MyBooleanClass(false))
    assert(xml == validateXml("Boolean", false.toString))
  }

  test("String Serialize") {
    val xml = xmlSerialize(MyStringClass("llama"))
    assert(xml == validateXml("String", "llama"))
  }

  //TODO Unit...

  def validateXml(key: String, value: String): String =
    s"<my${key}Class><my$key>$value</my$key></my${key}Class>"

}
