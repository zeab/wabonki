package scalatestreboot.mine

import org.scalatest.Suite
import scalatestreboot.scalatestexamples.MyFirstTest

import scala.reflect.runtime.{universe => ru}

object Mine {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("scalatestreboot.scalatestexamples.MyFirstTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()
    val myInstance = instance.asInstanceOf[Suite]

    val reflected = m.reflect(instance)
    val ee = reflected.instance.getClass.getDeclaredMethods

    val kk = instance.getClass.getDeclaredMethods

    println()
    def getValue[T](obj: T, memberName: String): Any = {
      val method = obj.getClass.getDeclaredMethod(memberName)
      method.invoke(obj)
    }

    val gg = getValue(instance, "$anonfun$new$1")

    val hh = reflected.symbol.typeSignature.decls

    val jj = instance.getClass.getDeclaredMethods
    println(ee)
    val methodSymbol1 = ru.typeOf[MyFirstTest].decls
    val methodSymbol = ru.typeOf[MyFirstTest].decl(ru.TermName("$anonfun$new$1")).asMethod
//    val method = reflected.reflectMethod(methodSymbol)
//    val kk = method.apply("This is a test", false)



    println()


  }

}
