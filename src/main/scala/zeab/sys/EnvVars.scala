package zeab.sys

trait EnvVars {

  //Gets the env var or returns a default value
  def getEnvVarAsString(keyToLookFor: String, default: String = "intentional blank"): String = {
    System.getenv(keyToLookFor) match {
      case foundKey: String => foundKey
      case _ => default
    }
  }

  //Gets the env var or throws an exception
  def getEnvVarAsString(keyToLookFor: String, exception: Option[Exception]): String = {
    System.getenv(keyToLookFor) match {
      case foundKey: String => foundKey
      case _ =>
        exception match {
          case Some(ex) => throw ex; ""
          case None => throw new Exception(s"Unable to find env var for key - '$keyToLookFor'"); ""
        }
    }
  }

  //Gets the env var or returns a default value
  def getEnvVarAsBoolean(keyToLookFor: String, default: Boolean = false): Boolean = {
    System.getenv(keyToLookFor) match {
      case foundKey: String => foundKey.toBoolean
      case _ => default
    }
  }

  //Gets the env var or returns a default value
  def getEnvVarAsLong(keyToLookFor: String, default: Long = 0): Long = {
    System.getenv(keyToLookFor) match {
      case foundKey: String => foundKey.toLong
      case _ => default
    }
  }

  //Gets the env var or returns a default value
  def getEnvVarAsDouble(keyToLookFor: String, default: Double = 0): Double = {
    System.getenv(keyToLookFor) match {
      case foundKey: String => foundKey.toDouble
      case _ => default
    }
  }

  //Gets the env var or returns a default value
  def getEnvVarAsInt(keyToLookFor: String, default: Int = 0): Int = {
    System.getenv(keyToLookFor) match {
      case foundKey: String => foundKey.toInt
      case _ => default
    }
  }

}
