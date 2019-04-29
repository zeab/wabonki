package zeab.akkatools.slack.slackwebhook

//Imports

//Akka
import akka.actor.{Actor, Cancellable}
import akka.event.{Logging, LoggingAdapter}
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class SlackWebHook2 extends Actor {

  implicit val executionContext: ExecutionContext = context.dispatcher

  val log: LoggingAdapter = Logging(context.system, this)

  def receive: Receive = empty

  def empty: Receive = {
    case msg: String =>
      log.info("Adding msg to queue and becoming not empty")
      val queueDrainer: Cancellable =
        context.system.scheduler.schedule(0.second, 1000.millisecond){
          log.info("FIRE!")
          self ! Fire
        }
      context.become(queued(List(msg), queueDrainer))
    case Fire => log.info("FIRE received but nothing to fire")
  }

  def queued(queue:List[String], queueDrainer: Cancellable): Receive = {
    case msg: String =>
      val updatedQueue: List[String] = List(msg) ++ queue
      log.info(s"Adding msg to queue - size: ${updatedQueue.size}")
      context.become(queued(updatedQueue, queueDrainer))
    case Fire =>
      val updatedQueue: List[String] = queue.drop(1)
      log.info(s"Removing msg to queue - size: ${updatedQueue.size}")


      if (updatedQueue.isEmpty){
        log.info("Becoming empty")
        queueDrainer.cancel
        context.become(empty)
      }
      else{
        val q = queue
        val t = updatedQueue
        context.become(queued(updatedQueue, queueDrainer))
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
