package zeab.seed.http

trait HttpMetaData{

  val setDomainKey: String = "SetDomain"
  
  val setConnectTimeoutKey: String = "SetConnectTimeout"
  val setReadTimeoutKey: String = "SetReadTimeout"
  val setUserAgentKey: String = "SetUserAgent"
  val setCloseHttpHeader: String = "SetCloseHttpHeader"

  val setTestRunIdKey: String = "SetTestRunId"
  val setTimer: String = "SetTimer"

  val setBearerKey: String = "SetBearer"

  def setBearer(value:String): Map[String, String] = Map(setBearerKey -> value)

}

object HttpMetaData extends HttpMetaData
