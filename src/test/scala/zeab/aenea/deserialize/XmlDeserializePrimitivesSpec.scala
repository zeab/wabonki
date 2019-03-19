package zeab.aenea.deserialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.primitives._
//ScalaTest
import org.scalatest.FunSuite

class XmlDeserializePrimitivesSpec extends FunSuite{

  test("Double Deserialize") {
    val expectedType: String = "MyDoubleClass"
    val xml: String = makeValidXml("Double", 1.1.toString)
    val deserialize: MyDoubleClass = xmlDeserialize[MyDoubleClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("Float Deserialize") {
    val expectedType: String = "MyDoubleClass"
    val xml: String = makeValidXml("Float", 6.1.toString)
    val deserialize: MyFloatClass = xmlDeserialize[MyFloatClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("Long Deserialize") {
    val expectedType: String = "MyLongClass"
    val xml: String = makeValidXml("Long", 6.toString)
    val deserialize: MyLongClass = xmlDeserialize[MyLongClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("Int Deserialize") {
    val expectedType: String = "MyIntClass"
    val xml: String = makeValidXml("Int", 6.toString)
    val deserialize: MyIntClass = xmlDeserialize[MyIntClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("Short Deserialize") {
    val expectedType: String = "MyShortClass"
    val xml: String = makeValidXml("Short", 6.toString)
    val deserialize: MyShortClass = xmlDeserialize[MyShortClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("Byte Deserialize") {
    val expectedType: String = "MyByteClass"
    val xml: String = makeValidXml("Byte", 6.toString)
    val deserialize: MyByteClass = xmlDeserialize[MyByteClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("Char Deserialize") {
    val expectedType: String = "MyCharClass"
    val xml: String = makeValidXml("Char", 'c'.toString)
    val deserialize: MyCharClass = xmlDeserialize[MyCharClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("Boolean Deserialize") {
    val expectedType: String = "MyBooleanClass"
    val xml: String = makeValidXml("Boolean", false.toString)
    val deserialize: MyBooleanClass = xmlDeserialize[MyBooleanClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  test("String Deserialize") {
    val expectedType: String = "MyStringClass"
    val xml: String = makeValidXml("String", "llama")
    val deserialize: MyStringClass = xmlDeserialize[MyStringClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  //TODO Unit...

  def makeValidXml(key:String, value:String): String =
    s"<my${key}Class><my$key>$value</my$key></my${key}Class>"

}
