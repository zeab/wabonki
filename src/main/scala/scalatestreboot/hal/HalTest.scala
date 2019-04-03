package scalatestreboot.hal

class HalTest {

  var testList = List.empty[RegisterTest]

  def test(testName: String)(test: => Any): Unit ={
    //Register this test with something....?
    testList = testList ++ List(RegisterTest(testName, test _))
  }

  //How the frazzle muffle does it know where to go...
  //How can i register this test on its invocation into this world as a baby method...???


}
