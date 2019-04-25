package zeab.akkatools.slack.slackseeds.slackrtm.models.response

//Imports
import zeab.akkatools.slack.slackseeds.slackrtm.models.general.{Self, Team}

case class GetRTMConnection200(
                                ok: Boolean,
                                url: String,
                                team: Team,
                                self: Self
                              )
