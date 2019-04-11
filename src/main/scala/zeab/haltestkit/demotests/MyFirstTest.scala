package zeab.haltestkit.demotests

//Imports
import akka.actor.ActorRef
import zeab.haltestkit.{HalTest, TestTag}

class MyFirstTest(testRegister:ActorRef) extends HalTest(testRegister){

  test("1 + 1 == 2", TestTag("bvt")){
    val expected: Int = 2
    val actual: Int = 1 + 1
    (expected == actual, expected)
  }

}
