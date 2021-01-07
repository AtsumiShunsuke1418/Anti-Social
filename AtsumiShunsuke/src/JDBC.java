import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
    public static void main(String[] args) {
        // 接続情報はまとめて一か所に記載しておく
        String schema = "jdbc:mysql://localhost:3306/";
        String databaseName = "company";
        String option = "?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9:00";
        String databaseURL = schema + databaseName + option;
        String databaseID = "root";
        String databasePassword = "as130578";

        // クローズさせるために、tryブロックより前にリソース用変数を定義
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            // DBに接続
            con = DriverManager.getConnection(databaseURL, databaseID, databasePassword);
            st = con.createStatement();

            // SQL文の実行。全件削除してから1件だけ登録して、データを取得する。
            st.executeUpdate("DELETE FROM employers");
            st.executeUpdate(
                    "INSERT INTO employers (id, name, created_at, updated_at) VALUES (1, 'guest', '2020-09-01 00:00:00.000000', '2020-09-01 00:00:00.000000');");
            rs = st.executeQuery("SELECT * FROM employers");

            while (rs.next()) {
                // レコードの取得
                String employerID = rs.getString("id");
                String employerName = rs.getString("name");
                System.out.printf("ID:%s 名前:%s\n", employerID, employerName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            // 開いたリソースをクローズしないと、リソースが開きっぱなしになってよくない。
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
