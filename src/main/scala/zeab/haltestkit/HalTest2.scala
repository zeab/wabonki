package zeab.haltestkit

trait HalTest2 {

  def test(testName: String)(test: => (String, Boolean, Any)): () => (String, Boolean, Any) ={
    test _
  }

}
