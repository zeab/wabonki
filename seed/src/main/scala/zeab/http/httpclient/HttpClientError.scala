package zeab.http.httpclient

//Imports
import zeab.http.HttpHeaders._
import zeab.MapExtended

case class HttpClientError(
                            requestTimestamp: String = "",
                            requestUrl: String = "",
                            requestMethod: String = "",
                            requestBody: String = "",
                            requestHeaders: Map[String, String] = Map.empty,
                            requestMetaData: Map[String, String] = Map.empty,
                            responseTimeInMs: Int = 0,
                            errorMsg: String = "",
                            errorDetailedMsg: String = "",
                            statusCode: Int = 0
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
         |$statusCode
         |* Response Time (ms): ${responseTimeInMs}ms
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