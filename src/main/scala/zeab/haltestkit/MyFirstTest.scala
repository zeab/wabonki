package zeab.haltestkit

class MyFirstTest extends HalTestCase {

  Test("This is a test"){
    println("inside the test")
    TestResult("test case ", true)
  }

//  Test("This is another test"){
//    println("inside the test2")
//    false
//  }
//
//  Test("This is another test1"){
//    println("inside the test3")
//    true
//  }

}
