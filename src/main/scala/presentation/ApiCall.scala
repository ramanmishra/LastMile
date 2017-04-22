package presentation

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import bizmodel.DirectionCall
import spray.json.DefaultJsonProtocol
import spray.json._
import scala.collection.immutable.Seq

/**
  * Created by DELL on 23-04-2017.
  */
class ApiCall(implicit actorSystem:ActorSystem) extends DefaultJsonProtocol {
  private implicit val exampleFormat = jsonFormat5(DirectionCall)
  private implicit val materializer = ActorMaterializer()
  val endpoint="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcn_eStJLn7qMv7YOW9WcdWHwMRRQpW8g&callback=initMap"

  def createRequest(directionCall: DirectionCall): HttpRequest =
    HttpRequest(
      method = HttpMethods.POST,
      uri = endpoint,
      entity = HttpEntity(ContentTypes.`application/json`, directionCall.toJson.toString),
      headers = Seq()
    )
  Http().singleRequest(createRequest(DirectionCall("pune","mumbai",List("khandala","panwel"),true,"DRIVING")))
}
