package zeab.sys

trait SysProps extends EnvVars {

  //Gets the env var or returns a default value
  def setSysProp(key: String, value: String): String = {
    System.setProperty(key, value)
  }

  def setSysPropFromEnvVar(keyToLookFor: String, key: String): String = {
    System.setProperty(key, getEnvVarAsString(keyToLookFor, None))
  }

  //Gets the env var or returns a default value
  def getSysPropAsString(keyToLookFor: String, default: String = "intentional blank"): String = {
    System.getProperty(keyToLookFor) match {
      case foundKey: String => foundKey
      case _ => default
    }
  }

  //Gets the env var or returns a default value
  def getSysPropAsBoolean(keyToLookFor: String, default: Boolean = false): Boolean = {
    System.getProperty(keyToLookFor) match {
      case foundKey: String => foundKey.toBoolean
      case _ => default
    }
  }

}
