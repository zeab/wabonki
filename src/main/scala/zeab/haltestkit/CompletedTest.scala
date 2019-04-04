package zeab.haltestkit

case class CompletedTest(
                          testRunId:String,
                          testCaseId:String,
                          testName:String,
                          testResult:TestResult,
                          testDuration:Long
                        )
