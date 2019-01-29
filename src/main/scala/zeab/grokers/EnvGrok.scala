package zeab.grokers

//Imports
import scala.util.{Failure, Success, Try}

trait EnvGrok {

  def getEnvAsBoolean(keyToLookFor: String, default: Boolean = false): Boolean =
    System.getenv(keyToLookFor) match {
      case null => default
      case key: String =>
        Try(key.toBoolean) match {
          case Success(k) => k
          case Failure(_) => default
        }
    }

  def getEnvAsInt(keyToLookFor: String, default: Int = 0): Int =
    System.getenv(keyToLookFor) match {
      case null => default
      case key: String =>
        Try(key.toInt) match {
          case Success(k) => k
          case Failure(_) => default
        }
    }

  def getEnvAsLong(keyToLookFor: String, default: Long = 0): Long =
    System.getenv(keyToLookFor) match {
      case null => default
      case key: String =>
        Try(key.toLong) match {
          case Success(k) => k
          case Failure(_) => default
        }
    }

  def getEnvAsDouble(keyToLookFor: String, default: Double = 0): Double =
    System.getenv(keyToLookFor) match {
      case null => default
      case key: String =>
        Try(key.toDouble) match {
          case Success(k) => k
          case Failure(_) => default
        }
    }

  //TODO Update these a little bit and make some variations
  //Checks the env for []'s and replaces them... i should make the int's work as well i just haven't gotten there yet
  def getEnvFromEnvCrashOnFail(keyToLookFor: String, default: String): String =
    "\\[.*\\]".r.findFirstIn(getEnvAsString(keyToLookFor, default)) match {
      case Some(foundValue) => getEnvCrashOnFail(foundValue.replace('[', ' ').replace(']', ' ').trim)
      case None => default
    }

  def getEnvAsString(keyToLookFor: String, default: String = "intentional blank"): String =
    System.getenv(keyToLookFor) match {
      case null => default
      case key: String => key
    }

  //TODO Is exception the best choice here?
  def getEnvCrashOnFail(keyToLookFor: String, exceptionToThrow: Option[Exception] = None): String =
    System.getenv(keyToLookFor) match {
      case null => exceptionToThrow match {
        case Some(ex) => throw ex
        case None => throw new Exception(s"Unable to find the environment variable - $keyToLookFor")
      }
      case key: String => key
    }

  //TODO Make a few variations of these
  def getEnvAsDelimiter(keyToLookFor: String, delimiter: Char = ',', default: List[String] = List("intentional blank")): List[String] =
    System.getenv(keyToLookFor) match {
      case null => default
      case key: String => key.split(delimiter).toList
    }

}
