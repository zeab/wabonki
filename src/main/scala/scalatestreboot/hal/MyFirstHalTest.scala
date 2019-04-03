package scalatestreboot.hal

class MyFirstHalTest extends HalTest {

  test("1 + 1 == 2"){
    val expectedValue: Int = 2
    val actualValue: Int = 1 + 1
    if(expectedValue == actualValue) (true, s"$expectedValue == $actualValue")
    else (false, s"$expectedValue != $actualValue")
  }

  test("2 + 2 == 4"){
    val expectedValue: Int = 4
    val actualValue: Int = 2 + 2
    if(expectedValue == actualValue) (true, s"$expectedValue == $actualValue")
    else (false, s"$expectedValue != $actualValue")
  }

}
