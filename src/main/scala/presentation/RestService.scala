package presentation

import actor.{ETASupervisorActor, ErrorLogActor}
import akka.actor._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import bizmodel._

import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * Exposes the REST APIs for dadr
  * @param system        ActorSystem for dadr
  * @param materializer  ActorMaterializer for dadr
  */
class RestService(implicit val system: ActorSystem, val materializer: ActorMaterializer)
  extends RestRoutes {


}


/**
  * Defines all the routes for dadr
  */
trait RestRoutes extends Api with Marshalling with ETAMessages {

  /**
    * Defines the routing tree for dadr
    * @return  Routing Tree of dadr
    */
  def routes: Route = routeDetails ~ AddRoute ~ subtractRoute

  /**
    * Defines the route or handler for displaying dadrs in the wall of a user
    * @return  Route for displaying dadrs in the wall of a user
    */

  def subtractRoute=pathPrefix("delete") {
    pathEndOrSingleSlash {
      post {
        entity(as[DeleteRoute]) { (delSeq:DeleteRoute) =>
          onComplete(calculateETADel(delSeq.newSequence)) {
            case Success(successResponse) =>
              successResponse match {
                case SuccessResponse => complete(OK)
                case ErrorResponse => complete(OK)
              }
            case Failure(_) => complete(ServiceUnavailable, serviceUnavailable)
          }
        }
      }
    }
  }


  def AddRoute=pathPrefix("add") {
    pathEndOrSingleSlash {
      post {
        entity(as[AddRoute]) { (newSeq: AddRoute) =>
          onComplete(calculateETA(newSeq.newSequence)) {
            case Success(successResponse) =>
              successResponse match {
                case SuccessResponse => complete(OK)
                case ErrorResponse => complete(OK)
              }
            case Failure(_) => complete(ServiceUnavailable, serviceUnavailable)
          }
        }
      }
    }
  }

  def routeDetails: Route =
    pathPrefix("route"/Segment) { routeid =>
      pathEndOrSingleSlash {
        get {
          //GET /wall/:userId - route for fetching the dadrs to be displayed on the wall
          onComplete(getRouteDetails(routeid)) {
            case Success(x) => complete(x)
            case Failure(_) => complete(ServiceUnavailable, serviceUnavailable )
          }
        }
      }
    }

}

/**
  * Contains all the methods which serve as the starting point for all user stories of dadr
  */
trait Api extends RequestTimeout {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  // Creates actor to subscribe and log exception messages
  val errorLogActor: ActorRef = system.actorOf(ErrorLogActor.props, ErrorLogActor.name)

  // Subscribing to ExceptionMessage
  system.eventStream.subscribe(errorLogActor, classOf[ExceptionMessage])

  // Creates dadrbizsupervisor actor
  lazy val ETASupervisorActor: ActorRef = system.actorOf(Props[ETASupervisorActor])

  /**
    * Sends message to UserBizSupervisorActor for registering a new user
    *
    * @param userId   UserId of the user whose wall has to be displayed
    * @return
    */
  def getRouteDetails(userId: String): Future[List[RouteDetails]] = {
    ETASupervisorActor.ask(RouteIdForDetail(userId)).mapTo[List[RouteDetails]]
  }
  def calculateETA(newSeq:List[String])={
    ETASupervisorActor.ask(AddRoute(newSeq))
  }
  def calculateETADel(newSeq:List[String])={
    ETASupervisorActor.ask(DeleteRoute(newSeq))
  }

}
