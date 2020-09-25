object SQLqueries {

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

  val sql1 =
    """
      |SELECT developer,round(AVG(price_per_sqm),2) AS AVG_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY developer
      |ORDER BY AVG_price_by_sqm DESC
      |LIMIT 5
      |""".stripMargin

  val sql2 =
    """
      |SELECT district, round(AVG(price_per_sqm),2) AS AVG_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY district
      |ORDER BY AVG_price_by_sqm DESC
      |LIMIT 5;
      |""".stripMargin

  val sql3 =
    """
      |SELECT district, round(AVG(price_per_sqm),2) AS AVG_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY district
      |ORDER BY AVG_price_by_sqm
      |LIMIT 5;
      |""".stripMargin

  val sql4 =
    """
      |SELECT developer,count(DISTINCT project_name) as pr_Count,Round(((count(DISTINCT project_name)*1.0)/(SELECT COUNT(DISTINCT project_name) from property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21")*100),2) as percentage
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY developer
      |ORDER BY percentage DESC
      |""".stripMargin

  val sql5 =
    """
      |SELECT project_name, round(AVG(price_per_sqm),2) as Avg_price_per_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY project_name
      |ORDER BY Avg_price_per_sqm DESC
      |LIMIT 5
      |""".stripMargin

  val sql6 =
    """
      |SELECT number_of_rooms, round(avg(price_per_sqm),0) as avg_price_per_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY number_of_rooms
      |ORDER BY avg_price_per_sqm DESC
      |""".stripMargin

  val sql7 =
    """
      |SELECT status, COUNT(id) as apartm_count, round(((count(id)*1.0)/(SELECT COUNT(id) FROM property_market_riga pmr WHERE property_type ="apartments" AND price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21" )*100),2) as percentage
      |FROM property_market_riga pmr
      |WHERE property_type ="apartments" AND price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY status
      |ORDER BY percentage DESC ;
      |""".stripMargin

  val sql8 =
    """
      |SELECT district, COUNT(DISTINCT developer) AS dev_count
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0 AND date ="2020-09-21"
      |GROUP BY district
      |ORDER BY dev_count desc
      |LIMIT 5
      |""".stripMargin

  val sql9 =
    """
      |SELECT strftime('%m', date) as Month, round(AVG(price_per_sqm),2) as Avg_price_per_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city ="Rīga" AND size>10 AND number_of_rooms >0
      |GROUP BY month
      |""".stripMargin

  val sql10 =
    """
      |SELECT strftime('%m', date) as month, round(AVG(price_per_sqm),2) as avg_price_by_sqm
      |FROM property_market_riga pmr
      |WHERE price_per_sqm!=0 AND city <>"Rīga" AND size>10 AND number_of_rooms >0
      |GROUP BY month;
      |""".stripMargin

}
