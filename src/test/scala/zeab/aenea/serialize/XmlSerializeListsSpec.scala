package zeab.aenea.serialize

//Imports
import zeab.aenea.XmlSerializer._
import zeab.aenea.modelsfortest.singleclasses.collections.lists._
//ScalaTest
import org.scalatest.FunSuite

class XmlSerializeListsSpec extends FunSuite {

  test("List: Double Serialize") {
    val theList: List[Double] = List(1.0, 5.3, 7, 3, 2.1)
    val obj: MyListDoubleClass = MyListDoubleClass(theList)
    val serializedXml: String = xmlSerialize(obj)
    val expectedXml = validateXml("ListDouble", theList)
    assert(serializedXml == expectedXml)
  }

  test("List: Int Serialize") {
    val theList: List[Int] = List(1, 5, 7, 3, 2)
    val obj: MyListIntClass = MyListIntClass(theList)
    val serializedXml: String = xmlSerialize(obj)
    val expectedXml = validateXml("ListInt", theList)
    assert(serializedXml == expectedXml)
  }

  test("List: String Serialize") {
    val theList: List[String] = List("llama", "panda", "bert")
    val obj: MyListStringClass = MyListStringClass(theList)
    val serializedXml: String = xmlSerialize(obj)
    val expectedXml = validateXml("ListString", theList)
    assert(serializedXml == expectedXml)
  }

  test("List: Long Serialize") {
    val theList: List[Long] = List(5L, 6L, 7L)
    val obj: MyListLongClass = MyListLongClass(theList)
    val serializedXml: String = xmlSerialize(obj)
    val expectedXml = validateXml("ListLong", theList)
    assert(serializedXml == expectedXml)
  }

  test("List: Boolean Serialize") {
    val theList: List[Boolean] = List(false, false, true)
    val obj: MyListBooleanClass = MyListBooleanClass(theList)
    val serializedXml: String = xmlSerialize(obj)
    val expectedXml = validateXml("ListBoolean", theList)
    assert(serializedXml == expectedXml)
  }

  test("List: Short Serialize") {
    val theList: List[Short] = List(4, 1, 22)
    val obj: MyListShortClass = MyListShortClass(theList)
    val serializedXml: String = xmlSerialize(obj)
    val expectedXml = validateXml("ListShort", theList)
    assert(serializedXml == expectedXml)
  }

  def validateXml(key: String, values:List[Any]): String =
    s"<my${key}Class>${values.map{value =>s"<my$key>$value</my$key>"}.mkString}</my${key}Class>"

}