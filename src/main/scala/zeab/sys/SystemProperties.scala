package zeab.sys

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}

/**
  * A collection of useful functions for getting and setting system properties in safe ways
  *
  * @author Kevin Kosnik-Downs (Zeab)
  * @since 2.12
  */
trait SystemProperties extends EnvironmentVariables {

  /** Looks for the value of the system properties key specified
    *
    * @param keyToLookFor The key to look for
    * @param typeTag      The return type (no need to actually specify this implicit it will default to the supplied T)
    * @return Either[Throwable, T]
    */
  def getSysProp[T](keyToLookFor: String)(implicit typeTag: TypeTag[T]): Either[Throwable, T] =
    Try(System.getProperty(keyToLookFor)) match {
      case Success(sysProp) => formatValue[T](typeTag.tpe.typeSymbol.name.toString, sysProp)
      case Failure(ex) => Left(ex)
    }

  /** Looks for the value of the system properties key specified
    *
    * @param keyToLookFor The key to look for
    * @param defaultValue What to return if the key is not found
    * @param typeTag      The return type (no need to actually specify this implicit it will default to the supplied T)
    * @return T
    */
  def getSysProp[T](keyToLookFor: String, defaultValue: T)(implicit typeTag: TypeTag[T]): T =
    getSysProp[T](keyToLookFor) match {
      case Right(value) => value
      case Left(_) => defaultValue
    }

  /** Sets the value of the system property key specified
    *
    * @param sysPropKey   The key to set
    * @param sysPropValue The value
    * @return String
    */
  def setSysProp(sysPropKey: String, sysPropValue: String): String = System.setProperty(sysPropKey, sysPropValue)

  /** Sets the value of the system property key from an environment variable specified
    *
    * @param envVarKey          The environment variables key
    * @param envVarDefaultValue What to return if the key is not found
    * @param sysPropKey         The system property key
    * @return String
    */
  def setSysProp(envVarKey: String, envVarDefaultValue: String, sysPropKey: String): String =
    getEnvVar[String](envVarKey) match {
      case Right(value) => System.setProperty(sysPropKey, value)
      case Left(_) => envVarDefaultValue
    }

}
