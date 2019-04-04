package zeab.haltestkit

class HalTest {

  //This is the eye sore of the entire project but I just cant figure out the way around it...
  var testList = List.empty[RegisteredTest]

  def test(testName: String)(test: => Any): Unit ={
    //Register this test with something....?
    testList = testList ++ List(RegisteredTest(testName, test _))
  }

  //How the frazzle muffle does it know where to go...
  //How can i register this test on its invocation into this world as a baby method...???


}
