import java.sql.{DriverManager, ResultSet}
import SQLqueries._
import scala.StringContext.InvalidEscapeException
import scala.collection.mutable.ListBuffer

object PropertyMarket extends App{
  val filePath = if(args.nonEmpty) args(0) else "./src/resources/property_market_2109.csv"
  val dbname = if(args.nonEmpty) args(1) else "property_market.db"
  val url = if(args.nonEmpty) args(2) else getDbUrl() //url where the database is located


  //Counts number of lines in a file
  def getLineCount(fileName:String): Int = {
    var count = 0
    val bufferedSource = io.Source.fromFile(fileName)
    for (_ <- bufferedSource.getLines) {
      count += 1
    }
    bufferedSource.close
    count
  }

  //Checks if all rows have the same length, otherwise throws an Exception
  def checkRowLength(fileName:String): Unit = {
    var myListBuf = scala.collection.mutable.ListBuffer[Int]()
    val bufferedSource = io.Source.fromFile(fileName)
    for (line <- bufferedSource.getLines) {
      val splitLine = line.split(";")
      myListBuf += splitLine.size
    }
    bufferedSource.close
    if(myListBuf.max == myListBuf.min)
      println("The document has consistent row lengths. Proceeding with analysis...")
    else
      throw new Exception ("The document has inconsistent row lengths. Cannot proceed with analysis.")
  }

  //Gets sequence of lines from a file
  def getParsedLines(fileName:String): Seq[Seq[String]] = {
    var myListBuf = scala.collection.mutable.ListBuffer[Seq[String]]()
    val bufferedSource = io.Source.fromFile(fileName)
    for (line <- bufferedSource.getLines) {
      val editedLine = "1;" ++ line
      val splitLine = editedLine.split(";")
      myListBuf += splitLine
    }
    bufferedSource.close
    myListBuf
  }

  //Gets sequence of PropertyAd objects
  def getPropertyAdSeq(splitLineSeq: Seq[Seq[String]]): Seq[PropertyAd] = {
    splitLineSeq.map(t => PropertyAd(t.head.toInt, t(1), t(2), t(3), t(4), t(5),
      t(6), t(7), t(8), t(9), t(10).toDouble, t(11), t(12).toInt,
      t(13).toInt, t(14).toDouble, t(15).toDouble, t(16), t(17), t(18), t(19)))
  }

  //Connects to database
  def getConnection(url: String) = {
    val conn = DriverManager.getConnection(url)
    conn}

  //Gets url where database is located
  def getDbUrl() = {
    val environmentVars = System.getenv()
    val sqlite_home = environmentVars.get("SQLITE_HOME")
    var url = ""
    try {
      url = s"jdbc:sqlite:$sqlite_home\db\$dbname"
    } catch {
      case e: InvalidEscapeException => url = s"jdbc:sqlite:$sqlite_home//db//$dbname"
    }
    url
  }

  //Converts ResultSet to HashMap
  def sqlToHashMap(sql: String) = {
    val rs: ResultSet = statement.executeQuery(sql)
    val myHashMap =
      Iterator
      .continually(rs.next)
      .takeWhile(identity)
      .map { _ => rs.getString(1) -> rs.getString(2)}
      .toMap

    myHashMap
  }

  def getDate(sql: String) = {
    val rs = statement.executeQuery(sql)
    val date = rs.getString(1).mkString
    date
  }

  //Prints to console: query results with date specified as parameter
  def printToConsole(sql: String, date: String): Unit = {
    def getResultSet() = {
      val pstmt = conn.prepareStatement(sql)
      var resultSet = pstmt.executeQuery()
      if (sql.count(i => i == '?') == 0) {
        resultSet = statement.executeQuery(sql)
      } else if (sql.count(i => i == '?') == 1) {
        pstmt.setString(1, date)
        resultSet = pstmt.executeQuery()
      } else if (sql.count(i => i == '?') == 2) {
        pstmt.setString(1, date)
        pstmt.setString(2, date)
        resultSet = pstmt.executeQuery()
      }
      resultSet
    }
    val resultSet = getResultSet()
    val meta = resultSet.getMetaData
    var colSeq = ListBuffer[String]()
    for (i <- 1 to
      meta.getColumnCount) {
      print(meta.getColumnName(i) + " " * 25)
      colSeq += meta.getColumnName(i)
  }
    while (resultSet.next()) {
      println()
      colSeq.foreach(col => print(resultSet.getString(col) + " " * (39- resultSet.getString(col).length)))
    }
  }

