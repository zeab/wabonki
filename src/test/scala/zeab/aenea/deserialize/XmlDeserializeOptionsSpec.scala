package zeab.aenea.deserialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.options._
//ScalaTest
import org.scalatest.FunSuite

class XmlDeserializeOptionsSpec extends FunSuite {

  test("Option: None Deserialize") {
    val expectedType: String = "MyOptionStringClass"
    val xml: String = "<myOptionStringClass><myOptionString/></myOptionStringClass>"
    val deserialize: MyOptionStringClass = xmlDeserialize[MyOptionStringClass](xml)
    assert(expectedType == deserialize.getClass.getSimpleName)
  }

  //TODO Expand this...

}