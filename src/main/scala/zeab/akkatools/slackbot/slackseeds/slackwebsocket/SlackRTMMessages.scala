package zeab.akkatools.slackbot.slackseeds.slackwebsocket

/** Collection of WS messages to and from slack */
object SlackRTMMessages {

  /** Slack Response Error */
  case class SlackError(
                         ok: Boolean,
                         reply_to: Int,
                         error: SlackErrorContent
                       )

  /** Slack Response Accept */
  case class SlackAccept(
                          ok: Boolean,
                          reply_to: Int,
                          ts: String,
                          text: String
                        )

  /** Message for Slack */
  case class SlackRTMMessage(
                              `type`: String,
                              channel: String,
                              user: String,
                              text: String,
                              ts: String,
                              source_team: String,
                              team: String
                            )

  /** Complete outbound event to slack */
  case class OutboundSlackEvent(
                                 id: Int,
                                 `type`: String,
                                 channel: String,
                                 text: String)

}
