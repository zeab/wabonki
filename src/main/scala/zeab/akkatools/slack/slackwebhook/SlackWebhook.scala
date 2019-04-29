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

class SlackWebhook(webhook: String, fireIntervalInMs: Int) extends Actor {

  implicit val executionContext: ExecutionContext = context.dispatcher

  val log: LoggingAdapter = Logging(context.system, this)

  def receive: Receive = empty

  def empty: Receive = {
    case m: String =>
      log.info("slack queue is empty")
      val queueDrainer: Cancellable =
        context.system.scheduler.schedule(0.second, fireIntervalInMs.millisecond){
          log.info("FIRE!")
          self ! Fire
        }
      context.become(queued(List(m), queueDrainer))
    case Fire => log.info("nothing to Fire")
  }

  def queued(queue: List[String], queueDrainer: Cancellable): Receive = {
    case m: String =>
      println(queue.size)
      println(queueDrainer)
//      if (queueDrainer == null) context.system.scheduler.schedule(0.second, fireIntervalInMs.millisecond){
//        log.info("FIRE!")
//        self ! Fire
//      }
      log.info("adding message to slack queue")
      context.become(queued(queue ++ List(m), queueDrainer))
    case Fire =>
      log.info("firing off slack message")
      queue.headOption match {
        case Some(msg) =>
          val httpSeed: HttpSeed = Slack.postSlackWebhook(webhook, PostSlackRequestBody(msg))
          HttpClient.invokeAsyncHttpClientResponse(httpSeed, isReturnBody = true) andThen {
            case Success(possibleResp) => possibleResp match {
              case Right(resp) =>
                println(resp.responseStatusCode)
                println(resp.responseBody)
                resp.responseStatusCode match {
                case 429 =>
                  //Stop the queue drainer
                  queueDrainer.cancel()
                  //TODO I think slack tells you when you can send again...so find that out and change it here
                  val newQueueDrainer: Cancellable = context.system.scheduler.schedule(20.second, fireIntervalInMs.millisecond){
                    log.info("FIRE!")
                    self ! Fire
                  }
                  context.become(queued(queue, newQueueDrainer))
                case 200 =>
                  //TODO Change this to filter it out by the message rather than dropping 1
                  val newQueue: List[String] = queue.drop(1)
                  resp.responseHeaders.find(header => header._1 == "Content-Type" & header._2.contains("text/html")) match {
                    case Some(_) => log.info("dropping message as undeliverable")
                    case None => log.info("message successfully sent")
                  }
                  if (newQueue.isEmpty) {
                    log.info("becoming empty")
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
          log.info("we got a none...")
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