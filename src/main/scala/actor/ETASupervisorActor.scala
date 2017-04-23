package actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.FromConfig
import bizmodel.{EtaCalc, RouteIdForDetail}
import presentation.RequestTimeout

class ETASupervisorActor()
  extends Actor with RequestTimeout with ActorLogging {



  //Creating hashsearchbiz actor
  lazy val routeDetailsActor: ActorRef = context.actorOf(Props[RouteDetailsActor])

  lazy val etaApiActor: ActorRef = context.actorOf(Props[ETAApiActor])


  /**
    * Receives case class messages and forwards the messages to the specific actors
    */
  def receive: PartialFunction[Any, Unit] = {

    // Forwards the message to hashsearchbiz actor if the message is of type HashTagKey
    case routeIdForDetail: RouteIdForDetail =>
      log.info("RouteID received")
      routeDetailsActor.forward(routeIdForDetail)

    case etaCalc: EtaCalc =>
      log.info("URI received")
      etaApiActor.forward(etaCalc)
  }
}
