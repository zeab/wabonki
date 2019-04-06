package zeab.haltestkit

trait HalTest2 {

  def test(testName: String)(test: => (String, Boolean, Any)): Any ={
    test
  }

}
