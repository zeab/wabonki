package zeab.sys

object Main extends EnvVars {

  def main(args: Array[String]): Unit = {


    val gg = getEnvVarAsString("home", "llama")

    println(gg)

  }

}
