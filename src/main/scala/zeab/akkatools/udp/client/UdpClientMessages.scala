package zeab.akkatools.udp.client

object UdpClientMessages {

  sealed trait UdpClientMessage

  case class SendUdpDatagram(msg: String) extends UdpClientMessage

  case class SendUdpDatagramToHost(msg: String, host: String = "localhost", port: String = "8125") extends UdpClientMessage

}
