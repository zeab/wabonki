package zeab.akkatools.slack.slackseeds.slack.seeds

//Imports
import zeab.akkatools.slack.slackseeds.slack.models.request.PostSlackRequestBody
import zeab.seed.http.{HttpMethods, HttpSeed}
import zeab.seed.http.ContentTypes._
import zeab.seed.http.HttpHeaders._
//Circe
import io.circe.generic.auto._
import io.circe.syntax._

object Slack {

  //Post a message directly to a slack web hook
  def postSlackWebhook(webhook: String, requestBody: PostSlackRequestBody): HttpSeed = {
    val url: String = s"https://hooks.slack.com/services/$webhook"
    val method: String = HttpMethods.post
    val body: String = requestBody.asJson.noSpaces
    val headers: Map[String, String] = Map(contentTypeKey -> applicationJson)
    HttpSeed(url, method, body, headers)
  }

}
