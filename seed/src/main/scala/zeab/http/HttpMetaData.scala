package zeab.http

trait HttpMetaData{

  val setHostKey: String = "SetHost"
  
  val setConnectTimeoutKey: String = "SetConnectTimeout"
  val setReadTimeoutKey: String = "SetReadTimeout"
  val setUserAgentKey: String = "SetUserAgent"
  val setCloseHttpHeader: String = "SetCloseHttpHeader"

}

object HttpMetaData extends HttpMetaData
