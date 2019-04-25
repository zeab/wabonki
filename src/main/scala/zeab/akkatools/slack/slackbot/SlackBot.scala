package zeab.akkatools.slack.slackbot

//Imports
import zeab.akkatools.slack.slackseeds.slackrtm.models.response.GetRTMConnection200
import zeab.akkatools.slack.slackseeds.slackrtm.seeds.SlackRTM
import zeab.j2sjavanethttpclient.HttpClient
import zeab.seed.http.HttpSeed
//Akka
import akka.Done
import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model.ws.WebSocketUpgradeResponse
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
//Scala
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
//Circe
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

class SlackBot(bearer:String)(implicit materializer: ActorMaterializer) extends Actor {

  implicit val system: ActorSystem = context.system
  implicit val executionContext: ExecutionContext = context.dispatcher

  val log: LoggingAdapter = Logging(context.system, this)

  def receive: Receive = disconnected

  def disconnected: Receive = {
    case Init =>
      val httpSeed: HttpSeed = SlackRTM.getRTMConnectionHttpSeed(bearer)
      HttpClient.invokeAsyncHttpClientResponse(httpSeed, isReturnBody = true) andThen {
        case Success(possibleResp) => possibleResp match {
          case Right(resp) =>
            resp.responseStatusCode match {
              case 200 =>
                //Since apparently they give you back a 200 and the error is in the message for auth
                resp.responseBody match {
                  case "{\"ok\":false,\"error\":\"not_authed\"}" =>
                    //TODO Decide if this is really what we want to do in all situations...
                    log.error("You auth is incorrect")
                    self ! PoisonPill
                    system.terminate
                  case _ =>
                    decode[GetRTMConnection200](resp.responseBody) match {
                      case Right(body) =>
                        context.become(connecting(body.url))
                        self ! Connect
                      case Left(ex) =>
                        //TODO Decide if this is really what we want to do in all situations...
                        log.error(ex.toString)
                        self ! PoisonPill
                        system.terminate
                    }
                }
              case _ =>
                //TODO Decide if this is really what we want to do in all situations...
                log.error(resp.responseBody)
                self ! PoisonPill
                system.terminate
            }
          case Left(ex) =>
            //TODO Decide if this is really what we want to do in all situations...
            log.error(ex.toString)
            self ! PoisonPill
            system.terminate
        }
        case Failure(ex) =>
          log.error(ex.toString)
          self ! PoisonPill
          system.terminate
      }
  }

  def connecting(url:String): Receive = {
    case Connect =>
      val req: WebSocketRequest = WebSocketRequest(uri = url)
      val webSocketFlow: Flow[Message, Message, Future[WebSocketUpgradeResponse]] = Http().webSocketClientFlow(req)
      val messageSource: Source[Message, ActorRef] =
        Source.actorRef[TextMessage.Strict](bufferSize = 10, OverflowStrategy.fail)
      val messageSink: Sink[Message, Future[Done]] = {
        Sink.foreach {message => self ! message}}
      val ((ws, upgradeResponse), closed): ((ActorRef, Future[WebSocketUpgradeResponse]), Future[Done]) =
        messageSource
          .viaMat(webSocketFlow)(Keep.both)
          .toMat(messageSink)(Keep.both)
          .run()
      closed.onComplete{_ =>
        log.info("we have disconnected on the web socket...")
        context.become(disconnected)
      }
      upgradeResponse.flatMap { upgrade =>
        if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
          log.info("Ws connection opened")
          context.become(connected(ws))
          Future.successful(Done)
        }
        else {
          log.error("Ws connection failed stopping program")
          //TODO Decide if this is actually the behavior we want... its never actually happened yet... so I haven't had to worry
          throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
        }
      }
  }

  def connected(webSocket:ActorRef): Receive = {
    case m: Message =>
      webSocket ! m
  }

  case object Init

  case object Connect

  /** Log Name on Start */
  override def preStart: Unit = {
    log.debug(s"Starting ${self.path.name}")
    self ! Init
  }

  /** Log Name on Stop */
  override def postStop: Unit = {
    log.debug(s"Stopping ${self.path.name}")
  }

}


//first i need make a regular http connection and get the token
//then open the web socket connection
//then have the listener and sender to deal with incoming and outgoing messages

//This is the thing that both catches and sends messages... so this will be a parents of 2 smaller actors