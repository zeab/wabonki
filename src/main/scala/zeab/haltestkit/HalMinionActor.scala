package zeab.haltestkit

//Imports
import java.util.UUID

import akka.actor.Actor

import scala.util.{Failure, Success, Try}

class HalMinionActor extends Actor {

  def receive: Receive = {
    case registeredTest: RegisteredTest =>
      val testStartTime: Long = System.currentTimeMillis
      val testResult: TestResult = Try(registeredTest.test.apply()) match {
        case Success(_) => TestResult(true, "assertion passed")
        case Failure(exception) => TestResult(false, exception.getMessage)
      }
      //Adjusts the test duration time that if its 0 it changes it to 1 because were using milliseconds as minimum duration span
      val duration: Long = System.currentTimeMillis() - testStartTime match {
        case 0 => 1
        case d: Long => d
      }
      //Its blank because this has no concept of test run id ... that is stored higher and gets added to these on its way out the door
      context.parent ! CompletedTest("", UUID.randomUUID().toString, registeredTest.testName, testResult, duration)
  }

}


