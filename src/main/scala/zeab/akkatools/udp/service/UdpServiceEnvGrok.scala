package zeab.akkatools.udp.service

//Imports
import zeab.sys.EnvironmentVariables

trait UdpServiceEnvGrok extends EnvironmentVariables {

  val udpServiceHost: String = getEnvVar("UDP_SERVICE_HOST", "localhost")
  val udpServicePort: String = getEnvVar("UDP_SERVICE_PORT", "8125")

}
