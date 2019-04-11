package experimental.haltestkit

import akka.actor.{ActorRef, ActorSystem, Props}
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.haltestkit.halrunner.{HalRunner, HalRunnerLogger}
import zeab.sys.EnvironmentVariables

import scala.concurrent.ExecutionContext

object Main extends EnvironmentVariables {

  def main(args: Array[String]): Unit = {

    val dirForDiscovery: String = getEnvVar[String]("tests", "experimental.haltestkit.demotests")
    val tagsToRun: List[String] = getEnvVar("tags", ',', List.empty)

    //Akka
    implicit val system: ActorSystem = ActorSystem("HalTestKit", AkkaConfigBuilder.buildConfig())
    implicit val ec: ExecutionContext = system.dispatcher

    val logger: ActorRef = system.actorOf(Props[HalRunnerLogger], "HalLogger")

    system.actorOf(Props(classOf[HalRunner], dirForDiscovery, tagsToRun, logger), "HalRunner")

  }

}
