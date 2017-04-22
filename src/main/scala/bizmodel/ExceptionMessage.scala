package bizmodel

/**
  * ExceptionMessage class is used for publishing and subscribing to log exception messages separately
  *
  * @param message  The message which caused exception
  */
case class ExceptionMessage(message: String) extends BusinessModel
