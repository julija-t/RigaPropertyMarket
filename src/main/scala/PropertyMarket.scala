import java.io.FileWriter
import java.sql.{DriverManager, ResultSet}

import SQLqueries._
import ReportMessages._

import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer


object PropertyMarket extends App{
  val filePath = if(args.nonEmpty) args(0) else "./src/resources/property_market_2109.csv"
  val destName = if(args.nonEmpty) args(1) else "./src/resources/property_market_2109_report.txt"
  val dbname = if(args.nonEmpty) args(2) else "property_market.db"
  val url = if(args.nonEmpty) args(3) else getDbUrl() //url where the database is located
  lazy val date = if(args.nonEmpty) args(4) else getDate(latestDateSql)


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
    val osName = System.getProperty("os.name").toLowerCase()
    val isWindowsOs = osName.startsWith("windows")
    var url = ""
    if (isWindowsOs) {
      url = s"jdbc:sqlite:$sqlite_home\\db\\$dbname"
    }
    else { url =  s"jdbc:sqlite:$sqlite_home/db/$dbname"
    }
    url
  }

  //Converts ResultSet to HashMap
  def sqlToListMap(sql: String, date: String = date): ListMap[String, String] = {
    val rs: ResultSet = getResultSetApp().getResultSet(sql, date)
    val nrOfColumns = rs.getMetaData.getColumnCount

    var columnNamesMap = Map[String, String]()
    var myHashMap = Map[String, String]()

      if (nrOfColumns == 2) {
        columnNamesMap = Map(rs.getMetaData.getColumnName(1) -> rs.getMetaData.getColumnName(2))
        myHashMap =
          Iterator
          .continually(rs.next)
          .takeWhile(identity)
          .map { _ => rs.getString(1) -> rs.getString(2)}
          .toMap
      } else if (nrOfColumns == 3) {
        columnNamesMap = Map(rs.getMetaData.getColumnName(1) -> rs.getMetaData.getColumnName(3))
        myHashMap =
          Iterator
          .continually(rs.next)
          .takeWhile(identity)
          .map { _ => rs.getString(1) -> rs.getString(3)}
          .toMap
      }
    val concatMap = columnNamesMap.++(myHashMap)
    val sortedMap = ListMap(concatMap.toSeq.sortWith(_._2 > _._2):_*)
    sortedMap
  }

  def getDate(sql: String) = {
    val rs = statement.executeQuery(sql)
    val date = rs.getString(1).mkString
    date
  }

  //Prints to console: query results with date specified as parameter
  def printToConsole(sql: String, date: String = date): Unit = {
    val rs: ResultSet = getResultSetApp().getResultSet(sql, date)
    val meta = rs.getMetaData
    var colSeq = ListBuffer[String]()
    for (i <- 1 to
      meta.getColumnCount) {
      print(meta.getColumnName(i) + " " * 25)
      colSeq += meta.getColumnName(i)
  }
    while (rs.next()) {
      println()
      colSeq.foreach(col => print(rs.getString(col) + " " * (39- rs.getString(col).length)))
    }
    println()
  }

  //Prints to console: finished report
  def printReport(date: String = date): Unit = {
    var messagesSeq = messages
    println("Displaying report in console")
    println((s"\n\nRiga real estate report: $date").toUpperCase)

    def toConsole (sql: String) = {
      println(Console.BOLD + messagesSeq.head + Console.RESET)
      printToConsole(sql, dateString)
      messagesSeq = messagesSeq.drop(1)
    }

    sqls.foreach(sql => toConsole(sql))
  }

  def saveReport(destName: String = destName, sqls: Seq[String] = sqls, messages: Seq[String] = messages): Unit = {
    println(s"\nSaving Report to file $destName")
    val fw = new FileWriter(destName, false)
    val reportName = s"Riga real estate report: $date".toUpperCase
    fw.write(reportName)
    var sqlSeq = sqls

    def writeMap(message: String) = {
      val myMap = sqlToListMap(sqlSeq.head, date)
      fw.write(message)
      fw.write("\n")
      val changedMap = myMap.map({case (k,v) => k + " " * (40 - k.length) + v + "\n"}).mkString
      fw.write(changedMap)
      sqlSeq = sqlSeq.drop(1)
    }

    messages.foreach(message => writeMap(message))

    fw.close()
    println(s"All done processing file $filePath into $destName.")
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
  printReport()
  saveReport()

}
