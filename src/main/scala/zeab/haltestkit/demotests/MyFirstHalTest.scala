package zeab.haltestkit.demotests

import zeab.haltestkit.HalTest

class MyFirstHalTest extends HalTest {

  test("1 + 1 == 2"){
    println("I ran")
    val expectedValue: Int = 2
    val actualValue: Int = 1 + 1
    assert(expectedValue == actualValue, s"$expectedValue != $actualValue")
  }

  test("2 + 2 == 4"){
    val expectedValue: Int = 4
    val actualValue: Int = 2 + 2
    Thread.sleep(4000)
    assert(expectedValue == actualValue, s"$expectedValue != $actualValue")
  }

}
