package zeab.akkatools.slackbot.slackseeds.slackrtm.seeds

//Imports
import zeab.seed.http.{HttpMethods, HttpSeed}

class SlackRTM {

  val getRTMConnectionHttpSeed: HttpSeed = {
    val url = "https://slack.com/api/rtm.connect"
    val method = HttpMethods.get
    HttpSeed(url, method)
  }

}
