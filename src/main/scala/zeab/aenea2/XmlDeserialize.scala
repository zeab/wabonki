package zeab.aenea2

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, NodeSeq}
import scala.xml.XML.loadString

object XmlDeserialize {

  def xmlDeserialize[T](input: String)(implicit typeTag: TypeTag[T]): Either[Exception, T] = {
    implicit val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    Try(loadString(input)) match {
      case Success(xml) =>
        if (typeTag.tpe.typeSymbol.name.toString == "Nothing") Left(new Exception("Must specify a type"))
        else Right(deserialize(List(typeTag.tpe.toString), xml).asInstanceOf[T])
      case Failure(ex) => Left(new Exception(ex.getMessage))
    }
  }

  private def deserialize(typeNames: List[String], xml: Elem)(implicit mirror: Mirror): Either[Exception, Any] = {
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

    val ww =
    reflectedValue.collectFirst { case Left(f) => f }.toLeft {
      reflectedValue.collect { case Right(r) => r }
    }
    ww
    val classMirror: ClassMirror = mirror.reflectClass(reflectedClass)
    val constructor: MethodSymbol = reflectedClass.primaryConstructor.asMethod
    val constructorMirror: MethodMirror = classMirror.reflectConstructor(constructor)
    Try(constructorMirror.apply(reflectedValue: _*)) match {
      case Success(instance) => Right(instance)
      case Failure(ex) => Left(new Exception(ex.getMessage))
    }
  }

  private def coreDeserialize(paramName: String, paramTypes: List[String], xml: Elem)(implicit mirror: Mirror): Either[Exception, Any] = {
    paramTypes.headOption.getOrElse("") match {
      case "String" | "Any" => Right((xml \ paramName).text)
      case "Char" =>
        //TODO Check up on char
        //This may or may not be right... but...idk if its every used by anyone so low pri
        Try((xml \ paramName).text.headOption.getOrElse(' ')) match {
          case Success(value) =>
            value match {
              case ' ' => Right(value)
              case _ => Right('E')
            }
          case Failure(_) => Right(' ')
        }
      case "Byte" =>
        Try((xml \ paramName).text.toByte) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Something"))
        }
      case "Float" =>
        Try((xml \ paramName).text.toFloat) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Something"))
        }
      case "Long" =>
        Try((xml \ paramName).text.toLong) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Something"))
        }
      case "Short" =>
        Try((xml \ paramName).text.toShort) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Something"))
        }
      case "Double" =>
        Try((xml \ paramName).text.toDouble) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Something"))
        }
      case "Int" =>
        Try((xml \ paramName).text.toInt) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Something"))
        }
      case "Boolean" =>
        Try((xml \ paramName).text.toBoolean) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Something"))
        }
      case "List" =>
        val nodes: NodeSeq = xml \ paramName
        if(nodes.toString == s"<$paramName/>") Right(List.empty)
        else {
          val theList =
          nodes.map { node =>
            //Have to add a root tag so that it can be "found" inside the xml... the text is irrelevant existence is all that matters
            coreDeserialize(paramName, paramTypes.drop(1), <root>{node.asInstanceOf[Elem]}</root>)
          }.toList
          theList.collectFirst { case Left(f) => f }.toLeft {
            theList.collect { case Right(r) => r }.mkString
          }
        }
      case "Option" =>
        val optionValue: Any = coreDeserialize(paramName, paramTypes.drop(1), xml)
        if (optionValue == "") Right(None)
        else Right(Some(optionValue))
      case "Unit" | "Either" => Left(new Exception("Unsupported Type"))
      case "" => Left(new Exception("Some other exception"))
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

}
