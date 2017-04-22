package dbmodel

/**
  * Case classes inheriting this trait can be used for database operations.
  */
trait DBModel {
  /**
    * Creates a query that can be executed against cassandra database
    *
    * @return CQL query
    */
  def getQuery: String
}

/**
  * Case classes inheriting this trait can be used for database read operations
  */
trait DBReadModel extends DBModel {
  /**
    * Creates a transformer that extracts information from resultset and creates
    * a list of [[bizmodel.BusinessModel]] objects
    *
    * @return Case object which extends Transformer trait
    */
}

/**
  * Case classes inheriting this trait can be used for database write operations.
  */
trait DBWriteModel extends DBModel
