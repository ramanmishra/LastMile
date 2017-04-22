package actor

import akka.actor.{Actor, ActorLogging, Props}
import bizmodel.ExceptionMessage
import presentation.RequestTimeout

/**
  * Companion object for ErrorLogActor contains Props for actor creation
  *
  */
object ErrorLogActor {

  val name = "errorlogactor"

  /**
    * A factory method to create Props for the Actor
    *
    * @return A configuration class of type Props which is used for Actor creation
    */
  def props: Props = Props(new ErrorLogActor)

}

/**
  * Contains logic to subscribe to error messages and logs in error file
  *
  */
class ErrorLogActor extends Actor with RequestTimeout with ActorLogging {

  def receive: PartialFunction[Any, Unit] = {
    case ExceptionMessage(message) =>
      log.warning(message)
  }
}

