package zeab.akkatools.udp.client

//Imports
import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.event.{Logging, LoggingAdapter}
import akka.io.{IO, Udp}
import akka.util.ByteString
import zeab.akkatools.udp.client.UdpClientMessages.SendUdpDatagramToHost

class UdpUnconnectedClientActor extends Actor {

  val log: LoggingAdapter = Logging(context.system, this)

  implicit val actorSystem:ActorSystem = context.system

  IO(Udp) ! Udp.SimpleSender

  def receive: Receive = {
    case Udp.SimpleSenderReady =>
      log.debug("Udp.SimpleSenderReady received becoming ready")
      context.become(ready(sender))
  }

  def ready(send: ActorRef): Receive = {
    case m: SendUdpDatagramToHost =>
      val address: InetSocketAddress = new InetSocketAddress(m.host, m.port.toInt)
      log.debug(s"Sending Udp ${m.host}:${m.port} msg: ${m.msg}")
      send ! Udp.Send(ByteString(m.msg), address)
  }

}
