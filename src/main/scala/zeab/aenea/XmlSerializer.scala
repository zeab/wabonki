package zeab.aenea

//Imports
import scala.reflect.runtime.universe._

object XmlSerializer {

  def xmlSerialize[T](objectToSerialize: Any)(implicit typeTag: TypeTag[T]): String ={
    def objToMap(objectToSerialize: Any): Map[String, Any] ={
      val objClass: Class[_] = objectToSerialize.getClass
      val objMirror: Mirror = runtimeMirror(objClass.getClassLoader)
      val objType: Type = objMirror.classSymbol(objClass).toType
      val instanceMirror: InstanceMirror = objMirror.reflect(objectToSerialize)
      objType.decls
        .filter {param => "value \\S".r.findFirstIn(param.toString) match {case Some(_) => true; case None => false}}
        .filterNot{param => param.name.toString.lastOption.getOrElse("") == ' '}
        .map{param =>
          if (param.typeSignature.typeSymbol.name.toString == "String" | param.typeSignature.typeSymbol.name.toString == "Int")
            param.name.toString -> instanceMirror.reflectField(param.asTerm).get.toString
          else param.name.toString -> objToMap(instanceMirror.reflectField(param.asTerm).get)
        }.toMap
    }
    val fullObjToMap: Map[String, Any] = Map(objectToSerialize.getClass.getSimpleName.seq(0).toLower + objectToSerialize.getClass.getSimpleName.drop(1) -> objToMap(objectToSerialize))

    def serialize(objectToSerialize:Map[String, Any]): String ={
      objectToSerialize.map{node =>
        val (nodeName, nodeWrappedValue): (String, Any) = node
        val nodeUnwrappedValue: String =
          if(nodeWrappedValue.getClass.getSimpleName == "String") nodeWrappedValue.asInstanceOf[String]
          else serialize(nodeWrappedValue.asInstanceOf[Map[String, Any]])
        s"<$nodeName>$nodeUnwrappedValue</$nodeName>"
      }.mkString
    }
    serialize(fullObjToMap)
  }

  def xmlDeserialize[T](xmlText: String)(implicit typeTag: TypeTag[T]): T ={
    ???
  }


}
