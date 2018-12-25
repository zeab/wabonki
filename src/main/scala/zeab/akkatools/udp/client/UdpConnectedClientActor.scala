package zeab.akkatools.udp.client

//Imports
import zeab.akkatools.udp.client.UdpClientMessages.SendUdpDatagram
//Java
import java.net.InetSocketAddress
//Akka
import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.event.{Logging, LoggingAdapter}
import akka.io.{IO, UdpConnected}
import akka.util.ByteString

//TODO Told to subscribe to the event bus as to pull down info with out having to always know where this udp client actors are

class UdpConnectedClientActor(host: String, port: String) extends Actor {

  val actorLog: LoggingAdapter = Logging(context.system, this)

  val remote: InetSocketAddress = new InetSocketAddress(host, port.toInt)

  implicit val actorSystem:ActorSystem = context.system

  def receive: Receive = {
    case UdpConnected.Connected =>
      actorLog.debug(s"connecting udp")
      context.become(ready(sender()))
  }

  def ready(connection: ActorRef): Receive = {
    case UdpConnected.Received(data) =>
      actorLog.debug(s"received msg: $data")
    //What to do if I get a message from the connected service
    case msg: SendUdpDatagram =>
      actorLog.debug(s"Sending Udp $host:$port msg: ${msg.msg}")
      connection ! UdpConnected.Send(ByteString(msg.msg))
    case UdpConnected.Disconnect =>
      actorLog.debug(s"disconnecting udp")
      connection ! UdpConnected.Disconnect
    case UdpConnected.Disconnected =>
      actorLog.debug(s"disconnected udp")
      context.stop(self)
  }

  /** Log Name on Start */
  override def preStart: Unit = {
    IO(UdpConnected) ! UdpConnected.Connect(self, remote)
    actorLog.debug(s"Starting ${this.getClass.getName}")
  }

  /** Log Name on Stop */
  override def postStop: Unit = {
    actorLog.debug(s"Stopping ${this.getClass.getName}")
  }

}
