package zeab.akkatools.slack.slackseeds.slackwebsocket

case class SlackErrorContent(
                              msg: String,
                              code: Int,
                              source: String
                            )
