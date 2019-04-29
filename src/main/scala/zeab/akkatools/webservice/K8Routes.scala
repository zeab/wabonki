package zeab.akkatools.webservice

//Imports
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, pathPrefix}
import akka.http.scaladsl.server.Route

object K8Routes {

  //TODO Allow for a function to be called to expand the health check for other services...
  def readinessCheck(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("readiness") {
      get {
        complete(StatusCodes.OK, "Readiness Check Passed")
      }
    }
  }

  //TODO Allow for a function to be called to expand the health check for other services...
  def healthCheck(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("health") {
      get {
        complete(StatusCodes.OK, "Health Check Passed")
      }
    }
  }

}
