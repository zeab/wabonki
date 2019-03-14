package zeab.aenea

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.primitives._
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializePrimitivesSpec extends FunSuite{

  test("Double Serialize") {
    val xml = xmlSerialize(MyDoubleClass(1.1))
    assert(xml == "<myDoubleClass><myDouble>1.1</myDouble></myDoubleClass>")
  }

  test("Float Serialize") {
    val xml = xmlSerialize(MyFloatClass(6.1F))
    assert(xml == "<myFloatClass><myFloat>6.1</myFloat></myFloatClass>")
  }

  test("Long Serialize") {
    val xml = xmlSerialize(MyLongClass(6L))
    assert(xml == "<myLongClass><myLong>6</myLong></myLongClass>")
  }

  test("Int Serialize") {
    val xml = xmlSerialize(MyIntClass(8))
    assert(xml == "<myIntClass><myInt>8</myInt></myIntClass>")
  }

  test("Short Serialize") {
    val xml = xmlSerialize(MyShortClass(8))
    assert(xml == "<myShortClass><myShort>8</myShort></myShortClass>")
  }

  test("Byte Serialize") {
    val xml = xmlSerialize(MyByteClass('c'.toByte))
    assert(xml == "<myByteClass><myByte>99</myByte></myByteClass>")
  }

  test("Char Serialize") {
    val xml = xmlSerialize(MyCharClass('c'))
    assert(xml == "<myCharClass><myChar>c</myChar></myCharClass>")
  }

  test("Boolean Serialize") {
    val xml = xmlSerialize(MyBooleanClass(false))
    assert(xml == "<myBooleanClass><myBoolean>false</myBoolean></myBooleanClass>")
  }

  test("String Serialize") {
    val xml = xmlSerialize(MyStringClass("llama"))
    assert(xml == "<myStringClass><myString>llama</myString></myStringClass>")
  }

  //Unit...

}
