package zeab.sys

trait SysProps extends EnvVars {

  //Gets the system prop or returns a default value
  def setSysProp(key: String, value: String): String = System.setProperty(key, value)

  //Set the system prop
  def setSysPropFromEnvVar(keyToLookFor: String, key: String): String = System.setProperty(key, getEnvVarAsString(keyToLookFor, None))

  //Gets the system prop or returns a default value
  def getSysPropAsString(keyToLookFor: String, default: String = "intentional blank"): String = {
    System.getProperty(keyToLookFor) match {
      case foundKey: String => foundKey
      case _ => default
    }
  }

  //Gets the system prop or throws an exception
  def getSysPropAsString(keyToLookFor: String, exception: Option[Exception]): String = {
    System.getProperty(keyToLookFor) match {
      case foundKey: String => foundKey
      case _ =>
        exception match {
          case Some(ex) => throw ex; ""
          case None => throw new Exception(s"Unable to find sys prop for key - '$keyToLookFor'"); ""
        }
    }
  }

  //Gets the system prop or returns a default value
  def getSysPropAsBoolean(keyToLookFor: String, default: Boolean = false): Boolean = {
    System.getProperty(keyToLookFor) match {
      case foundKey: String =>

        val hh = foundKey.toBoolean

        println()
        hh
      case _ => default
    }
  }

}

object SysProps extends SysProps