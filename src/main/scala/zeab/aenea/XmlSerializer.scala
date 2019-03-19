package zeab.aenea

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}
import scala.xml.Elem
import scala.xml.XML.loadString

//TODO Think about how to handle Unit (maybe just treat it as an None...?)
//TODO Think about how to handle Either (if its a right maybe treat it as a value and left treat it as None? or maybe String?)
//TODO Come back to this ... https://www.tutorialspoint.com/scala/scala_data_types.htm
//TODO https://www.cs.helsinki.fi/u/wikla/OTS/Sisalto/examples/html/ch26.html
object XmlSerializer {

  //TODO Update this so it returns Either[Error, T] where T can be a String or Elem (Scala Xml)
  def xmlSerialize(input: Any): String = {
    implicit val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val objName: String = input.getClass.getSimpleName.seq(0).toLower + input.getClass.getSimpleName.drop(1)
    s"<$objName>${serialize(input)}</$objName>"
  }

  def xmlDeserialize[T](input: String)(implicit typeTag: TypeTag[T]): T = {
    implicit val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val xml: Elem = loadString(input)
    deserialize(List(typeTag.tpe.toString), xml).asInstanceOf[T]
  }

  private def coreSerialize(obj: Any, paramName: String)(implicit mirror: Mirror): String = {
    val objName: String = obj.getClass.getSimpleName
    if (isPrimitive(objName)) s"<$paramName>$obj</$paramName>"
    else if (objName == "$colon$colon") obj.asInstanceOf[List[Any]].map { node =>
      coreSerialize(node, paramName)
    }.mkString
    else if (objName == "Some" | objName == "None$")
      obj.asInstanceOf[Option[Any]] match {
        case Some(actualValue) => coreSerialize(actualValue, paramName)
        case None => s"<$paramName/>"
      }
    else {
      val nodeType: String = obj.getClass.getSimpleName
      if (isPrimitive(nodeType)) coreSerialize(obj, paramName)
      else s"<$paramName>${serialize(obj)}</$paramName>"
    }
  }

  private def serialize(input: Any)(implicit mirror: Mirror): String = {
    //This is the bit when i already have the obj as an any where I loop though and check the types
    val objType: Type = mirror.classSymbol(input.getClass).toType
    val objInstance: InstanceMirror = mirror.reflect(input)
    //TODO Change this is its a little more selective when applying the _ removal filter for user defined case classes
    val objParams: Iterable[Symbol] = objType.decls
      .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
        case Some(_) => true;
        case None => false
      })
      .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
    objParams
      .map { param => coreSerialize(objInstance.reflectField(param.asTerm).get, param.name.toString) }
      .mkString
  }

  private def deserialize(typeNames: List[String], xml: Elem)(implicit mirror: Mirror): Any = {
    //TODO FIx this head so that if you get a "" back you error out
    val reflectedClass: ClassSymbol = mirror.staticClass(typeNames.head)
    val reflectedType: Type = reflectedClass.typeSignature
    val reflectedValue: List[Any] =
      reflectedType.decls
        .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
          case Some(_) => true;
          case None => false
        })
        .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
        .map { param =>
          val (paramName, paramTypes) = getSymbolInfo(param)
          coreDeserialize(paramName, paramTypes, xml)
        }
        .toList
    val classMirror = mirror.reflectClass(reflectedClass)
    val constructor = reflectedClass.primaryConstructor.asMethod
    val constructorMirror = classMirror.reflectConstructor(constructor)
    val instance = constructorMirror.apply(reflectedValue: _*)
    instance
  }

  private def coreDeserialize(paramName: String, paramTypes: List[String], xml: Elem)(implicit mirror: Mirror): Any = {
    paramTypes.headOption.getOrElse("") match {
      case "String" => (xml \ paramName).text
      case "Char" =>
        //TODO Check up on char
        //This may or may not be right... but...idk if its every used by anyone so low pri
        Try((xml \ paramName).text.headOption.getOrElse(' ')) match {
          case Success(value) =>
            value match {
              case ' ' => value
              case _ => 'E'
            }
          case Failure(_) => ' '
        }
      case "Byte" =>
        Try((xml \ paramName).text.toByte) match {
          case Success(value) => value
          case Failure(_) => ""
        }
      case "Float" =>
        Try((xml \ paramName).text.toFloat) match {
          case Success(value) => value
          case Failure(_) => ""
        }
      case "Long" =>
        Try((xml \ paramName).text.toLong) match {
          case Success(value) => value
          case Failure(_) => ""
        }
      case "Short" =>
        Try((xml \ paramName).text.toShort) match {
          case Success(value) => value
          case Failure(_) => ""
        }
      case "Double" =>
        Try((xml \ paramName).text.toDouble) match {
          case Success(value) => value
          case Failure(_) => ""
        }
      case "Int" =>
        Try((xml \ paramName).text.toInt) match {
          case Success(value) => value
          case Failure(_) => ""
        }
      case "Boolean" =>
        Try((xml \ paramName).text.toBoolean) match {
          case Success(value) => value
          case Failure(_) => ""
        }
      case "List" =>
        //Have to add a root tag so that it can be "found" inside the xml
        (xml \ paramName).map { node =>
          coreDeserialize(paramName, paramTypes.drop(1), <root>
            {node.asInstanceOf[Elem]}
          </root>)
        }.toList
      case "Option" =>
        val paramXml: String = (xml \ paramName).toString
        //Check to make sure its not just an empty node and short circuit any further checks
        "<.*/>".r.findFirstIn(paramXml) match {
          case Some(_) => None
          case None => Some(coreDeserialize(paramName, paramTypes.drop(1), xml))
        }
      case "Any" | "Unit" | "Either" => "Error!! Unsupported Type"
      case "" => "ERROR!"
      case _ => deserialize(paramTypes, loadString((xml \ paramName).toString))
    }
  }

  //Helpers below here
  //TODO Change this so that it only returns 0 1 or 2
  //so that it only splits it on the first [ for the types so that the loops will drop though correctly
  private def getSymbolInfo(symbol: Symbol): (String, List[String]) = {
    val paramName: String = symbol.name.toString
    val paramTypes: List[String] = symbol.typeSignature.resultType.toString
      .split("\\[")
      .map(_.replace("]", ""))
      .toList
    (paramName, paramTypes)
  }

  private def isPrimitive(nameToCheck: String): Boolean = {
    //TODO Now that im only using class names I can remove some of the dupe tags in here...one day...
    val primitives: List[String] = List("Double", "Float", "Long", "Int", "Integer", "Short", "Byte", "Char", "Character", "Unit", "Boolean", "String")
    primitives.contains(nameToCheck)
  }

}