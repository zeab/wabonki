package zeab.akkatools.slackbot.slackseeds.slackrtm.models.response

//Imports
import zeab.akkatools.slackbot.slackseeds.slackrtm.models.general.{Self, Team}

case class GetRTMConnection200(
                                ok: Boolean,
                                url: String,
                                team: Team,
                                self: Self
                              )
