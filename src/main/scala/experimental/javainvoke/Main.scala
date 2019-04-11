package experimental.javainvoke

//Imports
import java.lang.reflect.Method
//Scala
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}

//this is a way to do a scala reflection and then invoke the methods on that reflected class

object Main {

  def main(args: Array[String]): Unit = {

    val test = "zeab.haltestkit.demotests.OtherTest"

    val runTimeMirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val reflectedClassSymbol: ClassSymbol = runTimeMirror.staticClass(test)
    val classMirror: ClassMirror = runTimeMirror.reflectClass(reflectedClassSymbol)
    val constructorMethod: MethodSymbol = reflectedClassSymbol.primaryConstructor.asMethod
    val constructorMirror: MethodMirror = classMirror.reflectConstructor(constructorMethod)
    val instance: Any = constructorMirror.apply()

    val kk = instance.getClass.getDeclaredMethods

    val pendingTests: List[Method] =
      instance.getClass.getDeclaredMethods.filter{_.getName.contains("$anonfun")}.toList
    pendingTests.map { test =>
      val ee = test.invoke(instance)
      Try(test.invoke(instance))
    }
      .filter{test =>
        test match {
          case Success(t) =>
            val e = t
            t.isInstanceOf[(String, Boolean, Any)] | t.isInstanceOf[String]
          case Failure(_) => false
        }
      }
      .foreach(println)

  }

}
