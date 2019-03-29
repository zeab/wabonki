package zeab.aenea2

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, NodeSeq}
import scala.xml.XML.loadString

object XmlDeserialize {

  def xmlDeserialize[T](input: String)(implicit typeTag: TypeTag[T]): T = {
    implicit val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val xml: Elem = loadString(input)
    deserialize(List(typeTag.tpe.toString), xml).asInstanceOf[T]
  }

  private def deserialize(typeNames: List[String], xml: Elem)(implicit mirror: Mirror): Any = {
    //TODO FIx this head so that if you get a "" back you error out
    val reflectedClass: ClassSymbol = mirror.staticClass(typeNames.head)
    val reflectedType: Type = reflectedClass.typeSignature
    val reflectedValue: List[Any] =
      reflectedType.decls
        .filter(param => "value [^_]\\S".r.findFirstIn(param.toString) match {
          case Some(_) => true
          case None => false
        })
        .filterNot(param => param.name.toString.lastOption.getOrElse("") == ' ')
        .map { param =>
          val (paramName, paramTypes): (String, List[String]) = getSymbolInfo(param)
          coreDeserialize(paramName, paramTypes, xml)
        }
        .toList
    val classMirror: ClassMirror = mirror.reflectClass(reflectedClass)
    val constructor: MethodSymbol = reflectedClass.primaryConstructor.asMethod
    val constructorMirror: MethodMirror = classMirror.reflectConstructor(constructor)
    val instance: Any = constructorMirror.apply(reflectedValue: _*)
    instance
  }

  private def coreDeserialize(paramName: String, paramTypes: List[String], xml: Elem)(implicit mirror: Mirror): Any = {
    paramTypes.headOption.getOrElse("") match {
      case "String" | "Any" => (xml \ paramName).text
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
        val nodes: NodeSeq = xml \ paramName
        if(nodes.toString == s"<$paramName/>") List.empty
        else
          nodes.map { node =>
            //Have to add a root tag so that it can be "found" inside the xml... the text is irrelevant existence is all that matters
            coreDeserialize(paramName, paramTypes.drop(1), <root>{node.asInstanceOf[Elem]}</root>)
          }.toList
      case "Option" =>
        val optionValue: Any = coreDeserialize(paramName, paramTypes.drop(1), xml)
        if (optionValue == "") None
        else Some(optionValue)
      case "Unit" | "Either" => "Error!! Unsupported Type"
      case "" => "ERROR!"
      case _ => deserialize(paramTypes, loadString((xml \ paramName).toString))
    }
  }

  private def getSymbolInfo(symbol: Symbol): (String, List[String]) = {
    val paramName: String = symbol.name.toString
    val paramTypes: List[String] = symbol.typeSignature.resultType.toString
      .split("\\[")
      .map(_.replace("]", ""))
      .toList
    (paramName, paramTypes)
  }

}
