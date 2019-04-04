package zeab.haltestkit

//Imports
import akka.actor.{ActorSystem, Props}

import scala.concurrent.ExecutionContext

object HalRunner {

  def run(testList: List[String])(implicit system: ActorSystem, ec: ExecutionContext): Unit = {
    system.actorOf(Props[HalTestActor]) ! testList
  }

}
