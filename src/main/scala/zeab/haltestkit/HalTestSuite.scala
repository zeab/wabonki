package zeab.haltestkit

class HalTestSuite {

  def test(testName:String)(testResult: => Boolean): Unit ={
    testResult
  }

}
