package zeab.randomness

import java.util.concurrent.ThreadLocalRandom

trait Randomness {

  def getRandomInt(max:Int, min:Int = 0): Int = ThreadLocalRandom.current.nextInt(min, max)
  def getRandomDouble(max:Double, min:Double = 0): Double = ThreadLocalRandom.current.nextDouble(min, max)
  def getRandomLong(max:Long, min:Long = 0): Long = ThreadLocalRandom.current.nextLong(min, max)
  
  //Gets a random non-zero number
  def getRandomNonZero(max:Int): Int ={
    def worker(randomNumber: Int): Int = {
      randomNumber match {
        case 0 => worker(scala.util.Random.nextInt(max))
        case _ => randomNumber
      }
    }

    worker(scala.util.Random.nextInt(max))
  }

  //Just Letters
  def getRandomAlpha(length: Int): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z')
    zeroLength(chars, length)
  }

  //Letters and numbers
  def getRandomAlphaNumeric(length: Int): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    zeroLength(chars, length)
  }

  //Numbers only
  def getRandomNumeric(length: Int): String = {
    val chars: Seq[Char] = '0' to '9'
    zeroLength(chars, length)
  }

  //User defined char-set
  def getRandomCustom(chars: Seq[Char], length: Int): String = randomStringFromCharList(length, chars)

  //Helper method for randomAlpha
  private def randomStringFromCharList(length: Int, chars: Seq[Char]): String = {
    def worker(length: Int, chars: Seq[Char], randomString: String = ""): String = {
      if (length == 0) randomString
      else worker(length - 1, chars, s"$randomString${chars(util.Random.nextInt(chars.length))}")
    }

    worker(length, chars)
  }

  //Returns an empty string if the length is 0
  private def zeroLength(chars: Seq[Char], length: Int): String ={
    length match {
      case 0 => ""
      case _ => randomStringFromCharList(length, chars)
    }
  }

  //Randomly generates a month and day
  def getRandomMonthDay: (String, String) = {
    val month = getRandomNonZero(12)
    val day = getRandomNonZero(31)
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
