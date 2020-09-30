import org.sellmerfud.optparse.OptionParser

/** Arguments for the program.
 * Command line interface.
 */

object SetArgNames {

  /**
   * Default configuration arguments for retrieving and saving data
   * @param filePath existing csv file location
   * @param destName destination path for saving final text report
   * @param dbName name of the new SQL database
   * @param date
   */

  final case class Config(filePath: String = "./src/resources/property_market.csv",
                          destName: String = "./src/resources/property_market_report.txt",
                          dbName: String = "property_market.db",
                          date: String = "noDate"
                         )

  /** Command line interface.
   * Also creates ability to use defined or custom configuration arguments.  */
  val commandLineInterface: OptionParser[Config] = new OptionParser[Config] {
    optl[String]("-f", "--filePath", "") { (v, c) => c.copy(filePath = v getOrElse "")}
    optl[String]("-s", "--destName", "") { (v, c) => c.copy(destName = v getOrElse "")}
    optl[String]("-b", "--dbName", "") { (v, c) => c.copy(dbName = v getOrElse "")}
    optl[String]("-d", "--date", "") { (v, c) => c.copy(date = v getOrElse "")}
  }
}
