package zeab.haltestkit.halrunner

//TODO Actually do logging correctly and import the right lib that already exists

//Imports
import akka.actor.Actor
import zeab.haltestkit.teststatuses.CompletedTest

class HalRunnerLogger extends Actor {

  def receive: Receive = {
    case m: CompletedTest =>
      println(m)
    case m: String =>
      println(m)
  }

}
