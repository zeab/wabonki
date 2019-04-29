package zeab.seed.http.authorization

//Imports
import zeab.seed.http.{HttpHeaders, HttpMetaDataKeys}

object BearerAuthorization {

  def bearerAuthorization(metaData:Map[String, String]): Map[String, String] ={
    metaData.find(_._1 == HttpMetaDataKeys.setBearerKey) match {
      case Some(bearer) => HttpHeaders.bearerHeader(bearer._2)
      case None => Map.empty
    }
  }

}
