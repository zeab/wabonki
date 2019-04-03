package scalatestreboot.scalatestexamples

object Run {

  def main(args: Array[String]): Unit = {

    val gg = (new MyFirstTest).getClass.getDeclaredMethods

    (new MyFirstTest).execute(testName = "not an equation")

    //The lastest ones
    //val specName: String = "scalatestreboot.scalatestexamples.MyFirstTest"
    //val x = org.scalatest.tools.Runner.run(Array("-o", "-s", specName, "-P16"))

    //The ones from back in alasc days...
    //val x = org.scalatest.tools.Runner.run(Array("-R", ".", "-oF", "-P8", "-s", specName))


  }

}
