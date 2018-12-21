package zeab.akkatools.udp.service

object UdpServiceMessages {

  case class StartUdpServer(
                             port: String = "8125",
                             host: String = "0.0.0.0"
                           )

}
