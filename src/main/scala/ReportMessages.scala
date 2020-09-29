import PropertyMarket.date

object ReportMessages {
  val dateString: String = date

  val message1: String = s"\n\n1.TOP 5 most expensive developers in Riga by avg price per sqm EUR ($dateString) \n"

  val message2: String = s"\n\n2.TOP 5 most affordable developers in Riga by avg price per sqm EUR ($dateString) \n"

  val message3: String  = s"\n\n3.TOP 5 most expensive districts in Riga by avg price per sqm EUR ($dateString) \n"

  val message4: String  = s"\n\n4.TOP 5 most affordable districts in Riga by avg price per sqm EUR ($dateString) \n"

  val message5: String  = s"\n\n5.Project count and % of total by developer in Riga ($dateString) \n"

  val message6: String  = s"\n\n6.Distribution of projects by average price per sqm EUR in Riga ($dateString) \n"

  val message7: String  = s"\n\n7.Average price per sqm EUR by number of rooms in Riga ($dateString) \n"

  val message8: String  = s"\n\n8.Apartment count in Riga by status and percentage of total\n"

  val message9: String  = "\n\n9.TOP 5 popular districts in Riga by developer count \n"

  val message10: String  = "\n\n10.Average price per sqm EUR in Riga by months \n"

  val message11: String  = "\n\n11.Average price per sqm EUR outside Riga by months \n"

  val messages: Seq[String]  = Seq(message1, message2, message3, message4, message5,
    message6, message7, message8, message9, message10, message11)

}
