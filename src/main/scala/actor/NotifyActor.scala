package actor

import akka.actor.{Actor, ActorLogging, Props}
import bizmodel.RouteIdForDetail
import presentation.RequestTimeout

/**
  * Created by Akshay on 4/22/2017.
  */
object NotifyActor {

  val name = "routedetails"

  def props(): Props =
    Props(new NotifyActor())

}


class NotifyActor() extends Actor with RequestTimeout with ActorLogging {

  /**
    * Receives a case class HashTagKey and gets the dadrs for hashtag search
    */
  def receive: PartialFunction[Any, Unit] = {

    case RouteIdForDetail(routeId) =>
      log.info("RouteId message received by the RouteDetailsActor")

      //emailNotify
    //smsNotify
  }
}
