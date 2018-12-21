package zeab.akkatools.udp.client

//Imports
import zeab.envgrok.EnvGrok
//Akka
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool


trait UdpClientEnvGrok extends EnvGrok {

  val udpClientHost: String = envGrok("UDP_CLIENT_HOST", "localhost")
  val udpClientPort: String = envGrok("UDP_CLIENT_PORT", "8125")
  val isUdpClientConnected: Boolean = envGrok("IS_UDP_CLIENT_CONNECTED", "true").toBoolean

  //Udp Client
  def createUdpClient(poolSize: Int = 5, isConnected: Boolean = isUdpClientConnected)(implicit actorSystem: ActorSystem): ActorRef =
    if (isConnected) {
      actorSystem.actorOf(
        RoundRobinPool(poolSize).
          props(Props(
            classOf[UdpConnectedClientActor],
            udpClientHost,
            udpClientPort
          )
          ), "UdpClient"
      )
    }
    else {
      actorSystem.actorOf(
        RoundRobinPool(poolSize).
          props(
            Props(
              classOf[UdpUnconnectedClientActor]
            )
          ),
        "UdpClient"
      )
    }

}
