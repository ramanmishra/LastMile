package presentation

/**
  * A marker trait for all responses to REST Apis
  */
trait RestResponse

/**
  * A singleton object that indicates successful processing of a REST service
  */
case object SuccessResponse extends RestResponse

/**
  * A singleton object that indicates erroneous processing of a REST service
  */
case object ErrorResponse extends RestResponse
