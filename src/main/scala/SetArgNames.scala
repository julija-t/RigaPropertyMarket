import org.sellmerfud.optparse.OptionParser

object SetArgNames {

  final case class Config(filePath: String = "./src/resources/property_market.csv",
                          destName: String = "./src/resources/property_market_report.txt",
                          dbName: String = "property_market.db",
                          date: String = "noDate"
                         )
  val commandLineInterface: OptionParser[Config] = new OptionParser[Config] {
    optl[String]("-f", "--filePath", "") { (v, c) => c.copy(filePath = v getOrElse "")}
    optl[String]("-s", "--destName", "") { (v, c) => c.copy(destName = v getOrElse "")}
    optl[String]("-b", "--dbName", "") { (v, c) => c.copy(dbName = v getOrElse "")}
    optl[String]("-d", "--date", "") { (v, c) => c.copy(date = v getOrElse "")}
  }
}
