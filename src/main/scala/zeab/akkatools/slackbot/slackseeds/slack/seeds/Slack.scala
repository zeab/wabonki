package zeab.akkatools.slackbot.slackseeds.slack.seeds

//Imports
import zeab.akkatools.slackbot.slackseeds.slack.models.request.PostSlackClump
import zeab.seed.http.{HttpHeaders, HttpMethods, HttpSeed}
//Circe
import io.circe.generic.auto._
import io.circe.syntax._

class Slack {

  //Post a message directly to a slack web hook
  def postSlackWebhook(requestClump: PostSlackClump): HttpSeed = {
    val url: String = s"https://hooks.slack.com/services/${requestClump.webhook}"
    val method: String = HttpMethods.post
    val body: String = requestClump.body.asJson.noSpaces
    val headers: Map[String, String] =  Map.empty//HttpHeaders.contentJsonHeader
    HttpSeed(url, method, body, headers)
  }

}
