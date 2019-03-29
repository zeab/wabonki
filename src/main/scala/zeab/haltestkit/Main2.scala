package zeab.haltestkit

import scala.reflect.runtime.{universe => ru}

object Main2 {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.haltestkit.MyFirstTestSuite")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()
    val x = instance.asInstanceOf[HalTestSuite]
    val y = x.getClass.getDeclaredClasses
    val z = x.getClass.getDeclaredMethods.toList
    println()

  }

}
