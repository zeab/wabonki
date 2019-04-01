package zeab.akkatools.udp.client

//Imports
import zeab.sys.EnvironmentVariables
//Akka
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool

trait UdpClientEnvGrok extends EnvironmentVariables {

  val udpClientHost: String = getEnvVar("UDP_CLIENT_HOST", "localhost")
  val udpClientPort: String = getEnvVar("UDP_CLIENT_PORT", "8125")
  val isUdpClientConnected: Boolean = getEnvVar("IS_UDP_CLIENT_CONNECTED", "true").toBoolean

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
