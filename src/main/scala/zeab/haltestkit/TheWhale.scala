package zeab.haltestkit

import java.lang.reflect.Method
import scala.reflect.runtime.universe._
import scala.util.Try

object TheWhale {

  def main(args: Array[String]): Unit = {

    val test = "zeab.haltestkit.demotests.OtherTest"

    val runTimeMirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val reflectedClassSymbol: ClassSymbol = runTimeMirror.staticClass(test)
    val classMirror: ClassMirror = runTimeMirror.reflectClass(reflectedClassSymbol)
    val constructorMethod: MethodSymbol = reflectedClassSymbol.primaryConstructor.asMethod
    val constructorMirror: MethodMirror = classMirror.reflectConstructor(constructorMethod)
    val instance: Any = constructorMirror.apply()

    val pendingTests: List[Method] =
      instance.getClass.getMethods.filter{method =>method.getName.contains("$anonfun")}.toList

    pendingTests.map{test => Try(test.invoke(instance))}.foreach(println)

  }

}
