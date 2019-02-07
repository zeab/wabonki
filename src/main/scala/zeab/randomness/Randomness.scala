package zeab.randomness

//Imports
import java.util.concurrent.ThreadLocalRandom

trait Randomness {

  //Core Randomness
  def getRandomInt(max: Int = Int.MaxValue, min: Int = 1): Int = ThreadLocalRandom.current.nextInt(min, max)

  def getRandomDouble(max: Double = Double.MaxValue, min: Double = 1): Double = ThreadLocalRandom.current.nextDouble(min, max)

  def getRandomLong(max: Long = Long.MaxValue, min: Long = 1): Long = ThreadLocalRandom.current.nextLong(min, max)

  //Just Letters
  def getRandomAlphaString(maxLength: Int, minLength: Option[Int] = None): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z')
    randomStringFromCharList(maxLength, minLength, chars)
  }

  //Letters and numbers
  def getRandomAlphaNumericString(maxLength: Int, minLength: Option[Int] = None): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    randomStringFromCharList(maxLength, minLength, chars)
  }

  //Numbers only
  def getRandomNumericString(maxLength: Int, minLength: Option[Int] = None): String = {
    val chars: Seq[Char] = '0' to '9'
    randomStringFromCharList(maxLength, minLength, chars)
  }

  //User defined char-set
  def getRandomCustomString(chars: Seq[Char], maxLength: Int, minLength: Option[Int] = None): String = randomStringFromCharList(maxLength, minLength, chars)

  //Helper method for randomAlpha
  //it works by taking any seq of char's and randomly grab's one of them and add's it to a tail recursion string building loop
  private def randomStringFromCharList(maxLength: Int, minLength: Option[Int], chars: Seq[Char]): String = {

    //Figure out if its a random length or a static length
    val length: Int = minLength match {
      case Some(min) => getRandomInt(maxLength, min)
      case None => maxLength
    }

    //Let the tail recursion loop <- this is basically a thread safe string builder
    def worker(length: Int, chars: Seq[Char], randomString: String = ""): String = {
      if (length == 0) randomString
      else worker(length - 1, chars, s"$randomString${chars(getRandomInt(chars.length, 0))}")
    }

    worker(length, chars)
  }

  //Randomly generates a month and day
  def getRandomMonthDay: (String, String) = {
    val month: Int = getRandomInt(12)
    val day: Int = getRandomInt(31)
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