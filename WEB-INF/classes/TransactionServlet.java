
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class TransactionServlet extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("accountNumber") == null) {
      response.sendRedirect("Login.html");
      return;
    }

    String accountNumber = (String)session.getAttribute("accountNumber");
    double balance = (double)session.getAttribute("balance");
    String receiverAccount = request.getParameter("receiverAccount");
    double amount = Double.parseDouble(request.getParameter("amount"));

    try {
      Class.forName("com.mysql.jdbc.Driver");

      String url = "jdbc:mysql://127.0.0.1:3306/address-book";
      Connection conn = DriverManager.getConnection(url, "root", "root");

      conn.setAutoCommit(false);

      Statement stmt = conn.createStatement();
      String deductBalance = "UPDATE accounts SET balance = balance - " +
                             amount + " WHERE account_number = '" +
                             accountNumber + "' AND balance >= " + amount;
      int rss = stmt.executeUpdate(deductBalance);

      if (rss == 1) {
        String addBalance = "UPDATE accounts SET balance = balance + " +
                            amount + " WHERE account_number = '" +
                            receiverAccount + "'";
        int rs = stmt.executeUpdate(addBalance);
        if (rs == 1) {
          conn.commit();
          balance += amount;
          session.setAttribute("balance", balance);
          out.println("<head><title>Transaction Successful</title></head>");
          out.println("<body>");
          out.println(
              "<h1>Amount Transferred to Account Number : " + receiverAccount +
              "from Account No. : " + accountNumber + "</h1>");
          out.println("<p>Your New Balance : " + balance + "</p>");
          out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
          out.println("</body>");
          out.println("</html>");
          response.sendRedirect(
              "UserDashboardServlet?message=Transaction successfully");
        }
      } else {
        conn.rollback();
        response.sendRedirect(
            "Transaction.html?error=Not sufficient balance to send");
      }
      conn.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error : " + e.getMessage());
    }
  }
}
