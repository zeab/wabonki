package zeab.haltestkit

//Imports
import akka.actor.ActorSystem
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder

import scala.concurrent.ExecutionContext

object Main {

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system: ActorSystem = ActorSystem("HalTestKit", AkkaConfigBuilder.buildConfig())
    implicit val ec: ExecutionContext = system.dispatcher

    val testList = List(
      "zeab.haltestkit.demotests.MyFirstHalTest",
      "zeab.haltestkit.demotests.MyOtherHalTest"
    )
    HalRunner.run(testList)

  }

}
