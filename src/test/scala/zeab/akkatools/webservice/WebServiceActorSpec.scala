package zeab.akkatools.webservice

//TODO Fix this so that it actually works... not quite sure why the timeout just does not seem to want to be set

//Imports
import zeab.akkatools.webservice.WebServiceMessages.{StartService, StopService}
//Akka
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
//Akka Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get}
import akka.http.scaladsl.server.Route
//Scala
import org.scalatest._

class WebServiceActorSpec extends TestKit(ActorSystem("WebServerSpec", ConfigFactory.parseString("""
  akka.loggers = ["akka.testkit.TestEventListener"]
  """)))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
  with Matchers {

  implicit val actorMaterilizer:ActorMaterializer = ActorMaterializer()

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A web server should start disconnected" must {

    val webServerName:String = "WebServiceActor"
    val webServerActor:ActorRef = system.actorOf(Props(classOf[WebServiceActor], actorMaterilizer), webServerName)
    val route:Route = { get { complete(StatusCodes.Accepted, "Get Works") } }

//    "connect successfully when asked" in {
//      EventFilter.info(message = s"Web Server $webServerName online /0:0:0:0:0:0:0:0:8080",
//        occurrences = 1).intercept {
//        webServerActor ! StartService(route, "8080", "localhost")
//      }
//    }


  }

}
