package zeab.randomness

trait Randomness {

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

}

//So we can use it as is without having to extend it if we don't want
object Randomness extends Randomness
