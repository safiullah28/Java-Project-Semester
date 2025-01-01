import java.sql.*;

public class AccountDAO {
  public Connection conn;
  public Statement stmt;

  public AccountDAO() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    String url = "jdbc:mysql://127.0.0.1/address-book";
    this.conn = DriverManager.getConnection(url, "root", "root");
    this.stmt = conn.createStatement();
  }

  public boolean updatePin(String accountNumber, String newPin) {
    try {
      String query = "UPDATE accounts SET pin = '" + newPin +
                     "' WHERE account_number = '" + accountNumber + "'";
      int rowsAffected = stmt.executeUpdate(query);
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void close() {
    try {
      if (stmt != null)
        stmt.close();
      if (conn != null)
        conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
