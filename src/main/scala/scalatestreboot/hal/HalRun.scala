package scalatestreboot.hal

import java.util.UUID

object HalRun {

  def main(args: Array[String]): Unit = {

    val testRunId = UUID.randomUUID().toString
    //Reflect all the random classes here and get the list
    //Fire off the list into the ether
    val w = new MyFirstHalTest()

    val e = new MyFirstHalTest()

    val hh =
    (w.testList ++ e.testList).map{test =>
      val t0 = System.currentTimeMillis
      val t = test.test.apply()
      val duration = System.currentTimeMillis() - t0
      (testRunId, UUID.randomUUID().toString, test.testName, t, duration)
    }
    hh.foreach(println)

  }

}
