package zeab.sys

//Imports
import scala.util.{Failure, Success, Try}

/**
  * General set of functions for environment variables and system properties
  *
  * @author Kevin Kosnik-Downs (Zeab)
  * @since 2.12
  */
trait SysToolbox {

  /** Returns either a thing as instance of T or a throwable
    *
    * @param theTry The Try to unwrap
    * @return Either[Throwable, T]
    */
  def returnMatch[T](theTry: Try[Any]): Either[Throwable, T] =
    theTry match {
      case Success(value) => Right(value.asInstanceOf[T])
      case Failure(ex) => Left(ex)
    }

  /** Formats the value from a string to the thing specified
    *
    * @param typeName  What kind of format ex. (String, Boolean, Int)
    * @param typeValue The actual value to try and convert to T
    * @return Either[Throwable, T]
    */
  def formatValue[T](typeName: String, typeValue: String): Either[Throwable, T] =
    typeName match {
      case "String" | "Nothing" => Right(typeValue.asInstanceOf[T])
      case "Boolean" => returnMatch(Try(typeValue.toBoolean))
      case "Int" => returnMatch(Try(typeValue.toInt))
      case "Long" => returnMatch(Try(typeValue.toLong))
      case "Short" => returnMatch(Try(typeValue.toShort))
      case "Float" => returnMatch(Try(typeValue.toFloat))
      case "Double" => returnMatch(Try(typeValue.toDouble))
      case _ => Left(new Exception(s"Unsupported Return Type: $typeName"))
    }

}
