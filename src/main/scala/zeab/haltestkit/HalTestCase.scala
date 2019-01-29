package zeab.haltestkit

trait HalTestCase {

  def Test(testCaseName:String)(testResult: => TestResult): TestResult = {
    TestResult(testCaseName, testResult.testResult)
  }

  def Test1(testResult: => TestResult)(testCaseName:String): TestResult = {
    TestResult(testCaseName, testResult.testResult)
  }

}

