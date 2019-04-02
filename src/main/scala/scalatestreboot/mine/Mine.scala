package scalatestreboot.mine

import scala.reflect.runtime.{universe => ru}

object Mine {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.haltestkit.MyFirstTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()
    //val myInstance = instance.asInstanceOf[HalTestCase]

    val reflected = m.reflect(instance)
//    val methodSymbol = ru.typeOf[Test].dedecl(ru.TermName("Test")).asMethod
//    val method = reflected.reflectMethod(methodSymbol)
//    val kk = method.apply("This is a test", false)

    println()


  }

}
