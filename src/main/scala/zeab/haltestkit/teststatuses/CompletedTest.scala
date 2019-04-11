package zeab.haltestkit.teststatuses

case class CompletedTest(
                          testRunId: String,
                          testCaseId: String,
                          testName: String,
                          testResult: Any,
                          testDuration: Long
                        )
