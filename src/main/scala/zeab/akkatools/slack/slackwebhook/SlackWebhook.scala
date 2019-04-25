package zeab.akkatools.slack.slackwebhook

//Imports
import zeab.akkatools.slack.slackseeds.slack.models.request.PostSlackRequestBody
import zeab.akkatools.slack.slackseeds.slack.seeds.Slack
import zeab.j2sjavanethttpclient.HttpClient
import zeab.seed.http.HttpSeed
//Akka
import akka.actor.{Actor, Cancellable}
import akka.event.{Logging, LoggingAdapter}
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class SlackWebhook(webhook: String) extends Actor {

  implicit val executionContext: ExecutionContext = context.dispatcher

  val log: LoggingAdapter = Logging(context.system, this)

  def receive: Receive = empty

  def empty: Receive = {
    case m: String =>
      val queueDrainer: Cancellable = context.system.scheduler.schedule(0.second, 1.second)(self ! Fire)
      context.become(queued(List(m), queueDrainer))
  }

  def queued(queue: List[String], queueDrainer: Cancellable): Receive = {
    case m: String => context.become(queued(queue ++ List(m), queueDrainer))
    case Fire =>
      queue.headOption match {
        case Some(msg) =>
          val httpSeed: HttpSeed = Slack.postSlackWebhook(webhook, PostSlackRequestBody(msg))
          HttpClient.invokeAsyncHttpClientResponse(httpSeed, isReturnBody = true) andThen {
            case Success(possibleResp) => possibleResp match {
              case Right(resp) => resp.responseStatusCode match {
                case 429 =>
                  //Stop the queue drainer
                  queueDrainer.cancel()
                  //TODO I think slack tells you when you can send again...so find that out and change it here
                  val newQueueDrainer: Cancellable = context.system.scheduler.schedule(20.second, 1.second)(self ! Fire)
                  context.become(queued(queue, newQueueDrainer))
                case 200 =>
                  log.debug("slack msg sent")
                  val newQueue: List[String] = queue.drop(1)
                  if (newQueue.isEmpty) {
                    queueDrainer.cancel
                    context.become(empty)
                  }
                  else context.become(queued(newQueue, queueDrainer))
                case _ =>
                  println()
                  //TODO Decide what to do on certain error conditions...
                  log.info(s"something happened http code was ${resp.responseStatusCode}")
                  val newQueue: List[String] = queue.drop(1)
                  if (newQueue.isEmpty) {
                    queueDrainer.cancel
                    context.become(empty)
                  }
                  else context.become(queued(newQueue, queueDrainer))
              }
              case Left(ex) => log.error(ex.toString)
            }
            case Failure(ex) => log.error(ex.toString)
          }
        case None =>
          queueDrainer.cancel
          context.become(empty)
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

  case object Fire

}