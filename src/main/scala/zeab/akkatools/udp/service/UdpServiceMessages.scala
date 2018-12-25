package zeab.akkatools.udp.service

object UdpServiceMessages extends UdpServiceEnvGrok{

  case class StartUdpServer(
                             port: String = udpServicePort,
                             host: String = udpServiceHost
                           )

}
