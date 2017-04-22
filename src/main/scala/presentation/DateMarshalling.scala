package presentation

import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.scalalogging.LazyLogging
import spray.json.{JsString, JsValue, JsonFormat}

/**
  * Object for marshalling and unmarshalling java.util.date to and from json
  */
object DateMarshalling extends LazyLogging {

  /**
    * Object created for overriding read and write methods for marshalling and unmarshalling
    */
  implicit object DateFormat extends JsonFormat[Date] {

    /**
      * Converts
      * @param date value of java.util.Date
      * @return     Return value of type JsValue
      */
    def write(date: Date): JsValue = JsString(date.toString)

    /**
      *
      * @param json Json
      * @return return value
      */
    def read(json: JsValue): Date = {
      val date: Date = localIsoDateFormatter.get().parse(json.toString())
      logger.info("Date from JSON Read {}", date)
      date
    }
  }


  private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {

    override def initialValue() = new SimpleDateFormat("EEE MMM d hh:mm:ss zzz yyyy")
  }
}
