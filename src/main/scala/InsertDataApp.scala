import java.sql.PreparedStatement
import PropertyMarket.conn
import SQLqueries.insertSql

/** Functionality to insert data in SQL database */

final case class InsertDataApp() {


  /** Inserts rows into  SQL db table */
  def insertIntoDb(theSeq: scala.collection.mutable.Buffer[PropertyAdClean]){

    /** Set setAutoCommit to false : data is not written line by line in db. It's a slow process */
    conn.setAutoCommit(false)

    /**Creates PreparedStatement object using SQL insert query*/
    val pstmt = conn.prepareStatement(insertSql)

    /** Links db columns with defined cleaned data objects
     * @param pstmt creates prepared statements for inserting data in db
     * @param propertyAd links propertyAd objects with cleaned PropertyAdClean objects
     */
    def setPropertyAd (pstmt: PreparedStatement, propertyAd: PropertyAdClean ): Unit = {
      pstmt.setString(1, propertyAd.property_id)
      pstmt.setString(2, propertyAd.project_name)
      pstmt.setString(3, propertyAd.developer)
      pstmt.setString(4, propertyAd.city)
      pstmt.setString(5, propertyAd.district)
      pstmt.setString(6, propertyAd.address)
      pstmt.setString(7, propertyAd.property_type)
      pstmt.setString(8, propertyAd.status)
      pstmt.setDouble(9, propertyAd.size)
      pstmt.setInt(10, propertyAd.number_of_rooms)
      pstmt.setInt(11, propertyAd.floor)
      pstmt.setDouble(12, propertyAd.price)
      pstmt.setDouble(13, propertyAd.price_per_sqm)
      pstmt.setString(14, propertyAd.project_link)
      pstmt.setString(15, propertyAd.apartment_link)
      pstmt.setString(16, propertyAd.date)
      pstmt.addBatch

    }

    /** Establishes connection and writes data in batches (for speed up) to SQL table */
    theSeq.foreach(setPropertyAd(pstmt, _))
    pstmt.executeBatch()
    conn.commit()
    conn.setAutoCommit(true)
    pstmt.close()
  }

}
