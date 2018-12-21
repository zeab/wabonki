package zeab.akkatools.udp.client

//Imports
import zeab.akkatools.udp.client.UdpClientMessages.SendUdpDatagram
//Java
import java.net.InetSocketAddress
//Akka
import akka.actor.{Actor, ActorRef}
import akka.event.{Logging, LoggingAdapter}
import akka.io.{IO, UdpConnected}
import akka.util.ByteString

class UdpConnectedClientActor(host: String, port: String) extends Actor {

  val log: LoggingAdapter = Logging(context.system, this)

  val remote: InetSocketAddress = new InetSocketAddress(host, port.toInt)

  import context.system

  IO(UdpConnected) ! UdpConnected.Connect(self, remote)

  def receive: Receive = {
    case UdpConnected.Connected ⇒
      log.debug(s"connecting udp")
      context.become(ready(sender()))
  }

  def ready(connection: ActorRef): Receive = {
    case UdpConnected.Received(data) ⇒
      log.debug(s"received msg: $data")
    //What to do if I get a message from the connected service
    case msg: SendUdpDatagram ⇒
      log.debug(s"Sending Udp $host:$port msg: ${msg.msg}")
      connection ! UdpConnected.Send(ByteString(msg.msg))
    case UdpConnected.Disconnect ⇒
      log.debug(s"disconnecting udp")
      connection ! UdpConnected.Disconnect
    case UdpConnected.Disconnected ⇒
      log.debug(s"disconnected udp")
      context.stop(self)
  }

  /** Log Name on Start */
  override def preStart: Unit = {
    log.debug(s"Starting ${this.getClass.getName}")
  }

  /** Log Name on Stop */
  override def postStop: Unit = {
    log.debug(s"Stopping ${this.getClass.getName}")
  }

}
