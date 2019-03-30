package zeab.aenea

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, NodeSeq}
import scala.xml.XML.loadString

object XmlDeserialize {

  //Entry way into deserialize... this is where all the magic starts
  def xmlDeserialize[T](input: String)(implicit typeTag: TypeTag[T]): Either[Throwable, T] = {
    implicit val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    Try(loadString(input)) match {
      case Success(xml) =>
        deserialize(List(typeTag.tpe.toString), xml) match {
          case Right(obj) => Right(obj.asInstanceOf[T])
          case Left(ex) => Left(ex)
        }
      case Failure(ex) => Left(ex)
    }
  }

  //Main grunt worker to process each case class
  private def deserialize(typeNames: List[String], xml: Elem)(implicit mirror: Mirror): Either[Throwable, Any] = {
    typeNames.headOption match {
      case Some(typeName) =>
        val reflectedClass: ClassSymbol = mirror.staticClass(typeName)
        val reflectedType: Type = reflectedClass.typeSignature
        val reflectedValue: List[Either[Throwable, Any]] =
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
        val flattenReflectedValues: Either[Throwable, List[Any]] =
          reflectedValue.collectFirst { case Left(f) => f }.toLeft {
            reflectedValue.collect { case Right(r) => r }
          }
        flattenReflectedValues match {
          case Right(values) =>
            val classMirror: ClassMirror = mirror.reflectClass(reflectedClass)
            val constructor: MethodSymbol = reflectedClass.primaryConstructor.asMethod
            val constructorMirror: MethodMirror = classMirror.reflectConstructor(constructor)
            val instance: Any = constructorMirror.apply(values: _*)
            Right(instance)
          case Left(ex) => Left(ex)
        }
      case None => Left(new Exception("Empty Type Names list... something broken..."))
    }
  }

  //The bit that actually takes a value and attempts to return it as the value of the type asked
  private def coreDeserialize(paramName: String, paramTypes: List[String], xml: Elem)(implicit mirror: Mirror): Either[Throwable, Any] = {
    paramTypes.headOption.getOrElse("") match {
      case "String" | "Any" => Right((xml \ paramName).text)
//      case "Char" =>
//        //TODO Check up on char
//        //This may or may not be right... but...idk if its every used by anyone so low pri
//        Try((xml \ paramName).text.headOption.getOrElse(' ')) match {
//          case Success(value) =>
//            value match {
//              case ' ' => value
//              case _ => 'E'
//            }
//          case Failure(_) => ' '
//        }
      case "Byte" =>
        Try((xml \ paramName).text.toByte) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Unable to cast value to byte"))
        }
      case "Float" =>
        Try((xml \ paramName).text.toFloat) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Unable to cast value to float"))
        }
      case "Long" =>
        Try((xml \ paramName).text.toLong) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Unable to cast value to long"))
        }
      case "Short" =>
        Try((xml \ paramName).text.toShort) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Unable to cast value to short"))
        }
      case "Double" =>
        Try((xml \ paramName).text.toDouble) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Unable to cast value to double"))
        }
      case "Int" =>
        Try((xml \ paramName).text.toInt) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Unable to cast value to int"))
        }
      case "Boolean" =>
        Try((xml \ paramName).text.toBoolean) match {
          case Success(value) => Right(value)
          case Failure(_) => Left(new Exception("Unable to cast value to boolean"))
        }
      case "List" =>
        val nodes: NodeSeq = xml \ paramName
        if(nodes.toString == s"<$paramName/>") Right(List.empty)
        else{
          val theList: List[Either[Throwable, Any]] =
          nodes.map { node =>
            //Have to add a root tag so that it can be "found" inside the xml... the text is irrelevant existence is all that matters
            coreDeserialize(paramName, paramTypes.drop(1), <root>{node.asInstanceOf[Elem]}</root>)
          }.toList
          //Flatten so that we only keep
          theList.collectFirst { case Left(f) => f }.toLeft {
            theList.collect { case Right(r) => r }
          }
        }
      case "Option" =>
        val optionValue: Either[Throwable, Any] = coreDeserialize(paramName, paramTypes.drop(1), xml)
        if (optionValue.isLeft) Right(None)
        else
          optionValue match {
            case Right(value) => Right(Some(value))
            case Left(ex) => Left(ex)
          }
      case "Unit" | "Either" => Left(new Exception("Error!! Unsupported Type"))
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
