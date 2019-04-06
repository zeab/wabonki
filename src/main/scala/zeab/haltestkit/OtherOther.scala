package zeab.haltestkit

import scala.reflect.runtime.{universe => ru}
import scala.util.Try

object OtherOther {

  def main(args: Array[String]): Unit = {


    //val test = "zeab.haltestkit.demotests.MyFirstHalTest"
    val test = "zeab.haltestkit.demotests.OtherTest"

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass(test)
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)

    val instance = constructionMirror.apply()


    val ee = instance.getClass.getMethods.map(_.getName)

    val im = m.reflect(instance)
    //val l = instance.getClass.getMethods.map(_.getName).headOption //.filter(_.contains("$anonfun$new$1"))
    val l = instance.getClass.getMethods.filter{m =>
      m.getName.contains("$anonfun")
    }.toList

    l.map{test => Try(test.invoke(instance))}.foreach(println)

  }

}
