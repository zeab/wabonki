package zeab.akkatools.webservice

//Imports
import zeab.sys.EnvironmentVariables

trait WebServiceEnvGrok extends EnvironmentVariables {

  val webServiceHostKey: String = getEnvVar("WEB_SERVICE_HOST", "0.0.0.0")
  val webServicePortKey: String = getEnvVar("WEB_SERVICE_PORT", "8080")

}
