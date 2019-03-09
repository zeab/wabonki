package zeab.aenea

object Main {

  def main(args: Array[String]): Unit = {

    val person = Person("bob", 8, "spy")

    val personExtra = PersonExtra("igor",  Info(9, "nun", Pet("moose", 22)), Other("blue"))

    val single = Single("yes")

    //val a = XmlSerializer.xmlSerialize(person)
    val b = XmlSerializer.xmlSerialize(single)
    //val c = XmlSerializer.xmlSerialize(5L)

    println(b)

  }

}
