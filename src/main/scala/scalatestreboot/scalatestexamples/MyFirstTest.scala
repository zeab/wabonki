package scalatestreboot.scalatestexamples

//Imports
import org.scalatest.{FunSuite, ParallelTestExecution}

//with ParallelTestExecution
class MyFirstTest extends FunSuite {

  test("1 + 1 == 2"){
    println("1 + 1 == 2")
    assert(1 + 1 == 2)
  }

  test("0 + 0 == 0"){
    println("0 + 0 == 0")
    assert(0 + 0 == 0)
  }

  test("2 + 2 == 4"){
    println("2 + 2 == 4")
    assert(2 + 2 == 1)
  }

}
