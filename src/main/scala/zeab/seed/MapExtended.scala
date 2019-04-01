package zeab.seed

//Need to do something like ... move all the formatting into here and call it that...
//Then move it into the core bit so that its not just hanging out here by it self...

trait MapExtended {

  def mapFormat(m: Map[String, String]): String =
    if(m.isEmpty) "Blank"
    else{
      m.toList.map { i =>
        val (k, m) = i; s"  $k -> $m"
      }.mkString("  ", "\n  ", "")
    }

  def checkMapForBoolean(key: String, metaData: Map[String, String], default: Boolean = false): Boolean =
    metaData.getOrElse(key, default.toString).toBoolean

  def checkMapForString(key: String, metaData: Map[String, String], default: String = "default"): String =
    metaData.getOrElse(key, default)

  def checkMapForInt(key: String, metaData: Map[String, String], default: Int = 0): Int =
    metaData.getOrElse(key, default.toString).toInt

}
