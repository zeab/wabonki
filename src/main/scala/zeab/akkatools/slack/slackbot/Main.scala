package zeab.akkatools.slack.slackbot

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Main {

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system: ActorSystem = ActorSystem("SlackBot", AkkaConfigBuilder.buildConfig())
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = system.dispatcher

    val bearer: String = ""

    val slackBot: ActorRef = system.actorOf(Props(classOf[SlackBot], bearer, materializer), "SlackBot")

    system.scheduler.schedule(0.second, 3.second){
      slackBot ! UUID.randomUUID.toString
    }

  }

}
