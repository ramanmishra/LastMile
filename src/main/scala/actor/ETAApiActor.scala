package actor

import akka.actor.{Actor, ActorLogging, Props}
import bizmodel.EtaCalc
import com.mongodb.casbah.MongoClient
import presentation.RequestTimeout
import akka.pattern._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.util.parsing.json.JSON
import scala.io.Source


/**
  * Created by Akshay on 4/22/2017.
  */

class ETAApiActor() extends Actor with RequestTimeout with ActorLogging {

  /**
    * Receives a case class HashTagKey and gets the dadrs for hashtag search
    */
  def receive: PartialFunction[Any, Unit] = {

    case EtaCalc(uri) =>
      log.info("RouteId message received by the RouteDetailsActor")

      println(uri)
      val html = JSON.parseFull(Source.fromURL(uri).mkString)

      println(html)
      val a: List[List[Double]] = html match {
        // Matches if jsonStr is valid JSON and represents a Map of Strings to Any
        case Some(map: Map[String, List[Map[String, List[Map[String, Map[String, Double]]]]]]) =>
          map("rows").map(row => {
            row("elements").map(elem => {
              elem("distance")("value")
            })
          })
      }

      println(a)

      Future{a} pipeTo sender()
  }
}

