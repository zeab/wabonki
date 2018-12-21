package zeab.akkatools.udp.service

//Imports
import zeab.akkatools.udp.service.UdpServiceMessages.StartUdpServer
//Java
import java.net.InetSocketAddress
//Akka
import akka.actor.{Actor, ActorRef}
import akka.event.{Logging, LoggingAdapter}
import akka.io.{IO, Udp}

class UdpServiceActor extends Actor {

  val log: LoggingAdapter = Logging(context.system, this)

  def receive: Receive = disconnected

  def disconnected: Receive = {
    case m: StartUdpServer =>
      import context.system
      log.info(s"Binding Udp server to ${m.host}:${m.port}")
      IO(Udp) ! Udp.Bind(self, new InetSocketAddress(m.host, m.port.toInt))
      context.become(connected(sender()))
    case Udp.Received(_, _) =>
      log.error("Ucp is disconnected but the actor is still being sent messages")
  }

  def connected(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      log.info(data.utf8String)
    case Udp.Unbind =>
      log.info("Unbinding Udp server")
      socket ! Udp.Unbind
    case Udp.Unbound =>
      log.info("Stopping Udp server")
      context.stop(self)
      context.become(disconnected)
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
