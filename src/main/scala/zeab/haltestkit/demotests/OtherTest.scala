package zeab.haltestkit.demotests

//Imports
import zeab.haltestkit.HalTest2

class OtherTest extends HalTest2{

  test("1 + 1 == 2") {
    val expectedValue: Int = 2
    val actualValue: Int = 1 + 1
    ("llama", expectedValue == actualValue, "end")
  }

  test("0 + 0 == 0") {
    val expectedValue: Int = 2
    val actualValue: Int = 1 + 3
    ("other", expectedValue == actualValue, 9)
  }

}
