package zeab.seed.http.httpclient

//Imports
import zeab.seed.http.HttpHeaders._
import zeab.seed.MapExtended

case class HttpClientError(
                            requestTimestamp: String = "",
                            requestUrl: String = "",
                            requestMethod: String = "",
                            requestBody: String = "",
                            requestHeaders: Map[String, String] = Map.empty,
                            requestMetaData: Map[String, String] = Map.empty,
                            responseTime: Int = 0,
                            responseStatusCode: Int = 0,
                            errorMsg: String = "",
                            errorDetailedMsg: String = ""
                          ) extends MapExtended {
  override def toString: String =
    "\n" +
      s"""-----Error-----
         |* Unique Name: ${checkMapForString(uniqueNameKey, requestHeaders, "No Unique Name")}
         |* Timestamp: $requestTimestamp
         |* Error:
         |$errorMsg
         |* ErrorExtended:
         |$errorDetailedMsg
         |* Status Code:
         |$responseStatusCode
         |* Response Time (ms): ${responseTime}ms
         |--Request Data--
         |* Url: $requestUrl
         |* Method: $requestMethod
         |* Body:
         |${if (requestBody == "") "Blank" else requestBody}
         |* Headers:
         |${mapFormat(requestHeaders)}
         |* MetaData:
         |${mapFormat(requestMetaData)}""".stripMargin

}