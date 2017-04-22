package dbmodel

/**
  * AddDadrBoxUser is a db model used for registering a new user
  *
  * @param userId      UserId of the user who is registering
  * @param emailId     EmailId of the user
  * @param name        Name of the user
  * @param description Description of the user
  * @param avatar      Avatar of the user
  * @param password    Password of the user
  * @param country     Country of the user
  * @param wallpaper   Wallpaper of the user
  */
case class AddDadrBoxUser(userId: String, emailId: String, name: String, description: String,
                          avatar: String, password: String, country: String, wallpaper: String) extends DBWriteModel {

  def getQuery: String = {
    "BEGIN BATCH INSERT INTO User (UserId,EmailId,Name,Description,Avatar,Password,Country,Wallpaper,JoinedDate) VALUES ('@" +
      userId.toLowerCase + "', '" + emailId.toLowerCase + "', '" + name + "', '" + description + "', '" + avatar + "', '" + password + "', '" +
      country + "', '" + wallpaper + "', '" + java.time.LocalDate.now.toString +
      "'); INSERT INTO UserByEmail (EmailId,UserId) VALUES ('" +
      emailId.toLowerCase + "', '@" + userId.toLowerCase + "'); APPLY BATCH"
  }

}
