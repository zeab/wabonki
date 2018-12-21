package zeab.akkatools

//Imports
import akka.stream.scaladsl.Source
import akka.util.ByteString
import zeab.seed.http.{HttpMethods, HttpSeed}
import zeab.seed.http.httpclient.{HttpClientError, HttpClientResponse}
//Java
import java.text.SimpleDateFormat
import java.util.Calendar
//Akka
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpMethod, HttpRequest, HttpMethods => akkaMethods}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

package object akkahttpclient {

  //TODO theres a lot more settings im sure you can actually set with all of these settings

  //Deal with authorization
  def authorization(url: String, method: String, body: String, headers: Map[String, String], metaData: Map[String, String]): Map[String, String] = Map.empty

  //Invoke with http seed
  def invokeAsyncHttpClientResponse(
                                     httpSeed: HttpSeed,
                                     isReturnBody: Boolean
                                   )
                                   (implicit actorSystem:ActorSystem, executionContext: ExecutionContext, actorMaterilizer: ActorMaterializer): Future[Either[HttpClientError, HttpClientResponse]] =
    invokeAsyncHttpClientResponse(httpSeed.url, httpSeed.method, httpSeed.body, httpSeed.headers, httpSeed.metaData, isReturnBody)

  //Main invoke for the single request http client that comes with akka
  def invokeAsyncHttpClientResponse(
                                     url: String,
                                     method: String = HttpMethods.get,
                                     body: String = "",
                                     headers: Map[String, String] = Map.empty,
                                     metaData: Map[String, String] = Map.empty,
                                     isReturnBody: Boolean = true
                                   )
                                   (implicit actorSystem:ActorSystem, executionContext: ExecutionContext, actorMaterilizer: ActorMaterializer): Future[Either[HttpClientError, HttpClientResponse]] ={

    val request: Either[HttpClientError, HttpRequest] = createHttpRequest(url, method, body, headers, metaData)

    val callCreateTimeMark: Long = System.currentTimeMillis()
    val timestamp: String = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS").format(Calendar.getInstance().getTime)
    request match {
      case Right(r) =>
        for {
          response <- Http().singleRequest(r)
          responseBody <- if (isReturnBody) Unmarshal(response.entity).to[String] else Future("Body not decoded on purpose")
        } yield Right(HttpClientResponse(timestamp, url, method, body, headers, metaData, response.status.intValue(), (System.currentTimeMillis() - callCreateTimeMark).toInt, responseBody, response.headers.map{header => header.name -> header.value}.toMap))
      case Left(ex) => Future(Left(ex))
    }
  }

  //Return the http client as the source for a stream
  def invokeAsSource(
                      url: String,
                      method: String = HttpMethods.get,
                      body: String = "",
                      headers: Map[String, String] = Map.empty,
                      metaData: Map[String, String] = Map.empty,
                    ): Either[HttpClientError, Source[ByteString, Any]] =
    createHttpRequest(url, method, body, headers, metaData).map(_.entity.dataBytes)


  //Creates the actual http request that is used in both invokes
  private def createHttpRequest(
                                 url: String,
                                 method: String = HttpMethods.get,
                                 body: String = "",
                                 headers: Map[String, String] = Map.empty,
                                 metaData: Map[String, String] = Map.empty,
                               ): Either[HttpClientError, HttpRequest] ={
    val requestMethod: HttpMethod =
      method match {
        case HttpMethods.get => akkaMethods.GET
        case HttpMethods.post => akkaMethods.POST
        case HttpMethods.put => akkaMethods.PUT
        case HttpMethods.delete => akkaMethods.DELETE
        case HttpMethods.trace => akkaMethods.TRACE
      }

    val requestHeaders: List[RawHeader] = (authorization(url, method, body, headers, metaData) ++ headers).map{header =>
      val (headerKey, headerValue) = header
      RawHeader(headerKey, headerValue)
    }.toList

    //TODO Make this safer
    if (false) Left(HttpClientError())
    else Right(HttpRequest(requestMethod, url, requestHeaders, body))
  }

}
