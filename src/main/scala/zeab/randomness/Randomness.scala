package zeab.randomness

//Imports
import java.util.concurrent.ThreadLocalRandom

trait Randomness {

  //Core Randomness
  def getRandomInt(max:Int, min:Int = 1): Int = ThreadLocalRandom.current.nextInt(min, max)
  def getRandomDouble(max:Double, min:Double = 1): Double = ThreadLocalRandom.current.nextDouble(min, max)
  def getRandomLong(max:Long, min:Long = 1): Long = ThreadLocalRandom.current.nextLong(min, max)

  //Just Letters
  def getRandomAlphaString(length: Int): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z')
    randomStringFromCharList(length, chars)
  }

  //Letters and numbers
  def getRandomAlphaNumericString(length: Int): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    randomStringFromCharList(length, chars)
  }

  //Numbers only
  def getRandomNumericString(length: Int): String = {
    val chars: Seq[Char] = '0' to '9'
    randomStringFromCharList(length, chars)
  }

  //User defined char-set
  def getRandomCustomString(chars: Seq[Char], length: Int): String = randomStringFromCharList(length, chars)

  //Helper method for randomAlpha
  private def randomStringFromCharList(length: Int, chars: Seq[Char]): String = {
    def worker(length: Int, chars: Seq[Char], randomString: String = ""): String = {
      if (length == 0) randomString
      else worker(length - 1, chars, s"$randomString${chars(getRandomInt(chars.length))}")
    }

    worker(length, chars)
  }

  //Randomly generates a month and day
  def getRandomMonthDay: (String, String) = {
    val month = getRandomInt(12)
    val day = getRandomInt(31)
    month match {
      case 2 =>
        if (day > 29) getRandomMonthDay
        else month.toString -> day.toString
      case 4 | 6 | 9 | 11 =>
        if (day > 30) getRandomMonthDay
        else month.toString -> day.toString
      case _ => month.toString -> day.toString
    }
  }

}

//So we can use it as is without having to extend it if we don't want
object Randomness extends Randomness
