package zeab.aenea.serialize

//Imports
import zeab.aenea.XmlSerialize._
import zeab.aenea.modelsfortest.singleclasses.primitives._
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializePrimitivesSpec extends FunSuite {

  test("Double Serialize") {
    val obj: MyDoubleClass = MyDoubleClass(1.1)
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Double", 1.1.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("Float Serialize") {
    val obj: MyFloatClass = MyFloatClass(6.1F)
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Float", 6.1.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("Long Serialize") {
    val obj: MyLongClass = MyLongClass(6L)
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Long", 6.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("Int Serialize") {
    val obj: MyIntClass = MyIntClass(8)
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Int", 8.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("Short Serialize") {
    val obj: MyShortClass = MyShortClass(8)
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Short", 8.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("Byte Serialize") {
    val obj: MyByteClass = MyByteClass('c'.toByte)
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Byte", 'c'.toByte.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("Char Serialize") {
    val obj: MyCharClass = MyCharClass('c')
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Char", 'c'.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("Boolean Serialize") {
    val obj: MyBooleanClass = MyBooleanClass(false)
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("Boolean", false.toString)
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  test("String Serialize") {
    val obj: MyStringClass = MyStringClass("llama")
    val serializedXml: Either[Throwable, String] = xmlSerialize(obj)
    val expectedXml: String = validateXml("String", "llama")
    assert{serializedXml match {
      case Right(xml) => xml == expectedXml
      case Left(_) => false
    }}
  }

  //TODO Unit...

  def validateXml(key: String, value: String): String =
    s"<my${key}Class><my$key>$value</my$key></my${key}Class>"

}
