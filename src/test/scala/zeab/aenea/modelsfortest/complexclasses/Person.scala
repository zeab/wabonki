package zeab.aenea.modelsfortest.complexclasses

case class Person(nickName:String, age:Int, contacts:List[PhoneNumber], insurance:Option[Boolean])
