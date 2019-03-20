package zeab.haltest

trait HalTestSuite {

  case class test(testName:String)(testResult: => Boolean){
    def execute = testResult
  }

}
