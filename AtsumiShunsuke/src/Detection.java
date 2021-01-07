import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Detection {
  public static void main(String[] args) {
  // DB(conpany_detail) 接続情報
    String schema = "jdbc:mysql://localhost:3306/";
    String databaseName = "company";
    String option = "?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9:00";
    String databaseURL = schema + databaseName + option;
    String databaseID = "root";
    String databasePassword = "as130578";
    String sql_company = "SELECT * FROM company_detail WHERE company_name IN ('Evil Corp', 'YAKUZA Inc');";
    String sql_employee = "SELECT * FROM company_detail WHERE first_name IN ('Evil', 'YAKUZA') OR last_name IN ('Evil', 'YAKUZA');";

    // クローズさせるために、tryブロックより前にリソース用変数を定義
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    try {
      // DBに接続
      con = DriverManager.getConnection(databaseURL, databaseID, databasePassword);
      st = con.createStatement();
      // System.out.println("MySQLに接続できました。");
      ResultSet rs_co = st.executeQuery(sql_company);
      System.out.println("---------------------");
      if(rs_co == null) {
        System.out.println("貴社の取引先から反社会企業は見つかりませんでした。");
      }else{
        System.out.println("[反社会企業リスト]");
        System.out.println("(id)： " + " (会社名)");
        // ループ処理
        while (rs_co.next()) {
          int i_co = rs_co.getInt("id");
          String str_co = rs_co.getString("company_name");
          System.out.println(i_co + "： " + str_co);
      }

    }
      System.out.println("---------------------");

      ResultSet rs_em = st.executeQuery(sql_employee);
      if(rs_em == null) {
        System.out.println("貴社の取引先から反社会構成員は見つかりませんでした。");
      }else{
        System.out.println("[反社会構成員リスト]");
        System.out.println("(id)： " + "  (氏名)");
        // ループ処理
        while (rs_em.next()) {
          int i_em = rs_em.getInt("id");
          String str_fn = rs_em.getString("first_name");
          String str_ln = rs_em.getString("last_name");
          System.out.println(i_em + "： " + str_fn + " " + str_ln);
        }
      }
      System.out.println("---------------------");
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