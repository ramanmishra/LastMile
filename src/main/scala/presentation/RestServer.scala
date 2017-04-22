package presentation

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

/**
  * Start the HTTP server for the microservice at a specific port
  */
object RestServer extends App
  with RequestTimeout with CorsSupport {


  // Starts Actor System
  implicit val system = ActorSystem("etasystem")

  // Gets the host and port from configuration file
  val host = system.settings.config.getString("http.host")
  val port = system.settings.config.getInt("http.port")

  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  //logger.info("Actor system created")
  val api = new RestService().routes

  //Starts the HTTP server
  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(corsHandler(api), host, port)

  var serverBinding: ServerBinding = _

  bindingFuture.onComplete {
    case Success(binding) =>
      //info(s"RestApi bound to ${binding.localAddress} ")
      serverBinding = binding
      //Kamon.start
      println(s"Server started: " + binding.localAddress)
    case Failure(exception) =>
      //error(exception.getMessage)
      system.terminate()
      //Kamon.shutdown
  }

  //Registers shutdown hook
  sys.addShutdownHook({
    //info("Unbinding HttpServer ")
    val unbindFuture = serverBinding.unbind()
    try {
      //Await.result(unbindFuture, FiniteDuration(5, TimeUnit.SECONDS))
     // info("Unbinding Http server completed")
    }
    catch {
      case NonFatal(e) =>
    //    info(s"Cannot unbind: ${e.getMessage}")
    }
   // info("shutting down materializer")
    materializer.shutdown()
   // info("Terminating system ")
    system.terminate()
   // info("shutdown hook executed successfully")
   // Kamon.shutdown
  })
}


