import PropertyMarket.date

object ReportMessages {
  val dateString = date

  val message1 = s"\n\n1.TOP 5 most expensive developers in Riga by avg price per sqm EUR ($dateString) \n"

  val message2 = s"\n\n2.TOP 5 most expensive districts in Riga by avg price per sqm EUR ($dateString) \n"

  val message3 = s"\n\n3.TOP 5 most affordable districts in Riga by avg price per sqm EUR ($dateString) \n"

  val message4 = s"\n\n4.Project count and % of total by developer in Riga ($dateString) \n"

  val message5 = s"\n\n5.TOP 5 most expensive projects by average price per sqm EUR in Riga ($dateString) \n"

  val message6 = s"\n\n6.Average price per sqm EUR by number of rooms in Riga ($dateString) \n"

  val message7 = s"\n\n7.Apartment count in Riga by status and percentage of total\n"

  val message8 = "\n\n8.TOP 5 popular districts in Riga by developer count \n"

  val message9 = "\n\n9.Average price per sqm EUR in Riga by months \n"

  val message10 = "\n\n10.Average price per sqm EUR outside Riga by months \n"

  val messages = Seq(message1, message2, message3, message4, message5, message6, message7, message8, message9, message10)

}
