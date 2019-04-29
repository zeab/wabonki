package zeab.sys

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}

/**
  * A collection of useful functions to get environment variables in safe ways
  *
  * @author Kevin Kosnik-Downs (Zeab)
  * @since 2.12
  */
trait EnvironmentVariables extends SysToolbox {

  /** Looks for the value of the environment variable specified
    *
    * @param keyToLookFor The environment variables key to look for
    * @param typeTag      The return type (no need to actually specify this implicit it will default to the supplied T)
    * @return Either[Throwable, T]
    */
  def getEnvVar[T](keyToLookFor: String)(implicit typeTag: TypeTag[T]): Either[Throwable, T] = {
    Try(sys.env(keyToLookFor)) match {
      case Success(envVal) => formatValue[T](typeTag.tpe.typeSymbol.name.toString, envVal)
      case Failure(ex) => Left(ex)
    }
  }

  /** Looks for the value of the environment variable specified and throws the error or a custom one if not found
    *
    * @param keyToLookFor The environment variables key to look for
    * @param customError  The custom throwable to throw if the keyToLookFor is not found
    * @return T
    */
  def getEnvVar[T](keyToLookFor: String, customError: Option[Throwable] = None)(implicit typeTag: TypeTag[T]): T = {
    Try(sys.env(keyToLookFor)) match {
      case Success(envVal) => formatValue[T](typeTag.tpe.typeSymbol.name.toString, envVal) match {
        case Right(value) => value
        case Left(ex) => throw ex
      }
      case Failure(ex) =>
        customError match {
          case Some(customEx) => throw customEx
          case None => throw ex
        }
    }
  }

  /** Looks for the value of the environment variable specified
    *
    * @param keyToLookFor The environment variables key to look for
    * @param defaultValue If the environment variable key is not found return this value instead
    * @param typeTag      The return type (no need to actually specify this implicit it will default to the supplied T)
    * @return T
    */
  def getEnvVar[T](keyToLookFor: String, defaultValue: T)(implicit typeTag: TypeTag[T]): T =
    getEnvVar[T](keyToLookFor) match {
      case Right(value) => value
      case Left(_) => defaultValue
    }

  /** Looks for the values of the environment variable specified split by a delimiter
    *
    * @param keyToLookFor The environment variables key to look for
    * @param delimiter    What to split up the values by
    * @param typeTag      The return type (no need to actually specify this implicit it will default to the supplied T)
    * @return Either[Throwable, List[T]
    */
  def getEnvVar[T](keyToLookFor: String, delimiter: Char)(implicit typeTag: TypeTag[T]): Either[Throwable, List[T]] =
    getEnvVar[T](keyToLookFor) match {
      case Right(value) => Right(value.toString.split(delimiter).map { m => m.asInstanceOf[T] }.toList)
      case Left(ex) => Left(ex)
    }

  /** Looks for the values of the environment variable specified split by a delimiter
    *
    * @param keyToLookFor  The environment variables key to look for
    * @param delimiter     What to split up the values by
    * @param defaultValues If the environment variable key is not found return this value instead
    * @param typeTag       The return type (no need to actually specify this implicit it will default to the supplied T)
    * @return List[T]
    */
  def getEnvVar[T](keyToLookFor: String, delimiter: Char, defaultValues: List[T])(implicit typeTag: TypeTag[T]): List[T] =
    getEnvVar[T](keyToLookFor, delimiter) match {
      case Right(value) => value
      case Left(_) => defaultValues
    }

}
