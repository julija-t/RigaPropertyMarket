import java.io.File

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

  val f_list = getListOfFiles("./")
  f_list.foreach(println)
  getListOfFiles("./src").foreach(println)
  getListOfFiles("./src/resources").foreach(println)

  def getLineCount(fileName:String): Int = {
    var count = 0
    val bufferedSource = io.Source.fromFile(fileName)
    for (line <- bufferedSource.getLines) {
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
      println("The document has consistent number of rows. Proceeding with analysis.")
    else
      throw new Exception ("The document has inconsistent number of rows. Cannot proceed with analysis.")
  }




  val filePath = "./src/resources/property_market_2109.csv"
  val filePath2 = "./src/resources/fails_par_2019_gadu.csv" // temporary
  val lineCount = getLineCount(filePath)
  println(s"We got a file with $lineCount lines")
  val checkingRows: Unit = checkRowLength(filePath)
//  val checkingRows2 = checkRowLength(filePath2) //temporary row for checking if "throw Exception" works fine
}
