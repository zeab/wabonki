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
        .filter {param => "value [^_]\\S".r.findFirstIn(param.toString) match {case Some(_) => true; case None => false}}
        .filterNot{param => param.name.toString.lastOption.getOrElse("") == ' '}
        .map{param =>
          if (primitiveCheck(param.typeSignature.typeSymbol.name.toString)){
            param.name.toString -> instanceMirror.reflectField(param.asTerm).get.toString
          }
          else if (param.typeSignature.typeSymbol.name.toString == "List") {
            val theList: List[Any] = instanceMirror.reflectField(param.asTerm).get.asInstanceOf[List[Any]]
            val rr = theList.map{value =>
              if (primitiveCheck(value.getClass.getSimpleName)) {
                param.name.toString -> value
              }
              else {
                param.name.toString -> objToMap(value)
              }
            }.map(a => a)
            val ww = theList.map{value =>
              param.name.toString -> value
            }
            "" -> ww
          }
          else if (param.typeSignature.typeSymbol.name.toString == "Option") {
            val wrappedOptionValue = instanceMirror.reflectField(param.asTerm).get.asInstanceOf[Option[Any]]
            wrappedOptionValue match {
              case Some(value) =>
                if (primitiveCheck(value.getClass.getSimpleName)) param.name.toString -> value
                else param.name.toString -> objToMap(value)
              case None => param.name.toString -> ""
            }
          }
          else if (param.typeSignature.typeSymbol.name.toString == "Either") {"" -> ""}
          else param.name.toString -> objToMap(instanceMirror.reflectField(param.asTerm).get)
        }.toMap
    }
    val fullObjToMap: Map[String, Any] = Map(objectToSerialize.getClass.getSimpleName.seq(0).toLower + objectToSerialize.getClass.getSimpleName.drop(1) -> objToMap(objectToSerialize))

    def serialize(objectToSerialize:Map[String, Any]): String ={
      objectToSerialize.map{node =>
        val (nodeName, nodeWrappedValue): (String, Any) = node
        if(nodeWrappedValue.getClass.getSimpleName == "$colon$colon"){
          val ww = nodeWrappedValue.asInstanceOf[List[Any]]
          ww.map{a =>
            val nodeUnwrappedValue: String =
              if(primitiveCheck(a.getClass.getSimpleName)) a.asInstanceOf[String]
              else serialize(Map(a.asInstanceOf[(String, Any)]))
            if(nodeUnwrappedValue == "") s"<$nodeName/>"
            else s"<$nodeName>$nodeUnwrappedValue</$nodeName>"
          }.mkString.replace("<>", "").replace("</>", "")
        }
        else{
          val nodeUnwrappedValue: String =
            if(primitiveCheck(nodeWrappedValue.getClass.getSimpleName)) nodeWrappedValue.asInstanceOf[String]
            else serialize(nodeWrappedValue.asInstanceOf[Map[String, Any]])
          if(nodeUnwrappedValue == "") s"<$nodeName/>"
          else s"<$nodeName>$nodeUnwrappedValue</$nodeName>"
        }

//        println()
//        val nodeUnwrappedValue: String =
//          if(primitiveCheck(nodeWrappedValue.getClass.getSimpleName)) nodeWrappedValue.asInstanceOf[String]
//          else serialize(nodeWrappedValue.asInstanceOf[Map[String, Any]])
//        if(nodeUnwrappedValue == "") s"<$nodeName/>"
//        else s"<$nodeName>$nodeUnwrappedValue</$nodeName>"
      }.mkString
    }
    serialize(fullObjToMap)
  }

  def xmlDeserialize[T](xmlText: String)(implicit typeTag: TypeTag[T]): T ={
    ???
  }

  private def primitiveCheck(objToCheck:String): Boolean ={
    val primitive = List("String", "Int", "Integer", "Double", "Float", "Long")
    primitive.contains(objToCheck)
  }

}
