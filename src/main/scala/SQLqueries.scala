object SQLqueries {

  /** SQL queries:
   * Creating SQL table
   * Inserting data in table
   * Querying database.
   */

  /** Creates SQL table and defines data types */
  val createTableSql: String =
    """
      |CREATE TABLE IF NOT EXISTS property_market_riga (
      |ad_id INTEGER PRIMARY KEY,
      |property_id TEXT,
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

  /** Insert data into SQL table  */
  val insertSql: String =
    """
      |INSERT INTO property_market_riga(
      |property_id, project_name, developer,
      |city, district, address,
      |property_type, status, size,
      |number_of_rooms, floor, price,
      |price_per_sqm, project_link, apartment_link,
      |date)
      |VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)""".stripMargin

  /** Latest date in SQL database */
  val latestDateSql = "SELECT MAX(date) FROM property_market_riga;"

  /** Multiple SQL queries for the final report */
  val sql1: String  =
    """
      |SELECT developer,round(AVG(price_per_sqm),2) AS avg_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY developer
      |ORDER BY AVG_price_by_sqm DESC
      |LIMIT 5
      |""".stripMargin

  val sql2: String  =
    """
      |SELECT developer,round(AVG(price_per_sqm),2) AS avg_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY developer
      |ORDER BY AVG_price_by_sqm
      |LIMIT 5
      |""".stripMargin

  val sql3: String  =
    """
      |SELECT district, round(AVG(price_per_sqm),2) AS avg_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY district
      |ORDER BY AVG_price_by_sqm DESC
      |LIMIT 5;
      |""".stripMargin

  val sql4: String  =
    """
      |SELECT district, round(AVG(price_per_sqm),2) AS avg_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY district
      |ORDER BY AVG_price_by_sqm
      |LIMIT 5;
      |""".stripMargin

  val sql5: String  =
    """
      |SELECT developer,count(DISTINCT project_name) as pr_Count,Round(((count(DISTINCT project_name)*1.0)/(SELECT COUNT(DISTINCT project_name) from property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date)*100),2) as percentage
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY developer
      |ORDER BY percentage DESC
      |""".stripMargin


  val sql6: String  =
    """
      |SELECT project_name, round(AVG(price_per_sqm),2) as avg_price_per_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY project_name
      |ORDER BY Avg_price_per_sqm DESC
      |""".stripMargin

  val sql7: String  =
    """
      |SELECT number_of_rooms, round(avg(price_per_sqm),0) as avg_price_per_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY number_of_rooms
      |ORDER BY avg_price_per_sqm DESC
      |""".stripMargin

  val sql8: String  =
    """
      |SELECT status, COUNT(property_id) as apartm_count, round(((count(property_id)*1.0)/(SELECT COUNT(property_id) FROM property_market_riga pmr WHERE property_type ="apartments" AND price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date )*100),2) as percentage
      |FROM property_market_riga pmr
      |WHERE property_type ="apartments" AND price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY status
      |ORDER BY percentage DESC ;
      |""".stripMargin

  val sql9: String  =
    """
      |SELECT district, COUNT(DISTINCT developer) AS dev_count
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date = :date
      |GROUP BY district
      |ORDER BY dev_count DESC
      |LIMIT 5
      |""".stripMargin

  val sql10: String  =
    """
      |SELECT strftime('%m', date) as month, round(AVG(price_per_sqm),2) as avg_price_per_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0
      |GROUP BY month
      |ORDER BY month
      |""".stripMargin

  val sql11: String  =
    """
      |SELECT strftime('%m', date) as month, round(AVG(price_per_sqm),2) as avg_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city <>"Rīga" AND size>10 AND number_of_rooms >0
      |GROUP BY month
      |ORDER BY month
      |""".stripMargin

  /** Sequence of all SQL query values/results  */
  val sqls: Seq[String] = Seq(sql1, sql2, sql3, sql4, sql5, sql6, sql7, sql8, sql9, sql10, sql11)

}
