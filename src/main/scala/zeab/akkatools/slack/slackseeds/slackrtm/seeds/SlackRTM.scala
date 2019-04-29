package zeab.akkatools.slack.slackseeds.slackrtm.seeds

//Imports
import zeab.seed.http.HttpSeed
import zeab.seed.http.HttpMethods.get
import zeab.seed.http.HttpMetaDataKeys._

object SlackRTM {

  def getRTMConnectionHttpSeed(bearer:String): HttpSeed = {
    val url: String = "https://slack.com/api/rtm.connect"
    val method: String = get
    val metaData: Map[String, String] = Map(setBearerKey -> bearer)
    HttpSeed(url, method, metaData = metaData)
  }

}
