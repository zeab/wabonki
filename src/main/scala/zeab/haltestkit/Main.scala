package zeab.haltestkit

//Imports
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, NodeSeq}
import scala.xml.XML.loadString

object Main {

  def main(args: Array[String]): Unit = {

    val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
    val reflectedClass: ClassSymbol = mirror.staticClass("zeab.haltestkit.MyFirstTestSuite")
    val reflectedType: Type = reflectedClass.typeSignature
    val reflectedValue = reflectedType.decls
    val instance = mirror.reflect(reflectedClass).instance
    val methods = instance.getClass.getDeclaredMethods.toList

    val objType: Type = mirror.classSymbol(instance.getClass).toType
    val x = objType.decls

    println()

  }

}
