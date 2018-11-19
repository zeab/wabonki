//Imports
import sbt.Keys._
import sbt._

object Artifactory extends AutoPlugin {

  val credsFile: String = "scpprd.credentials"
  val res: String = "https://scpprd.jfrog.io/scpprd/sbux-local/"
  val pubTo: String = "https://scpprd.jfrog.io/scpprd/sbux-local"

  override lazy val projectSettings: Seq[Def.Setting[_]] = getArtifactoryCredentials

  // Run on everything in the project automatically
  override def trigger = allRequirements

  def getArtifactoryCredentials: Seq[Def.Setting[_]] = {
    val propRealm = sys.props.get("ARTIFACTORY_REALM")
    val propHost = sys.props.get("ARTIFACTORY_HOST")
    val propUser = sys.props.get("ARTIFACTORY_USER")
    val propPassword = sys.props.get("ARTIFACTORY_PASSWORD").map(_.replaceAll("^'+|'+$", ""))
    val envRealm = sys.env.get("ARTIFACTORY_REALM")
    val envHost = sys.env.get("ARTIFACTORY_HOST")
    val envUser = sys.env.get("ARTIFACTORY_USER")
    val envPassword = sys.env.get("ARTIFACTORY_PASSWORD").map(_.replaceAll("^'+|'+$", ""))
    val artifactoryCredentialsFile = Path.userHome / ".sbt" / credsFile

    ((propRealm, propHost, propUser, propPassword), (envRealm, envHost, envUser, envPassword)) match {
      case ((Some(realm), Some(host), Some(user), Some(password)), (_, _, _, _)) =>
        Seq(
          credentials += Credentials(realm, host, user, password),
          resolvers += "Artifactory" at res,
          publishTo := Some(realm at host)
        )
      case ((_, _, _, _), (Some(realm), Some(host), Some(user), Some(password))) =>
        Seq(
          credentials += Credentials(realm, host, user, password),
          resolvers += "Artifactory" at res,
          publishTo := Some(realm at host)
        )
      case _ =>
        if (artifactoryCredentialsFile.exists()) {
          Seq(
            credentials += Credentials(artifactoryCredentialsFile),
            resolvers += "Artifactory" at res,
            publishTo := Some("Artifactory Realm" at pubTo)
          )
        }
        else {
          println("Unable to find environment variables or config file for artifactry... if this is an error fix it")
          Seq.empty
        }
    }
  }

}
