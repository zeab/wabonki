//package zeab.akkatools.threadmonitor
//
////Imports
//import scala.concurrent.duration._
//import scala.concurrent.{ExecutionContext, Future}
////Akka
//import akka.actor.{Actor, ActorSystem, Cancellable}
//import akka.event.{Logging, LoggingAdapter}
//import akka.pattern.pipe
//
////Pass an implicit execution context to be monitored
//
//class ImplicitThreadMonitorActor(implicit executionContext: ExecutionContext) extends Actor {
//
//  implicit val actorSystem: ActorSystem = context.system
//  //TODO add a logging config just for the responses
//  val actorLog: LoggingAdapter = Logging(context.system, this)
//
//  def receive: Receive = inactive
//
//  def inactive: Receive = {
//    case Start =>
//      val monitor = actorSystem.scheduler.schedule(0.second, 5.second) {
//        val t0 = System.currentTimeMillis()
//        Future(System.currentTimeMillis() - t0) pipeTo self
//      }
//      context.become(active(monitor))
//  }
//
//  def active(monitor: Cancellable): Receive = {
//    case m: Long =>
//      if (m > 100) actorLog.warning("Thread is under duress")
//      else if (m > 500) actorLog.error("Thread is under heavy duress")
//  }
//
//  //Lifecycle Hooks
//  /** Log Name on Start */
//  override def preStart: Unit = {
//    actorLog.debug(s"Starting ${this.getClass.getName}")
//  }
//
//  /** Log Name on Stop */
//  override def postStop: Unit = {
//    actorLog.debug(s"Stopping ${this.getClass.getName}")
//  }
//
//  case object Start
//
//}
