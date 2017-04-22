package presentation

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bizmodel.{RouteDetails, _}
import spray.json.{DefaultJsonProtocol, _}


/**
  * Trait for marshalling and unmarshalling business models to and from json
  */
trait Marshalling extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val deleteFormat=jsonFormat1(DeleteRoute)
  implicit val routeDetailsFormat=jsonFormat9(RouteDetails)
  implicit val AddRouteFormate=jsonFormat1(AddRoute)
}


