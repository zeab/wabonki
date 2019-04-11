package experimental.scalareflection

class JustSomeMethods {

  def test(testName: String, test: => (Boolean, Any)): (String, () => (Boolean, Any)) = (testName, test _)

  def tet2(testName: => String, test: => (Boolean, Any)) = Unit

  def ttt(test: => (String, () => Any)) = Unit
  def uu(t: => Any) = Unit

  def saySomething(prefix: => String) = (s: String) => {
    prefix + " " + s
  }

}
