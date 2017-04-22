package actor

import akka.actor.{Actor, ActorLogging, Props}
import bizmodel.{RouteIdForDetail, Subscription}
import presentation.RequestTimeout

/**
  * Created by Akshay on 4/22/2017.
  */
object SubscribeActor {

  val name = "subscription"


  def props(): Props = Props(new SubscribeActor)

}

class SubscribeActor() extends Actor with RequestTimeout with ActorLogging {

  /**
    * Receives a case class HashTagKey and gets the dadrs for hashtag search
    */
  def receive: PartialFunction[Any, Unit] = {

    case Subscription(emailId,isEmailId,isPhoneNo) =>
      log.info("RouteId message received by the RouteDetailsActor")

  }
}
