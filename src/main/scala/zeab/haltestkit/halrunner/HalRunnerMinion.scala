package zeab.haltestkit.halrunner

//Imports
import zeab.haltestkit.teststatuses.{CompletedTest, RegisteredTest}
//Akka
import akka.actor.ActorRef
//Java
import java.util.UUID
//Akka
import akka.actor.Actor
//Scala
import scala.concurrent.{ExecutionContext, Future}

//TODO Make this safer

class HalRunnerMinion(parent:ActorRef, testRunId: String) extends Actor {

  implicit val ec: ExecutionContext = context.system.dispatcher

  def receive: Receive = {
    case m: RegisteredTest =>
      Future{
        val testStart: Long = System.currentTimeMillis()
        val testResult: Any = m.test.apply()
        val testEnd: Long = System.currentTimeMillis() - testStart
        parent ! CompletedTest(testRunId, UUID.randomUUID.toString, m.testName, testResult, testEnd)
      }
  }

}
