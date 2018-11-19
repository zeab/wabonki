package zeab.randomness

//Imports
import org.scalatest.FunSpec

class RandomnessSpec extends FunSpec {

  describe("Randomness") {

    it("random alpha string with a length of 5") {
      val randomness: String = Randomness.getRandomAlpha(5)
      assert(randomness.matches("[A-Za-z]*"))
      assert(randomness.length == 5)
    }

    it("random numeric string with a length of 5") {
      val randomness: String = Randomness.getRandomNumeric(5)
      assert(randomness.matches("""\d*"""))
      assert(randomness.length == 5)
    }

    it("random alpha numeric string with a length of 5") {
      val randomness: String = Randomness.getRandomAlphaNumeric(5)
      assert(randomness.matches("[0-9a-zA-Z-_]*"))
      assert(randomness.length == 5)
    }

    it("random alpha string with empty") {
      val randomness: String = Randomness.getRandomAlpha(0)
      assert(randomness == "")
    }

    it("random numeric string with emoty") {
      val randomness: String = Randomness.getRandomNumeric(0)
      assert(randomness == "")
    }

    it("random alpha numeric string with empty") {
      val randomness: String = Randomness.getRandomAlphaNumeric(0)
      assert(randomness == "")
    }

    it("random non zero is not zero and a number") {
      val randomness: Int = Randomness.getRandomNonZero(3)
      assert(randomness != 0)
    }

  }

}
