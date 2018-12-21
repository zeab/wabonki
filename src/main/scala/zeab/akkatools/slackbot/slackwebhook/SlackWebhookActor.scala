package zeab.akkatools.slackbot.slackwebhook

//TODO While perfectly functional... this was written at a time before i quite knew streams... and I feel this could be better much better

//It also has built in retry and back off management ... which is why I say it could be well smoother is a better word

//Imports
import zeab.akkatools.slackbot.slackseeds.slack.models.request.{PostSlackClump, PostSlackRequestBody}
import zeab.akkatools.slackbot.slackseeds.slack.seeds.Slack
import zeab.akkatools.slackbot.slackwebhook.SlackWebhookMessages.PostSlackWebhook
import zeab.j2sjavanethttpclient.HttpClient
import zeab.seed.http.httpclient.{HttpClientError, HttpClientResponse}
//Akka
import akka.actor.{Actor, ActorLogging, ActorSystem, Cancellable}
import akka.stream.ActorMaterializer
import akka.pattern.pipe
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
//Circe
import io.circe.generic.auto._
import io.circe.parser.decode

class SlackWebhookActor extends Actor with ActorLogging {

  implicit val actorSystem: ActorSystem = context.system
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = context.dispatcher

  def receive: Receive = empty

  def empty: Receive = {
    case m: PostSlackWebhook =>
      log.debug("Queing a slack webhook message")
      val capturedSelf = self
      val drainQueue = actorSystem.scheduler.schedule(0.second, 1.second) {
        capturedSelf ! FireMessage
      }
      context.become(queue(List(m), drainQueue))
    case FireMessage =>
    //do nothing since i
  }

  def queue(q: List[PostSlackWebhook], drainQueue: Cancellable): Receive = {
    case m: PostSlackWebhook =>
      log.debug("Queing a slack webhook message")
      context.become(queue(q ++ List(m), drainQueue))
    case FireMessage =>
      q.headOption match {
        case Some(m) =>
          log.debug("Firing Message at slack")
          val slack = new Slack
          val httpSeed = slack.postSlackWebhook(PostSlackClump(PostSlackRequestBody(m.msg), m.webhook))
          HttpClient.invokeAsyncHttpClientResponse(httpSeed.url, httpSeed.method, httpSeed.body, httpSeed.headers, httpSeed.metaData) pipeTo self
          context.become(queue(q.drop(1), drainQueue))
        case None => context.become(empty)
      }
    case m: Either[HttpClientError, HttpClientResponse] =>
      m match {
        case Right(resp) =>
          //TODO check if there is a response header i can grok for the slow down time i need
          if (resp.responseStatusCode == 429) {
            //Decode the request body back into the post thingy
            //use that to make the slack call back
            val decodedRespBody = decode[PostSlackRequestBody](resp.requestBody)
            decodedRespBody match {
              case Right(body) =>
                drainQueue.cancel()
                val capturedSelf = self
                val newDrainQueue = actorSystem.scheduler.schedule(20.second, 1.second) (capturedSelf ! FireMessage)
                context.become(queue(q ++ List(PostSlackWebhook(body.text, resp.requestUrl.split("/").lastOption.getOrElse(""))), newDrainQueue))
              case Left(ex) => log.warning(ex.toString)
            }
          }
          else log.debug("Disregarding slack response")
        case Left(err) => log.error("Unable to make call to slack webhook")
      }
  }

  /** Log Name on Start */
  override def preStart: Unit = {
    log.debug(s"Starting ${self.path.name}")
  }

  /** Log Name on Stop */
  override def postStop: Unit = {
    log.debug(s"Stopping ${self.path.name}")
  }

  case object FireMessage

}
