package zeab.haltestkit

case class RegisteredTest(testName:String, test: () => Any)
