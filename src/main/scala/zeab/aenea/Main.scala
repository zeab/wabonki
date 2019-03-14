package zeab.aenea

import zeab.aenea.models._

import scala.collection.immutable.Queue
import scala.xml.Elem

object Main {

  def main(args: Array[String]): Unit = {
    //val rawXml = "<person><nickName>bob</nickName><address><street>2nd stree</street><zipCode>H0H0H0</zipCode><phoneNumber><areaCode>888</areaCode><phoneNumber>123456789</phoneNumber></phoneNumber></address><address><street>pine ct</street><zipCode>55325</zipCode><phoneNumber><areaCode>999</areaCode><phoneNumber>098765432</phoneNumber></phoneNumber><phoneNumber><areaCode>999</areaCode><phoneNumber>8374637265</phoneNumber></phoneNumber><phoneNumber><areaCode>222</areaCode><phoneNumber>298374627</phoneNumber></phoneNumber></address><address><street>main street</street><zipCode>80808</zipCode><phoneNumber><areaCode>345</areaCode><phoneNumber>23454321</phoneNumber></phoneNumber><phoneNumber><areaCode>567</areaCode><phoneNumber>74564563431</phoneNumber></phoneNumber></address><car><nickName>honey</nickName><features><nikeName>ac</nikeName><installed>yes</installed></features></car></person>"
    //val rawXml = "<scalaObjects><myFloat>1.0</myFloat><myInt>8</myInt><myLong>3</myLong><myOption/><myString>moose</myString><myBoolean>false</myBoolean><myDouble>9.0</myDouble></scalaObjects>"
    val rawXml = "<primitives><myFloat>1.0</myFloat><myInt>8</myInt><myLong>3</myLong><myString>moose</myString><myBoolean>false</myBoolean><myDouble>9.0</myDouble></primitives>"
    //val rawXml = "<simpleClass><age>9</age><anotherSimpleClass><nickName>moose</nickName></anotherSimpleClass></simpleClass>"
    val person = XmlSerializer.xmlDeserialize[Primitives](rawXml)

    println(person)
  }

}
