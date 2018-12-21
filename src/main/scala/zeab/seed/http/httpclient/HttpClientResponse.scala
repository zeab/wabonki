package zeab.seed.http.httpclient

//Imports
import zeab.seed.http.HttpHeaders._
import zeab.seed.MapExtended

case class HttpClientResponse(
                               requestTimestamp: String = "",
                               requestUrl: String = "",
                               requestMethod: String = "",
                               requestBody: String = "",
                               requestHeaders: Map[String, String] = Map.empty,
                               requestMetaData: Map[String, String] = Map.empty,
                               responseStatusCode: Int = 0,
                               responseTimeInMs: Int = 0,
                               responseBody: String = "",
                               responseHeaders: Map[String, String] = Map.empty
                             ) extends MapExtended {
  override def toString: String =
    "\n" +
      s"""-----Response-----
         |* Unique Name: ${checkMapForString(uniqueNameKey, requestHeaders, "No Unique Name")}
         |* Timestamp: $requestTimestamp
         |* Response Status: $responseStatusCode
         |* Response Body:
         |$responseBody
         |* Response Headers:
         |${mapFormat(responseHeaders)}
         |* Response Time (ms): $responseTimeInMs
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