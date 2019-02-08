package zeab.sys

import scala.util.Random

object Main extends EnvVars {

  def main(args: Array[String]): Unit = {


    val aa = List("a", "b")
    val hh = Random.shuffle(aa).take(1)


    val gg = getEnvVarAsString("home", "llama")

    println(gg)

  }

}
