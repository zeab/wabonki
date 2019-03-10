package zeab.aenea

import zeab.aenea.models._

object Main {

  def main(args: Array[String]): Unit = {

    val primitives = Primitives("moose", 9, 4.6, 3F, 2L, true)
    val simpleList = SimpleList("alpha", List("jo", "bert", "liz"), "omega")
    val simpleOption = SimpleOption(Some("llama"))
    val complexOption = ComplexOption(None)

    val address1 = Address("2nd stree", "H0H0H0", List(PhoneNumber("888", "123456789")))
    val address2 = Address("pine ct", "55325", List(PhoneNumber("999", "098765432"),PhoneNumber("999", "8374637265"), PhoneNumber("222", "298374627")))
    val address3 = Address("main street", "80808", List(PhoneNumber("345", "23454321"), PhoneNumber("567", "74564563431")))
    val car = Some(Car("honey", Some(Feature("ac", Some("yes")))))
    val person = Person("bob", List(address1, address2, address3), car)

    val zz = XmlSerializer.xmlSerialize(SimpleList("a", List.empty, "o"))

    val aa = "<person><nickName>bob</nickName><address><street>2nd stree</street><zipCode>H0H0H0</zipCode><phoneNumber><areaCode>888</areaCode><phoneNumber>123456789</phoneNumber></phoneNumber></address><address><street>pine ct</street><zipCode>55325</zipCode><phoneNumber><areaCode>999</areaCode><phoneNumber>098765432</phoneNumber></phoneNumber><phoneNumber><areaCode>999</areaCode><phoneNumber>8374637265</phoneNumber></phoneNumber><phoneNumber><areaCode>222</areaCode><phoneNumber>298374627</phoneNumber></phoneNumber></address><address><street>main street</street><zipCode>80808</zipCode><phoneNumber><areaCode>345</areaCode><phoneNumber>23454321</phoneNumber></phoneNumber><phoneNumber><areaCode>567</areaCode><phoneNumber>74564563431</phoneNumber></phoneNumber></address><car><nickName>honey</nickName><features><nikeName>ac</nikeName><installed>yes</installed></features></car></person>"
    //val bb = XmlSerializer.xmlDeserialize[Person](aa)

    println(zz)
  }

}
