package zeab.akkatools.udp.service

//Imports
import zeab.akkatools.udp.service.UdpServiceMessages.StartUdpServer
//Java
import java.net.InetSocketAddress
//Akka
import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.event.{Logging, LoggingAdapter}
import akka.io.{IO, Udp}

class UdpServiceActor extends Actor {

  val actorLog: LoggingAdapter = Logging(context.system, this)

  implicit val actorSystem:ActorSystem = context.system

  def receive: Receive = disconnected

  def disconnected: Receive = {
    case m: StartUdpServer =>
      actorLog.info(s"Binding Udp server to ${m.host}:${m.port}")
      IO(Udp) ! Udp.Bind(self, new InetSocketAddress(m.host, m.port.toInt))
      context.become(connected(sender()))
    case Udp.Received(_, _) =>
      actorLog.error("Ucp is disconnected but the actor is still being sent messages")
  }

  def connected(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      actorLog.info(data.utf8String)
    case Udp.Unbind =>
      actorLog.info("Unbinding Udp server")
      socket ! Udp.Unbind
    case Udp.Unbound =>
      actorLog.info("Stopping Udp server")
      context.stop(self)
      context.become(disconnected)
  }

  /** Log Name on Start */
  override def preStart: Unit = {
    actorLog.debug(s"Starting ${this.getClass.getName}")
  }

  /** Log Name on Stop */
  override def postStop: Unit = {
    actorLog.debug(s"Stopping ${this.getClass.getName}")
  }

}
