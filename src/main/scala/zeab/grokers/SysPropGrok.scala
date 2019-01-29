package zeab.grokers

trait SysPropGrok extends EnvGrok {

  def getSysPropAsString(keyToLookFor: String, default: String = "intentional blank"): String =
    System.getProperty(keyToLookFor) match {
      case null => default
      case key: String => key
    }

  def setSysProp(key: String, value: String): String =
    System.setProperty(key, value) match {
      case null => "Is this ever anything but null?"
      case key: String => key
    }

  def setSysPropFromEnv(EnvKeyToLookFor: String, SySPropKeyToLookFor: String, value: String): String =
    System.setProperty(SySPropKeyToLookFor, getEnvAsString(EnvKeyToLookFor)) match {
      case null => "Is this ever anything but null?"
      case key: String => key
    }

}
