package zeab.haltestkit

import scala.reflect.runtime.{universe => ru}

object MainOther {

  def main(args: Array[String]): Unit = {

    val test = "zeab.haltestkit.demotests.MyFirstHalTest"

    val classLoader: ClassLoader = ClassLoader.getSystemClassLoader
    val mirror = ru.runtimeMirror(classLoader)
    val testClass: Class[_] = classLoader.loadClass(test)
    val testSymbol = mirror.classSymbol(testClass)


    val i = testSymbol.typeSignature
    i.baseClasses.find(a => "HalTest".equals(a.name.toString)) match {
      case Some(c) =>
        val g = c.asClass.typeSignature.decls
        val uu =
        g.find(d => d.isMethod) match {
          case Some(x) =>
            val kk = x.asMethod
            Some(kk)
          case None => None
        }
        println()
      case None =>
    }

    println()




//    val clazz = m.staticClass(test)
//    val cm = m.reflectClass(clazz)
//
//    val constructor = clazz.primaryConstructor.asMethod
//    val constructionMirror = cm.reflectConstructor(constructor)
//    val instance = constructionMirror.apply()

    println()


  }

}
