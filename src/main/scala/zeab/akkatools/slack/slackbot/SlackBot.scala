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
                  case "{\"ok\":false,\"error\":\"missing_scope\",\"needed\":\"rtm:stream\",\"provided\":\"identify,bot,incoming-webhook,channels:read,team:read,channels:write,groups:write,links:read\"}" =>
                    //some other error... i think its when you dont have the right permissions on your app side
                    log.error("Try using Bot User OAuth Access Token")
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

      val ee = url
      println()
      // Future[Done] is the materialized value of Sink.foreach,
      // emitted when the stream completes
      val incoming: Sink[Message, Future[Done]] =
        Sink.foreach[Message] {
          case message: TextMessage.Strict =>
            println(message.text)
        }

      // send this as a message over the WebSocket
      val outgoing = Source.single(TextMessage("hello world!"))

      // flow to use (note: not re-usable!)
      val webSocketFlow = Http().webSocketClientFlow(WebSocketRequest("ws://localhost:8080/websocket"))

      // the materialized value is a tuple with
      // upgradeResponse is a Future[WebSocketUpgradeResponse] that
      // completes or fails when the connection succeeds or fails
      // and closed is a Future[Done] with the stream completion from the incoming sink
      val (upgradeResponse, closed) =
      outgoing
        .viaMat(webSocketFlow)(Keep.right) // keep the materialized Future[WebSocketUpgradeResponse]
        .toMat(incoming)(Keep.both) // also keep the Future[Done]
        .run()

      // just like a regular http request we can access response status which is available via upgrade.response.status
      // status code 101 (Switching Protocols) indicates that server support WebSockets
      val connected = upgradeResponse.flatMap { upgrade =>
        if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
          Future.successful(Done)
        } else {
          throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
        }
      }

      // in a real application you would not side effect here
      connected.onComplete(println)
      closed.foreach(_ => println("closed"))
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