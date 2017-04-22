package actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import bizmodel._
import com.mongodb.casbah.Imports._
import presentation.RequestTimeout
import akka.pattern._
import spray.json.DefaultJsonProtocol.jsonFormat3
import spray.json._
import DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try
import scala.util.parsing.json.JSON

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

      lazy val etaSupervisorActor: ActorRef = context.actorOf(Props[ETASupervisorActor])

      val mongoClient = MongoClient("192.168.25.213", 27017)
      val collection = mongoClient.getDB("test").collectionNames()
      val data = JSON.parseFull(mongoClient.getDB("test").getCollection("route").find(new BasicDBObject("_id", "NSNLAXB12TH042017")).toArray().toString)

      println(data)
      val a: List[Map[String, String]] = data match {
        // Matches if jsonStr is valid JSON and represents a Map of Strings to Any
        case Some(list: List[Map[String, List[Map[String, String]]]]) =>
          list.flatMap((x: Map[String, List[Map[String, String]]]) =>x("segment"))
      }


      val res: List[RouteDetails] = a.map(xyz => RouteDetails(xyz("stopType").toString, xyz("cityName").toString,
        xyz("deliveryStreetAddress").toString, xyz("destState").toString, xyz("pickupCity").toString,
        xyz("driverName").toString, xyz("pickupStreetAddress").toString, xyz("pickupState").toString,
        xyz("destCity").toString))

      //val dataRes = res.filter(ele => ele.stopType.eq("P")).head :: res.filter(ele => ele.stopType.eq("D"))
      val dataRes: List[RouteDetails] = if (res.filter(ele => ele.stopType.eq("P")).size > 0) {
        res.filter(ele => ele.stopType.eq("P")).head :: res.filter(ele => ele.stopType.eq("D"))
      }else {
        res.filter(ele => ele.stopType.eq("D"))
      }

      val dividedList: List[List[RouteDetails]] = dataRes.grouped(8).toList

      val count = dividedList.count(_ => true)

      val matrix: Future[List[List[List[Double]]]] = Future.sequence(dividedList.flatMap(a => dividedList.map(b => {
        etaSupervisorActor.ask(EtaCalc("https://maps.googleapis.com/maps/api/distancematrix/json?origins="
          + a.mkString("|").replaceAll(" ", "+")
          + "&destinations="
          + b.mkString("|").replaceAll(" ", "+")
          + "&mode=car&language=en-EN&key=AIzaSyCSI6Ee30NrSFKj62IDzcZuf71UskQCEDM")).mapTo[List[List[Double]]]
      })))

      matrix.map(e => println(e))


    case AddRoute(newSequence)=>
      log.info(("Add route received by actor"))
      //call eta calculate
    case DeleteRoute(newSequence)=>
      log.info("delete route received by actor")
      //call eta calculate

  }
}
