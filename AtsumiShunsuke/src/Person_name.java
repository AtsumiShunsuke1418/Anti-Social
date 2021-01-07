import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person_name {
  public static void main(String[] args) {
    // DB(conpany_detail) 接続情報
    String schema = "jdbc:mysql://localhost:3306/";
    String databaseName = "company";
    String option = "?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9:00";
    String databaseURL = schema + databaseName + option;
    String databaseID = "root";
    String databasePassword = "as130578";

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    try {
      // DBに接続
      con = DriverManager.getConnection(databaseURL, databaseID, databasePassword);
      st = con.createStatement();
      String sql = "SELECT * FROM company_detail;";
      rs = st.executeQuery(sql);
      // System.out.println("MySQLに接続できました。");

      while(rs.next()){
        int id = rs.getInt("id");
        String first_name = rs.getString("first_name");
        String last_name = rs.getString("last_name");
        System.out.println(id + "： " + first_name + " " + last_name);
      }

    } catch (SQLException e) {
      System.out.println("MySQLに接続できませんでした。");
    }  finally {
      try {
        if (rs != null)
            rs.close();
          if (st != null)
              st.close();
          if (con != null)
              con.close();
      } catch (Exception ex) {
          ex.printStackTrace();
      }
    }
  }
}
