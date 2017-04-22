package actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import bizmodel.RouteIdForDetail
import presentation.RequestTimeout


/**
  * Created by Akshay on 4/22/2017.
  */


/**
  * Forwards the messages to the specific business actors
  */
class ETASupervisorActor
  extends Actor with RequestTimeout with ActorLogging {

  //Creating routeDetailsActor actor
  lazy val routeDetailsActor: ActorRef = context.actorOf(Props[RouteDetailsActor])


  /**
    * Receives case class messages and forwards the messages to the specific actors
    */
  def receive: PartialFunction[Any, Unit] = {

    // Forwards the message to hashsearchbiz actor if the message is of type HashTagKey
    case routeIdForDetail: RouteIdForDetail =>
      log.info("RouteID received")
      routeDetailsActor.forward(routeIdForDetail)
  }
}
