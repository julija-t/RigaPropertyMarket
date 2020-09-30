import PropertyMarket.date

/** SQL query messages/titles for the final text report*/


object ReportMessages {

  /** Retrieves date from PropertyMarket.scala*/
  val dateString: String = date

  /** Messages/titles of all SQL queries for the final text file report  */
  val message1: String = s"\n\n1.TOP 5 most expensive developers in Riga by avg price per sqm EUR ($dateString) \n"

  val message2: String = s"\n\n2.TOP 5 most affordable developers in Riga by avg price per sqm EUR ($dateString) \n"

  val message3: String  = s"\n\n3.TOP 5 most expensive districts in Riga by avg price per sqm EUR ($dateString) \n"

  val message4: String  = s"\n\n4.TOP 5 most affordable districts in Riga by avg price per sqm EUR ($dateString) \n"

  val message5: String  = s"\n\n5.Developer share by project count in Riga, percentage of total project count ($dateString) \n"

  val message6: String  = s"\n\n6.Distribution of projects by average price per sqm EUR in Riga ($dateString) \n"

  val message7: String  = s"\n\n7.Average price per sqm EUR by number of rooms in Riga ($dateString) \n"

  val message8: String  = s"\n\n8.Apartment count in Riga by status and percentage of total ($dateString) \n"

  val message9: String  = s"\n\n9.TOP 5 popular districts in Riga by developer count ($dateString) \n"

  val message10: String  = "\n\n10.Average price per sqm EUR in Riga by months \n"

  val message11: String  = "\n\n11.Average price per sqm EUR outside Riga by months \n"

  /** Sequence of all report messages*/
  val messages: Seq[String]  = Seq(message1, message2, message3, message4, message5,
    message6, message7, message8, message9, message10, message11)

}
