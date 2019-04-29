package zeab.akkatools.webservice

//Imports
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, pathPrefix}
import akka.http.scaladsl.server.Route

object K8Routes {

  def readinessCheck(path: String = "readiness")(implicit actorSystem: ActorSystem): Route = {
    pathPrefix(path) {
      get {
        complete(StatusCodes.OK, "Readiness Check Passed")
      }
    }
  }

  //TODO Allow for a function to be called to expand the health check for other services...
  def livenessCheck(path: String = "liveness")(implicit actorSystem: ActorSystem): Route = {
    pathPrefix(path) {
      get {
        complete(StatusCodes.OK, "Liveness Check Passed")
      }
    }
  }

}
