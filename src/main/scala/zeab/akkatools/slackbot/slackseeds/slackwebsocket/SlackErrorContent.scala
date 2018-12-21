package zeab.akkatools.slackbot.slackseeds.slackwebsocket

case class SlackErrorContent(
                              msg: String,
                              code: Int,
                              source: String
                            )
