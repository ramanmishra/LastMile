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

      val a: List[Map[String, String]] = data match {
        // Matches if jsonStr is valid JSON and represents a Map of Strings to Any
        case Some(list: List[Map[String, List[Map[String, String]]]]) =>
          list.flatMap((x: Map[String, List[Map[String, String]]]) =>x("segment"))
      }


      val res: List[RouteDetails] = a.map(xyz => RouteDetails(xyz("stopType").toString, xyz("cityName").toString,
        xyz("deliveryStreetAddress").toString, xyz("destState").toString, xyz("pickupCity").toString,
        xyz("driverName").toString, xyz("pickupStreetAddress").toString, xyz("pickupState").toString,
        xyz("destCity").toString))

      println(res.map(a=>a.stopType))
      //val dataRes = res.filter(ele => ele.stopType.eq("P")).head :: res.filter(ele => ele.stopType.eq("D"))
      val dataRes: List[ModifiedRouteDetails] = if (res.filter(_.stopType.equals("P")).size > 0) {
        res.filter(_.stopType.equals("P")).map(ele => ModifiedRouteDetails(ele.pickupStreetAddress+" "+ele.pickupCity+ " "+ ele.pickupState)).head :: res.filter(_.stopType.equals("D")).map(ele => ModifiedRouteDetails(ele.deliveryStreetAddress+" "+ele.destCity+ " "+ ele.destState))
      }else {
        println(res.filter(ele => ele.stopType.eq("P")))
        res.filter(_.stopType.equals("D")).map(ele => ModifiedRouteDetails(ele.deliveryStreetAddress+" "+ele.destCity+ " "+ ele.destState))
      }

      val dividedList: List[List[ModifiedRouteDetails]] = dataRes.grouped(8).toList
println(dividedList)
      val count = dividedList.count(_ => true)
      val matrix: List[List[(ModifiedRouteDetails, ModifiedRouteDetails)]] =
        dividedList.flatMap(a => dividedList.map(b => {
        a zip b
      }))

      val apicall = matrix.map(a => a.map(b =>
        etaSupervisorActor.ask(EtaCalc("https://maps.googleapis.com/maps/api/distancematrix/json?origins="
          + b._1.address.mkString("|").replaceAll(" ", "+")
          + "&destinations="
          + b._2.address.mkString("|").replaceAll(" ", "+")
          + "&mode=car&language=en-EN&key=AIzaSyBcn_eStJLn7qMv7YOW9WcdWHwMRRQpW8g")).mapTo[List[List[Double]]]))

      apicall.map(e => println(e))


    case AddRoute(newSequence)=>
      log.info(("Add route received by actor"))
      //call eta calculate
    case DeleteRoute(newSequence)=>
      log.info("delete route received by actor")
      //call eta calculate

  }
}
