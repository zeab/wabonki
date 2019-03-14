package zeab.aenea

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.options._
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializeOptionSpec extends FunSuite {

  test("Option: None Serialize") {
    val xml = xmlSerialize(MyOptionStringClass(None))
    assert(xml == "<myOptionStringClass><myOptionString/></myOptionStringClass>")
  }

  test("Option: Some[String] Serialize") {
    val xml = xmlSerialize(MyOptionStringClass(Some("llama")))
    assert(xml == validateXml("String", "llama"))
  }

  test("Option: Some[Int] Serialize") {
    val xml = xmlSerialize(MyOptionIntClass(Some(8)))
    assert(xml == validateXml("Int", 8.toString))
  }

  test("Option: Some[Boolean] Serialize") {
    val xml = xmlSerialize(MyOptionBooleanClass(Some(false)))
    assert(xml == validateXml("Boolean", false.toString))
  }

  test("Option: Some[Byte] Serialize") {
    val xml = xmlSerialize(MyOptionByteClass(Some('c'.toByte)))
    assert(xml == validateXml("Byte", 'c'.toByte.toString))
  }

  test("Option: Some[Char] Serialize") {
    val xml = xmlSerialize(MyOptionCharClass(Some('c')))
    assert(xml == validateXml("Char", 'c'.toString))
  }

  test("Option: Some[Double] Serialize") {
    val xml = xmlSerialize(MyOptionDoubleClass(Some(1.1)))
    assert(xml == validateXml("Double", 1.1.toString))
  }

  test("Option: Some[Float] Serialize") {
    val xml = xmlSerialize(MyOptionFloatClass(Some(1.1F)))
    assert(xml == validateXml("Float", 1.1.toString))
  }

  test("Option: Some[Long] Serialize") {
    val xml = xmlSerialize(MyOptionLongClass(Some(1L)))
    assert(xml == validateXml("Long", 1.toString))
  }

  test("Option: Some[Short] Serialize") {
    val xml = xmlSerialize(MyOptionShortClass(Some(1)))
    assert(xml == validateXml("Short", 1.toString))
  }

  //TODO Unit...

  def validateXml(key:String, value:String): String =
    s"<myOption${key}Class><myOption$key>$value</myOption$key></myOption${key}Class>"

}