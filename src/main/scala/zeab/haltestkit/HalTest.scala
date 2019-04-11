package zeab.haltestkit

//Imports
import akka.actor.ActorRef
import zeab.haltestkit.teststatuses.RegisteredTest

class HalTest(testRegister: ActorRef) {

  //Counts the total number of tests that attempt to register themselves so we know how many to expect
  var testCount: Int = 0

  def test(testName: String, testTags: TestTag*)(test: => Any): Unit = {
    testCount = testCount + 1
    testRegister ! RegisteredTest(testName, testTags.toList, test _)
  }

}
