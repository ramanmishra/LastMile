package presentation

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bizmodel._
import spray.json.{DefaultJsonProtocol, _}


/**
  * Trait for marshalling and unmarshalling business models to and from json
  */
trait Marshalling extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val registerUserFormat: RootJsonFormat[RouteDetails] = jsonFormat1(RouteDetails)

}


