package zeab.randomness

//Imports
import Randomness._
//Scala
import scala.util.matching.Regex
//ScalaTest
import org.scalatest.FunSuite

class RandomnessSpec extends FunSuite{

  test("Random Int should return a number between 5 and 10") {
    val randomInt: Int = getRandomInt(10, 5)
    assert(randomInt <= 10 && randomInt >= 5)
  }

  test("Random Int should return a number between 0 and 1") {
    val randomInt: Int = getRandomInt(1, 0)
    assert(randomInt <= 1 && randomInt >= 0)
  }

  test("Random Double should return a number between 5 and 10") {
    val randomDouble: Double = getRandomDouble(10, 5)
    assert(randomDouble <= 10 && randomDouble >= 5)
  }

  test("Random Double should return a number between 0 and 1") {
    val randomDouble: Double = getRandomDouble(1, 0)
    assert(randomDouble <= 1 && randomDouble >= 0)
  }

  test("Random Long should return a number between 5 and 10") {
    val randomLong: Long = getRandomLong(10, 5)
    assert(randomLong <= 10 && randomLong >= 5)
  }

  test("Random Long should return a number between 0 and 1") {
    val randomLong: Long = getRandomLong(1, 0)
    assert(randomLong <= 1 && randomLong >= 0)
  }

  test("Random Month/Day should return a valid month and day number combo") {
    val (month, day): (String, String) = getRandomMonthDay
    if (month.toInt == 2) assert(day.toInt <= 29)
    else if (month.toInt == 4 | month.toInt == 6 | month.toInt == 9 | month.toInt == 11) assert(day.toInt <= 30)
    else assert(day.toInt <= 31)
  }

  test("Random Numeric as String should return only numbers") {
    val randomNumeric: String = getRandomNumericString(5)
    val numberPattern: Regex = "[0-9]+".r
    numberPattern.findFirstMatchIn(randomNumeric) match {
      case Some(_) => assert(true)
      case None => assert(false)
    }
    assert(randomNumeric.length == 5)
  }

  test("Random Alpha as String should return only letters") {
    val randomAlpha: String = getRandomAlphaString(5)
    val numberPattern: Regex = "[a-zA-Z]+".r
    numberPattern.findFirstMatchIn(randomAlpha) match {
      case Some(_) => assert(true)
      case None => assert(false)
    }
    assert(randomAlpha.length == 5)
  }

  test("Random Alpha Numeric as String should return letters and numbers") {
    val randomAlphaNumeric: String = getRandomAlphaNumericString(5, Some(3))
    val numberPattern: Regex = "[0-9a-zA-Z]+".r
    numberPattern.findFirstMatchIn(randomAlphaNumeric) match {
      case Some(_) => assert(true)
      case None => assert(false)
    }
    assert(randomAlphaNumeric.length <= 5 && randomAlphaNumeric.length >= 3)
  }

  test("Random Custom as String should return whatever I have it") {
    val randomCustom: String = getRandomCustomString(Seq('k', ':'), 10, Some(3))
    val numberPattern: Regex = "[k,:]+".r
    numberPattern.findFirstMatchIn(randomCustom) match {
      case Some(_) => assert(true)
      case None => assert(false)
    }
    assert(randomCustom.length <= 10 && randomCustom.length >= 3)
  }

}