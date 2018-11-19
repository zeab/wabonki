package zeab.envgrok

//Imports
import scala.util.{Failure, Success, Try}

trait EnvGrok {

  //Standard way to grok a list
  def envGrokDelimiter(keyToLookFor: String, delimiter: Char = ',', default: List[String] = List("intentional blank")): List[String] = {
    Try(sys.env(keyToLookFor)) match {
      case Success(envValue) => envValue.split(delimiter).toList
      case Failure(_) => default
    }
  }

  //Checks the env for []'s and replaces them... i should make the int's work as well i just haven't gotten there yet
  def envGrokFromEnv(keyToLookFor: String, default: String): String = {
    "\\[.*\\]".r.findFirstIn(envGrok(keyToLookFor, default)) match {
      case Some(foundValue) => envGrokFailOnFail(foundValue.replace('[', ' ').replace(']', ' ').trim)
      case None => default
    }
  }

  //A standard way to pull information from the environment
  def envGrok(keyToLookFor: String, default: String = "intentional blank"): String = {
    Try(sys.env(keyToLookFor)) match {
      case Success(envValue) => envValue
      case Failure(_) => default
    }
  }

  //Look for a key and fail the program if not found
  def envGrokFailOnFail(keyToLookFor: String): String = sys.env(keyToLookFor)

}
