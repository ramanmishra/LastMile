package presentation

import akka.util.Timeout

import scala.util.Try

/**
  * Used for setting the timeout for the application
  */
trait RequestTimeout {

  import com.typesafe.config.ConfigFactory

  import scala.concurrent.duration._

  implicit val timeout: Timeout = {
    val t = Try(ConfigFactory.load().getString("akka.http.server.request-timeout")).getOrElse("30s")
    val d = Duration(t)
    Timeout(FiniteDuration(d.length, d.unit))
  }
}
