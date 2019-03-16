package zeab.aenea

//Imports
import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}
import scala.xml.Elem
import scala.xml.XML.loadString

//TODO Think about how to handle Unit (maybe just treat it as an None...?)
//TODO Think about how to handle Either (if its a right maybe treat it as a value and left treat it as None? or maybe String?)
//TODO Come back to this ... https://www.tutorialspoint.com/scala/scala_data_types.htm
object XmlSerializer {

  //TODO Update this so it returns Either[Error, T] where T can be a String or Elem (Scala Xml)
  def xmlSerialize(input: Any): String = {
    //This is the bit when i already have the obj as an any where I loop though and check the types
    def serialize(obj: Any, paramName: String): String = {
      val objName: String = obj.getClass.getSimpleName
      if (isPrimitive(objName)) s"<$paramName>$obj</$paramName>"
      else if (objName == "$colon$colon") obj.asInstanceOf[List[Any]].map { node => serialize(node, paramName) }.mkString
      else if (objName == "Some" | objName == "None$")
        obj.asInstanceOf[Option[Any]] match {
          case Some(actualValue) => serialize(actualValue, paramName)
          case None => s"<$paramName/>"
        }
      else xmlSerialize(obj)
    }

    val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val objType: Type = mirror.classSymbol(input.getClass).toType
    val objInstance: InstanceMirror = mirror.reflect(input)
    //TODO Change this is its a little more selective when applying the _ removal filter for user defined case classes
    val objParams: Iterable[Symbol] = objType.decls
      .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
        case Some(_) => true;
        case None => false
      })
      .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
    val objName: String = input.getClass.getSimpleName.seq(0).toLower + input.getClass.getSimpleName.drop(1)
    val objValues: String = objParams.map { param => serialize(objInstance.reflectField(param.asTerm).get, param.name.toString)}.mkString
    s"<$objName>$objValues</$objName>"
  }

  def xmlDeserialize[T](rawXml: String)(implicit typeTag: TypeTag[T]): Either[String, T] = {
    val xml: Elem = loadString(rawXml)
    val objParams = typeTag.tpe.decls
      .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
        case Some(_) => true
        case None => false
      })
      .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
      .map{param =>
        val ee = param.asClass.getClass.getSimpleName
        param
      }
    //TODO Clean this up...also add error checking
    val mirror = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = mirror.staticClass(typeTag.tpe.toString)
    val cm = mirror.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructorMirror = cm.reflectConstructor(constructor)
    val instance = constructorMirror.apply{List.empty: _*}
    Right(instance.asInstanceOf[T])
  }

  def xmlDeserialize2[T](rawXml: String)(implicit typeTag: TypeTag[T]): Either[String, T] = {

    //TODO maybe throw a try around this and throw the error... don't know if that's how it happens yet though
    val xml: Elem = scala.xml.XML.loadString(rawXml)

    def makeHappen(rawXml: String)(implicit typeTag: TypeTag[T]): List[Any] = {
      val gg = typeTag.tpe.decls
        .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
          case Some(_) => true;
          case None => false
        })
        .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
        .map { param =>
          if (isPrimitive(param.typeSignature.typeSymbol.name.toString)) {
            param.typeSignature.typeSymbol.name.toString match {
              case "Int" => (xml \ param.name.toString).text.toInt
              case "String" => (xml \ param.name.toString).text
              case "Double" => (xml \ param.name.toString).text.toDouble
              case "Float" => (xml \ param.name.toString).text.toFloat
              case "Long" => (xml \ param.name.toString).text.toLong
              case "Boolean" => (xml \ param.name.toString).text.toBoolean
            }
          }
          else if (param.typeSignature.typeSymbol.name.toString == "Option") {
            //TODO Fix options so they actually return a value is there is one
            val ee = (xml \ param.name.toString).text
            //Maybe i need to unwrap it so that if its a primitive ill return that else ill keep unwrapping
            None
          }
          else if (param.typeSignature.typeSymbol.name.toString == "List") {
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

  private def isPrimitive(nameToCheck: String): Boolean = {
    //TODO Now that im only using class names I can remove some of the dupe tags in here...one day...
    val primitives: List[String] = List("Double", "Float", "Long", "Int", "Integer", "Short", "Byte", "Char", "Character", "Unit", "Boolean", "String")
    primitives.contains(nameToCheck)
  }

}