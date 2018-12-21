package zeab.akkatools.slackbot.slackwebhook

/** Collection of messages for Slack Reporter Actor */
object SlackWebhookMessages {

  /** Post a message to slack with webhook
    *
    * @param msg     Text to post to the slack channel from the webhook
    * @param webhook Slack webhook */
  case class PostSlackWebhook(
                               msg: String,
                               webhook: String
                             )

}
