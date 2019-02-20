package zeab.randomness

//Imports
import java.util.concurrent.ThreadLocalRandom
import scala.annotation.tailrec

/**
  * A collection of useful functions to generate random values for all types
  * Utilizes Java's ThreadLocalRandom rather than the Regular Random
  *
  * Per my current understanding while the regular Java random functions is thread safe
  * If used concurrently the regular random could contention and thus performance loss
  * Using ThreadLocalRandom's implementation of random solves that problem
  *
  * @author Kevin Kosnik-Downs (Zeab)
  * @since 2.12
  */
trait Randomness {

  /** Returns the next generated pseudorandom between Int.MaxValue and Int.MinValue from the current thread local random instance.
    *
    * @param max The greatest Int the return can possibly be (inclusive)
    * @param min The least Int the return can possibly be (inclusive)
    * @return Pseudorandom Int
    * @note Within the java implementation of ThreadLocalRandom
    *       The origin (min) is inclusive
    *       The Bound (max) is exclusive
    *       Which is why its necessary to sometimes add 1 behind the scenes to the max value so this becomes inclusive
    */
  def getRandomInt(max: Int = Int.MaxValue, min: Int = Int.MinValue): Int = max match {
    case Int.MaxValue => ThreadLocalRandom.current.nextInt(min, max)
    case _ => ThreadLocalRandom.current.nextInt(min, max + 1)
  }

  /** Returns the next generated pseudorandom between Long.MaxValue and Long.MinValue from the current thread local random instance.
    *
    * @param max The greatest Long the return can possibly be (inclusive)
    * @param min The least Long the return can possibly be (inclusive)
    * @return Pseudorandom Long
    * @note Within the java implementation of ThreadLocalRandom
    *       The origin (min) is inclusive
    *       The Bound (max) is exclusive
    *       Which is why its necessary to sometimes add 1 behind the scenes to the max value so this becomes inclusive
    */
  def getRandomLong(max: Long = Long.MaxValue, min: Long = Long.MinValue): Long = max match {
    case Int.MaxValue => ThreadLocalRandom.current.nextLong(min, max)
    case _ => ThreadLocalRandom.current.nextLong(min, max + 1L)
  }

  /** Returns the next generated pseudorandom between Double.MaxValue and Double.MinValue from the current thread local random instance.
    *
    * @param max The greatest Double the return can possibly be (inclusive)
    * @param min The least Double the return can possibly be (inclusive)
    * @return Pseudorandom Double
    * @note Within the java implementation of ThreadLocalRandom
    *       The origin (min) is inclusive
    *       The Bound (max) is exclusive
    *       This since its a double is not necessary to add 1 because that results int an off by 1 error
    *       A max of 10 and min of 4 without the plus 1 correction the others have will possibly yield 10.1-10.9 which is outside value zone
    */
  def getRandomDouble(max: Double = Double.MaxValue, min: Double = Double.MinValue): Double = ThreadLocalRandom.current.nextDouble(min, max)

  //Letters and numbers
  /** Returns a string of pseudorandom numbers and lower/upper case letters from the current thread local random instance.
    *
    * @param maxLength The greatest number of char the random string the return can possibly be (inclusive)
    * @param minLength The least number of char the random string will be; defaults to max value
    * @return Pseudorandom Letters and Numbers string
    */
  def getRandomAlphaNumericString(maxLength: Int, minLength: Option[Int] = None): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    getRandomCustomString(maxLength, minLength, chars)
  }

  //Just Letters
  /** Returns a string of pseudorandom lower and upper case letters from the current thread local random instance.
    *
    * @param maxLength The greatest number of char the random string the return can possibly be (inclusive)
    * @param minLength The least number of char the random string will be; defaults to max value
    * @return Pseudorandom Letters only string
    */
  def getRandomAlphaString(maxLength: Int, minLength: Option[Int] = None): String = {
    val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z')
    getRandomCustomString(maxLength, minLength, chars)
  }

  //Numbers only
  /** Returns a string of pseudorandom numbers from the current thread local random instance.
    *
    * @param maxLength The greatest number of char the random string the return can possibly be
    * @param minLength The least number of char the random string will be; defaults to max value
    * @return Pseudorandom Letters and Numbers string
    */
  def getRandomNumericString(maxLength: Int, minLength: Option[Int] = None): String = {
    val chars: Seq[Char] = '0' to '9'
    getRandomCustomString(maxLength, minLength, chars)
  }

  //User defined char-set
  /** Returns a string of pseudorandom user defined input from the current thread local random instance.
    *
    * This is the work horse of the entire Random String set of functions as they are all just hard coded user defined char sets
    * It is also where the random length of the string is determined for all of them
    *
    * @param chars     The seq of character's you wish to choose from to form this string
    * @param maxLength The greatest number of char the random string the return can possibly be
    * @param minLength The least number of char the random string will be; defaults to max value
    * @return Pseudorandom string from user defined values
    */
  def getRandomCustomString(maxLength: Int, minLength: Option[Int], chars: Seq[Char]): String = {

    //Figure out if its a random length or a static length
    val length: Int = minLength match {
      case Some(min) => getRandomInt(maxLength, min)
      case None => maxLength
    }

    //Let the tail recursion loop <- this is basically a thread safe string builder
    @tailrec
    def worker(length: Int, chars: Seq[Char], randomString: String = ""): String = {
      if (length == 0) randomString
      else worker(length - 1, chars, s"$randomString${chars(getRandomInt(chars.length - 1, 0))}")
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