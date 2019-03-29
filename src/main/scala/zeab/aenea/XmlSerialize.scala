package zeab.aenea

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}
import scala.xml.XML.loadString

//TODO Think about how to handle Unit (maybe just treat it as an None...?)
//TODO Think about how to handle Either (if its a right maybe treat it as a value and left treat it as None? or maybe String?)
//TODO Come back to this ... https://www.tutorialspoint.com/scala/scala_data_types.htm
//TODO https://www.cs.helsinki.fi/u/wikla/OTS/Sisalto/examples/html/ch26.html
//TODO Decide what to do about empty value inside a list... do I return a blank... or do I shorten the returned list by 1
object XmlSerialize {

  def xmlSerialize[T](input: Any)(implicit typeTag: TypeTag[T]): Either[Throwable, T] = {
    implicit val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val objName: String = input.getClass.getSimpleName.seq(0).toLower + input.getClass.getSimpleName.drop(1)
    serialize(input) match {
      case Right(xml) =>
        val fullXml: String = s"<$objName>$xml</$objName>"
        typeTag.tpe.typeSymbol.name.toString match {
          case "Nothing" | "String" => Right(fullXml.asInstanceOf[T])
          case "Elem" =>
            Try(loadString(fullXml)) match {
              case Success(x) => Right(x.asInstanceOf[T])
              case Failure(_) => Left(new Exception("Invalid Xml unable to parse"))
            }
          case _ => Left(new Exception(s"Unsupported type ${typeTag.tpe.typeSymbol.name.toString}"))
        }
      case Left(ex) => Left(ex)
    }
  }

  private def serialize(input: Any)(implicit mirror: Mirror): Either[Throwable, String] = {
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
    val possibleXml: Iterable[Either[Throwable, String]] =
      objParams
        .map { param => coreSerialize(objInstance.reflectField(param.asTerm).get, param.name.toString) }
    possibleXml.collectFirst { case Left(f) => f }.toLeft {
      possibleXml.collect { case Right(r) => r }.mkString
    }
  }


  private def coreSerialize(obj: Any, paramName: String)(implicit mirror: Mirror): Either[Throwable, String] = {
    val objName: String = obj.getClass.getSimpleName
    if (isPrimitive(objName)) Right(s"<$paramName>$obj</$paramName>")
    else if (objName == "$colon$colon") {
      val theList: List[Either[Throwable, String]] =
        obj.asInstanceOf[List[Any]].map { node =>
          coreSerialize(node, paramName)
        }
      theList.collectFirst { case Left(f) => f }.toLeft {
        theList.collect { case Right(r) => r }.mkString
      }
    }
    else if (objName == "Some" | objName == "None$")
      obj.asInstanceOf[Option[Any]] match {
        case Some(actualValue) => coreSerialize(actualValue, paramName)
        case None => Right(s"<$paramName/>")
      }
    //TODO Add more unsupported types here
    else if (objName == "Either") {
      Left(new Exception(s"Unsupported Type $objName"))
    }
    else {
      val nodeType: String = obj.getClass.getSimpleName
      if (isPrimitive(nodeType)) coreSerialize(obj, paramName)
      else {
        serialize(obj) match {
          case Right(xml) => Right(s"<$paramName>$xml</$paramName>")
          case Left(ex) => Left(ex)
        }
      }
    }
  }

  private def isPrimitive(nameToCheck: String): Boolean = {
    //TODO Now that im only using class names I can remove some of the dupe tags in here...one day...
    val primitives: List[String] = List("Double", "Float", "Long", "Int", "Integer", "Short", "Byte", "Char", "Character", "Unit", "Boolean", "String")
    primitives.contains(nameToCheck)
  }

}
