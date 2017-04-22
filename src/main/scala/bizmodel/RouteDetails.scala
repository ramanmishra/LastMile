package bizmodel

/**
  * Created by Akshay on 4/23/2017.
  */
case class RouteDetails(stopType : String, cityName : String, deliveryStreetAddress : String,
                        destState : String, pickupCity : String, driverName : String,
                        pickupStreetAddress : String,pickupState : String , destCity : String) extends BusinessModel