  //Prints to console: finished report
  def printReport(date: String): Unit = {
    val dateString = date
    println(s"Riga real estate report: $dateString")
    println(Console.BOLD + s"\n\n1.TOP 5 most expensive developers in Riga by avg price per sqm EUR ($dateString) \n" + Console.RESET)
    printToConsole(sql1, dateString)
    println(Console.BOLD + s"\n\n2.TOP 5 most expensive districts in Riga by avg price per sqm EUR ($dateString) \n" + Console.RESET)
    printToConsole(sql2, dateString)
    println(Console.BOLD + s"\n\n3.TOP 5 most affordable districts in Riga by avg price per sqm EUR ($dateString) \n" + Console.RESET)
    printToConsole(sql3, dateString)
    println(Console.BOLD + s"\n\n4.Project count and % of total by developer in Riga ($dateString) \n" + Console.RESET)
    printToConsole(sql4, dateString)
    println(Console.BOLD + s"\n\n5.TOP 5 most expensive projects by average price per sqm EUR in Riga ($dateString) \n" + Console.RESET)
    printToConsole(sql5, dateString)
    println(Console.BOLD + s"\n\n6.Average price per sqm EUR by number of rooms in Riga ($dateString) \n" + Console.RESET)
    printToConsole(sql6, dateString)
    println(Console.BOLD + s"\n\n7.Apartment count in Riga by status and percentage of total\n" + Console.RESET)
    printToConsole(sql7, dateString)
    println(Console.BOLD + "\n\n8.TOP 5 popular districts in Riga by developer count \n" + Console.RESET)
    printToConsole(sql8, dateString)
    println(Console.BOLD + "\n\n9.Average price per sqm EUR in Riga by months \n" + Console.RESET)
    printToConsole(sql9, dateString)
    println(Console.BOLD + "\n\n10.Average price per sqm EUR outside Riga by months \n" + Console.RESET)
    printToConsole(sql10, dateString)
  }




  //CSV part
  val lineCount = getLineCount(filePath)
  println(s"We got a file with $lineCount lines")
  val checkingRows: Unit = checkRowLength(filePath)
  val rawSplit = getParsedLines(filePath)
  val seqWithoutEmptyValues = rawSplit.map(line => line.map(el => if (el.isEmpty) 0.toString else el))
  val allPropertyAds = getPropertyAdSeq(seqWithoutEmptyValues.slice(1, seqWithoutEmptyValues.length))
  val cleansedPropertyAds = allPropertyAds.map(t => PropertyAdClean(t.ad_id, t.property_id, t.project_name, t.developer, t.city,
    t.district, t.address, t.property_type, t.status, t.size, t.number_of_rooms,
    t.floor, t.price, t.price_per_sqm, t.project_link, t.apartment_link, t.date)).toBuffer


  //DB part
  val conn = getConnection(url)
  val statement = conn.createStatement
  statement.execute(createTableSql) //Creates empty table
  val appInsert = InsertDataApp() //appInsert object adds data to the table with "insert" method
  val checkIfEmptySql =  "SELECT EXISTS(SELECT 1 FROM property_market_riga) AS Output" // Checks if table property_market_riga is empty
  val rs = statement.executeQuery(checkIfEmptySql)//Executes the query returns ResultSet
  // Checks the contents of ResultSet, if it's 0, calls appInsert object and inserts data
  while (rs.next) {
    val output = rs.getInt("Output")
    if (output == 0) appInsert.insertIntoDb(cleansedPropertyAds)
  }
  val latestDate = getDate(latestDateSql)
  printReport(latestDate)

}
