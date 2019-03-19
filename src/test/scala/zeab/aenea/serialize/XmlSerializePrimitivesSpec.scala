package zeab.aenea.serialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.primitives._
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializePrimitivesSpec extends FunSuite {

  test("Double Serialize") {
    val obj: MyDoubleClass = MyDoubleClass(1.1)
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Double", 1.1.toString))
  }

  test("Float Serialize") {
    val obj: MyFloatClass = MyFloatClass(6.1F)
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Float", 6.1.toString))
  }

  test("Long Serialize") {
    val obj: MyLongClass = MyLongClass(6L)
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Long", 6.toString))
  }

  test("Int Serialize") {
    val obj: MyIntClass = MyIntClass(8)
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Int", 8.toString))
  }

  test("Short Serialize") {
    val obj: MyShortClass = MyShortClass(8)
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Short", 8.toString))
  }

  test("Byte Serialize") {
    val obj: MyByteClass = MyByteClass('c'.toByte)
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Byte", 'c'.toByte.toString))
  }

  test("Char Serialize") {
    val obj: MyCharClass = MyCharClass('c')
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Char", 'c'.toString))
  }

  test("Boolean Serialize") {
    val obj: MyBooleanClass = MyBooleanClass(false)
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("Boolean", false.toString))
  }

  test("String Serialize") {
    val obj: MyStringClass = MyStringClass("llama")
    val serializedXml: String = xmlSerialize(obj)
    assert(serializedXml == validateXml("String", "llama"))
  }

  //TODO Unit...

  def validateXml(key: String, value: String): String =
    s"<my${key}Class><my$key>$value</my$key></my${key}Class>"

}
