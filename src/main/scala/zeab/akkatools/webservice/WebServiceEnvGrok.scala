package zeab.akkatools.webservice

//Imports
import zeab.envgrok.EnvGrok

trait WebServiceEnvGrok extends EnvGrok {

  val webServiceHostKey: String = envGrok("WEB_SERVICE_HOST", "0.0.0.0")
  val webServicePortKey: String = envGrok("WEB_SERVICE_PORT", "8080")

}
