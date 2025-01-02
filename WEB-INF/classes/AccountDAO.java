import java.sql.*;
import java.util.*;

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

  public boolean createAccount(String accountNumber, String pin, String role) {
    try {
      String query =
          "INSERT INTO accounts (account_number, pin, role, balance) VALUES ('" +
          accountNumber + "', '" + pin + "', '" + role + "', 0.0)";
      int rowsAffected = stmt.executeUpdate(query);
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteAccount(String accountNumber) {
    try {
      String query =
          "DELETE FROM accounts WHERE account_number = '" + accountNumber + "'";
      int rowsAffected = stmt.executeUpdate(query);
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean addBalance(String accountNumber, double amount) {
    try {
      String updateQuery = "UPDATE accounts SET balance = balance + " + amount +
                           " WHERE account_number = '" + accountNumber + "'";
      int rowsAffected = stmt.executeUpdate(updateQuery);
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  public boolean withDrawBalance(String accountNumber, double amount) {
    try {
      String updateQuery = "UPDATE accounts SET balance = balance - " + amount +
                           " WHERE account_number = '" + accountNumber + "'";
      int rowsAffected = stmt.executeUpdate(updateQuery);
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public Account getAccount(String accountNumber) {
    Account account = null;
    try {
      String query = "SELECT * FROM accounts WHERE account_number = '" +
                     accountNumber + "'";
      ResultSet rs = stmt.executeQuery(query);

      if (rs.next()) {
        String account_number = rs.getString("account_number");
        String pin = rs.getString("pin");
        double balance = rs.getDouble("balance");
        String role = rs.getString("role");

        account = new Account(account_number, pin, balance, role);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error fetching account details: " +
                                 e.getMessage());
    }
    return account;
  }

  public boolean sendAmount(String senderAccount, String receiverAccount,
                            double amount) throws SQLException {
    boolean success = false;

    try {
      conn.setAutoCommit(false);

      String deductBalanceQuery = "UPDATE accounts SET balance = balance - " +
                                  amount + " WHERE account_number = '" +
                                  senderAccount + "' AND balance >= " + amount;

      if (stmt.executeUpdate(deductBalanceQuery) != 1) {
        conn.rollback();
        stmt.close();
        return false;
      }

      String addBalanceQuery = "UPDATE accounts SET balance = balance + " +
                               amount + " WHERE account_number = '" +
                               receiverAccount + "'";

      if (stmt.executeUpdate(addBalanceQuery) != 1) {
        conn.rollback();
        stmt.close();
        return false;
      }

      conn.commit();
      success = true;
      stmt.close();
    } catch (SQLException e) {
      conn.rollback();
      throw e;
    } finally {
      conn.setAutoCommit(true);
    }

    return success;
  }

  public Account login(String account_Number, String Pin) throws SQLException {
    try {
      String query = "SELECT * FROM accounts WHERE account_number ='" +
                     account_Number + "' AND pin ='" + Pin + "'";

      ResultSet rs = stmt.executeQuery(query);

      if (rs.next()) {
        String accountNumber = rs.getString("account_number");
        String role = rs.getString("role");
        double balance = rs.getDouble("balance");
        String pin = rs.getString("pin");
        return new Account(accountNumber, pin, balance, role);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error fetching account details: " +
                                 e.getMessage());
    }
    return null;
  }

  public List<Account> getAllAccounts() throws SQLException {
    List<Account> accounts = new ArrayList<>();

    try {
      String query = "SELECT * FROM accounts WHERE role = 'user'";

      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        String account_number = rs.getString("account_number");
        double balance = rs.getDouble("balance");
        String pin = rs.getString("pin");
        String role = rs.getString("role");

        Account account = new Account(account_number, pin, balance, role);
        accounts.add(account);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error fetching account details: " +
                                 e.getMessage());
    }
    return accounts;
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
