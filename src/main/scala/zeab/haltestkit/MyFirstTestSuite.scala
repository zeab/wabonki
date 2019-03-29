package zeab.haltestkit

class MyFirstTestSuite extends HalTestSuite {

  test("one plus one is two"){
    println("test 1")
    1 + 1 == 2
  }

  test("zero plus one is one"){
    println("test 3")
    0 + 1 == 2
  }

  test("one plus two is three"){
    println("test 2")
    1 + 2 == 3
  }

}
