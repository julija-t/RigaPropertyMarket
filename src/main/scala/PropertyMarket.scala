import java.io.File
import java.sql.{DriverManager, PreparedStatement}

object PropertyMarket extends App{
  println(System.getProperty("user.dir"))

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  val fList = getListOfFiles("./")
  fList.foreach(println)
  getListOfFiles("./src").foreach(println)
  getListOfFiles("./src/resources").foreach(println)

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

  def getParsedLines(fileName:String): Seq[Seq[String]] = {
    var myListBuf = scala.collection.mutable.ListBuffer[Seq[String]]()
    val bufferedSource = io.Source.fromFile(fileName)
    for (line <- bufferedSource.getLines) {
      val splitLine = line.split(";")
      myListBuf += splitLine
    }
    bufferedSource.close
    myListBuf
  }

  def getPropertyAdSeq(splitLineSeq: Seq[Seq[String]]): Seq[PropertyAd] = {
    splitLineSeq.map(t => PropertyAd(t.head, t(1), t(2), t(3), t(4), t(5),
      t(6), t(7), t(8), t(9).toDouble, t(10), t(11).toInt, t(12).toInt,
      t(13).toDouble, t(14).toDouble, t(15), t(16), t(17), t(18)))
  }

  val url = "jdbc:sqlite:C:/sqlite/db/property_market.db"
  val conn = DriverManager.getConnection(url)
  val statement = conn.createStatement()
  val createTableSql =
    """
      |CREATE TABLE IF NOT EXISTS property_market_riga (
      |id TEXT,
      |project_name TEXT,
      |developer TEXT,
      |city TEXT,
      |district TEXT,
      |address TEXT,
      |property_type TEXT,
      |status TEXT,
      |size DOUBLE,
      |number_of_rooms INT,
      |floor INT,
      |price DOUBLE,
      |price_per_sqm DOUBLE,
      |project_link TEXT,
      |apartment_link TEXT,
      |date DATE
      |)""".stripMargin

  val insertValuesToTable =
    """
      |INSERT INTO property_market_riga VALUES(?,?)
      |""".stripMargin


  val filePath = "./src/resources/property_market_2109.csv"
  val filePath2 = "./src/resources/fails_par_2019_gadu.csv" // temporary
  val lineCount = getLineCount(filePath)
  println(s"We got a file with $lineCount lines")
  val checkingRows: Unit = checkRowLength(filePath)
//  val checkingRows2 = checkRowLength(filePath2) //temporary row for checking if "throw Exception" works fine
  val rawSplit = getParsedLines(filePath)
  val seqWithoutEmptyValues = rawSplit.map(line => line.map(el => if (el.isEmpty) 0.toString else el))
  val allPropertyAds = getPropertyAdSeq(seqWithoutEmptyValues.slice(1, seqWithoutEmptyValues.length))
  val cleansedPropertyAds = allPropertyAds.map(t => PropertyAdClean(t.id, t.project_name, t.developer, t.city,
    t.district, t.address, t.property_type, t.status, t.size, t.number_of_rooms,
    t.floor, t.price, t.price_per_sqm, t.project_link, t.apartment_link, t.date)).toBuffer
//  val latestPropertyAds = cleansedPropertyAds.filter(_.date == "2020-09-21") //temporary for checking if sequence works

  //DB part
  statement.execute(createTableSql) //Creates empty table
  val appInsert = InsertDataApp() //appInsert object adds data to the table with "insert" method
  val checkIfEmptySql =  "SELECT EXISTS(SELECT 1 FROM property_market_riga) AS Output" // Query that checks if table property_market_riga is empty
  val rs = statement.executeQuery(checkIfEmptySql)//Executes the query returns ResultSet
  // Checks the contents of ResultSet, if it's 0, calls appInsert object and inserts data
  while (rs.next) {
    val output = rs.getInt("Output")
    if (output == 0) appInsert.insert(cleansedPropertyAds)
  }

}
