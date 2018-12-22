package zeab.akkatools.slackbot.slackseeds.slackrtm.seeds

//Imports
import zeab.seed.http.HttpSeed
import zeab.seed.http.HttpMethods.get

class SlackRTM {

  val getRTMConnectionHttpSeed: HttpSeed = {
    val url = "https://slack.com/api/rtm.connect"
    val method = get
    HttpSeed(url, method)
  }

}
