import PropertyMarket.conn

final case class InsertDataApp() {
  /**
   * Insert rows into the riga_property_market table
   *
     * @param theSeq
   */
  def insert(theSeq: scala.collection.mutable.Buffer[PropertyAdClean]) {
    val insertSql =
      """
        |INSERT INTO property_market_riga(
        |id, project_name, developer,
        |city, district, address,
        |property_type, status, size,
        |number_of_rooms, floor, price,
        |price_per_sqm, project_link, apartment_link,
        |date)
        |VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)""".stripMargin

    val pstmt = conn.prepareStatement(insertSql) //Creates a PreparedStatement object

    for (obj <- theSeq) {
      pstmt.setString(1, obj.id)
      pstmt.setString(2, obj.project_name)
      pstmt.setString(3, obj.developer)
      pstmt.setString(4, obj.city)
      pstmt.setString(5, obj.district)
      pstmt.setString(6, obj.address)
      pstmt.setString(7, obj.property_type)
      pstmt.setString(8, obj.status)
      pstmt.setDouble(9, obj.size)
      pstmt.setInt(10, obj.number_of_rooms)
      pstmt.setInt(11, obj.floor)
      pstmt.setDouble(12, obj.price)
      pstmt.setDouble(13, obj.price_per_sqm)
      pstmt.setString(14, obj.project_link)
      pstmt.setString(15, obj.apartment_link)
      pstmt.setString(16, obj.date)
      pstmt.execute()
    }

    pstmt.close()
  }

}
