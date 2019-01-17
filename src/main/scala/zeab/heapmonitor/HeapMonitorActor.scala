package zeab.heapmonitor

//Imports
import akka.actor.{Actor, ActorRef, Cancellable}
import zeab.logging.Logging
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
//Circe and Akka-Http plugin
import io.circe.generic.auto._
import io.circe.syntax._

class HeapMonitorActor(monitorInterval:Int, eventToEventStream: Boolean) extends Actor with ByteMath with Logging{

  implicit val ec: ExecutionContext = context.dispatcher

  val monitor:Cancellable = context.system.scheduler.schedule(0.second, monitorInterval.second){self ! Ping}

  def receive: Receive = {
    case Ping =>
      val runTime: Runtime = Runtime.getRuntime
      val heapSize: Long = byteToMegaByte(runTime.totalMemory)
      val emptyHeapSize: Long = byteToMegaByte(runTime.freeMemory)
      val usedHeapSize: Long = byteToMegaByte(runTime.totalMemory() - runTime.freeMemory)
      val maxHeapSize: Long = byteToMegaByte(runTime.maxMemory)
      val heapInfo: HeapLog = HeapLog(heapSize, maxHeapSize, emptyHeapSize, usedHeapSize)
      if (eventToEventStream) context.system.eventStream.publish(heapInfo)
      else logRaw.info(heapInfo.asJson.noSpaces)
  }

  case object Ping

  //Lifecycle Hooks
  /** Log Name on Start */
  override def preStart: Unit = {
  }

  /** Log Name on Stop */
  override def postStop: Unit = {
    monitor.cancel()
  }

}


//the point of this is to query the memory every x amount of seconds and