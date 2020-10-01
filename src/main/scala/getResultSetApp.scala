import java.sql.ResultSet

import PropertyMarket.conn
import PropertyMarket.statement

/** Gets ResultSet from SQL db */

final case class getResultSetApp(){
  def getResultSet (sql:String, date:String): ResultSet = {
    val pstmt = conn.prepareStatement(sql)
    var resultSet = pstmt.executeQuery()
    if (sql.contains(":date")) {
      pstmt.setString(1, date)
      resultSet = pstmt.executeQuery()
    } else {
      resultSet = statement.executeQuery(sql)
    }
    resultSet
  }
}
