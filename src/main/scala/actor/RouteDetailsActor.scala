package actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import bizmodel.RouteIdForDetail
import com.mongodb.casbah.Imports._
import presentation.RequestTimeout
import akka.pattern._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Akshay on 4/22/2017.
  */

/**
  * Contains business logic for fetching the dadrs for hashtag search
  */
class RouteDetailsActor
  extends Actor with RequestTimeout with ActorLogging {

  /**
    * Receives a case class HashTagKey and gets the dadrs for hashtag search
    */
  def receive: PartialFunction[Any, Unit] = {

    case RouteIdForDetail(routeId) =>
      log.info("RouteId message received by the RouteDetailsActor")
      val mongoClient = MongoClient("192.168.25.213", 27017) //192.168.25.213
      Future.successful("We hit the db") pipeTo sender()
      val collection = mongoClient.getDB("test").collectionNames()
      println(collection)

  }
}
