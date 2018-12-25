package zeab.akkatools.udp.service

//Imports
import zeab.envgrok.EnvGrok

trait UdpServiceEnvGrok extends EnvGrok {

  val udpServiceHost: String = envGrok("UDP_SERVICE_HOST", "localhost")
  val udpServicePort: String = envGrok("UDP_SERVICE_PORT", "8125")

}
