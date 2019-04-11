package experimental.haltestkit.demotests

//Imports
import akka.actor.ActorRef
import zeab.haltestkit.{HalTest, TestTag}

class MyOtherTest(testRegister:ActorRef) extends HalTest(testRegister){

  test("2 + 2 == 4", TestTag("moose"), TestTag("bvt")){
    val expected: Int = 4
    val actual: Int = 2 + 2
    Thread.sleep(1000)
    expected == actual
  }

  test("3 + 3 == 6", TestTag("cat")){
    val expected: Int = 6
    val actual: Int = 3 + 3
    expected == actual
  }

}
