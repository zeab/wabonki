package scalatestreboot.scalatestexamples

object Run {

  def main(args: Array[String]): Unit = {

    (new MyFirstTest).execute(testName = "1 + 1 == 2")

    //The lastest ones
    //val specName: String = "scalatestreboot.scalatestexamples.MyFirstTest"
    //val x = org.scalatest.tools.Runner.run(Array("-o", "-s", specName, "-P16"))

    //The ones from back in alasc days...

    //val x = org.scalatest.tools.Runner.run(Array("-R", ".", "-oF", "-P8", "-s", specName))

  }

}
