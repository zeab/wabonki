package zeab.aenea

//Imports
import scala.reflect.runtime.{universe => ru}
import scala.reflect.runtime.universe._
import scala.xml.Elem

//TODO Think about how to handle Unit (maybe just treat it as an None...?)
//TODO Think about how to handle Either (if its a right maybe treat it as a value and left treat it as None? or maybe String?)

object XmlSerializer {

  //TODO Play code golf
  def xmlSerialize(objectToSerialize: Any): String = {
    def objToMap(objectToSerialize: Any): Map[String, Any] = {
      val objClass: Class[_] = objectToSerialize.getClass
      val objMirror: Mirror = runtimeMirror(objClass.getClassLoader)
      val objType: Type = objMirror.classSymbol(objClass).toType
      val instanceMirror: InstanceMirror = objMirror.reflect(objectToSerialize)
      objType.decls
        .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
          case Some(_) => true;
          case None => false
        })
        .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
        .map { param =>
          if (primitiveCheck(param.typeSignature.typeSymbol.name.toString)) param.name.toString -> instanceMirror.reflectField(param.asTerm).get.toString
          else if (param.typeSignature.typeSymbol.name.toString == "List") {
            val theList: List[Any] = instanceMirror.reflectField(param.asTerm).get.asInstanceOf[List[Any]]
            val theListValues: List[Any] = theList.map { value =>
              if (primitiveCheck(value.getClass.getSimpleName)) value
              else objToMap(value)
            }
            param.name.toString -> theListValues
          }
          else if (param.typeSignature.typeSymbol.name.toString == "Option") {
            val wrappedOptionValue: Option[Any] = instanceMirror.reflectField(param.asTerm).get.asInstanceOf[Option[Any]]
            wrappedOptionValue match {
              case Some(value) =>
                if (primitiveCheck(value.getClass.getSimpleName)) param.name.toString -> value
                else param.name.toString -> objToMap(value)
              case None => param.name.toString -> ""
            }
          }
          else if (param.typeSignature.typeSymbol.name.toString == "Either") {
            //TODO Decide what to do about either's... return an error maybe? idk
            "" -> ""
          }
          else param.name.toString -> objToMap(instanceMirror.reflectField(param.asTerm).get)
        }.toMap
    }
    def serialize(objectToSerialize: Map[String, Any]): String = {
      objectToSerialize.map { node =>
        val (nodeName, nodeWrappedValue): (String, Any) = node

        def unwrapNodeValue(nodeWrappedValue: Any): String = {
          val nodeUnwrappedValue: String =
            if (primitiveCheck(nodeWrappedValue.getClass.getSimpleName)) nodeWrappedValue.toString
            else if (nodeWrappedValue.getClass.getSimpleName == "Nil$") ""
            else serialize(nodeWrappedValue.asInstanceOf[Map[String, Any]])
          if (nodeUnwrappedValue == "") s"<$nodeName/>"
          else s"<$nodeName>$nodeUnwrappedValue</$nodeName>"
        }

        if (nodeWrappedValue.getClass.getSimpleName == "$colon$colon") nodeWrappedValue.asInstanceOf[List[Any]].map(unwrapNodeValue).mkString
        else unwrapNodeValue(nodeWrappedValue)
      }.mkString
    }
    val fullObjToMap: Map[String, Any] = Map(objectToSerialize.getClass.getSimpleName.seq(0).toLower + objectToSerialize.getClass.getSimpleName.drop(1) -> objToMap(objectToSerialize))
    serialize(fullObjToMap)
  }

  private def primitiveCheck(objToCheck: String): Boolean = {
    val primitive = List("Double", "Float", "Long", "Int", "Integer", "Short", "Byte", "Char", "Character", "Unit", "Boolean", "String")
    primitive.contains(objToCheck)
  }

  //TODO Actually put some real error checking in throughout the entire process
  def xmlDeserialize[T](rawXml: String)(implicit typeTag: TypeTag[T]): Either[String, T] = {

    //TODO maybe throw a try around this and throw the error... don't know if that's how it happens yet though
    val xml: Elem = scala.xml.XML.loadString(rawXml)

    def makeHappen(rawXml:String)(implicit typeTag: TypeTag[T]): List[Any] ={
      val gg = typeTag.tpe.decls
        .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
          case Some(_) => true;
          case None => false
        })
        .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
        .map{param =>
          if (primitiveCheck(param.typeSignature.typeSymbol.name.toString)){
            param.typeSignature.typeSymbol.name.toString match {
              case "Int" =>  (xml \ param.name.toString).text.toInt
              case "String" =>  (xml \ param.name.toString).text
              case "Double" =>  (xml \ param.name.toString).text.toDouble
              case "Float" => (xml \ param.name.toString).text.toFloat
              case "Long" => (xml \ param.name.toString).text.toLong
              case "Boolean" => (xml \ param.name.toString).text.toBoolean
            }
          }
          else if (param.typeSignature.typeSymbol.name.toString == "Option"){
            //TODO Fix options so they actually return a value is there is one
            val ee = (xml \ param.name.toString).text
            //Maybe i need to unwrap it so that if its a primitive ill return that else ill keep unwrapping
            None
          }
          else if (param.typeSignature.typeSymbol.name.toString == "List"){
            //How do i get myself a list of elem? so that i can apply those into the object at hand?
            List.empty
          }
          else makeHappen((xml \ param.name.toString).toString)
        }.toList
      gg
    }

    val mirror = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = mirror.staticClass(typeTag.tpe.toString)
    val cm = mirror.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructorMirror = cm.reflectConstructor(constructor)
    val instance = constructorMirror.apply(makeHappen(rawXml): _*)
    Right(instance.asInstanceOf[T])
  }

}
