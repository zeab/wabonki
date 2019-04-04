package zeab.haltestkit.demotests

import zeab.haltestkit.HalTest

class MyOtherHalTest extends HalTest {

  test("3 + 1 == 4"){
    val expectedValue: Int = 4
    val actualValue: Int = 3 + 1
    Thread.sleep(2000)
    assert(expectedValue == actualValue, s"$expectedValue != $actualValue")
  }

  test("1 + 2 == 3"){
    val expectedValue: Int = 3
    val actualValue: Int = 1 + 2
    assert(expectedValue == actualValue, s"$expectedValue != $actualValue")
  }

}
