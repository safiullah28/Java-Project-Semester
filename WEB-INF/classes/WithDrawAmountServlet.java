import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class WithDrawAmountServlet extends HttpServlet {

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

    String amountt = request.getParameter("amount");
    double amount = 0.0;
    response.setContentType("text/html");
    out.println("<html lang='en'>");

    try {
      amount = Double.parseDouble(amountt);
      if (amount <= 0) {
        throw new NumberFormatException("Amount must be positive");
      }
    } catch (NumberFormatException e) {
      response.setContentType("text/html");

      out.println("<body>");
      out.println("<h3>Invalid Amount. Please enter a positive number.</h3>");
      out.println("<a href='WithDrawAmount.html'>WithDraw Amount</a>");
      out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
      out.println("</body></html>");
      return;
    }

    try {
      Class.forName("com.mysql.jdbc.Driver");
      String url = "jdbc:mysql://127.0.0.1/address-book";
      Connection conn = DriverManager.getConnection(url, "root", "root");

      Statement stmt = conn.createStatement();
      String updateQuery = "UPDATE accounts SET balance = balance - " + amount +
                           " WHERE account_number = '" + accountNumber + "'";

      int rss = stmt.executeUpdate(updateQuery);
      if (rss > 0) {
        balance -= amount;
        session.setAttribute("balance", balance);

        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<style>");
        out.println(
            "body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 20px; }");
        out.println("h1, h3 { color: #333; }");
        out.println("a { color: #0066cc; text-decoration: none; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Amount WithDraw Successfully!</h1>");
        out.println("<p>Account Number: <strong>" + accountNumber +
                    "</strong></p>");
        out.println("<p>Your new Balance is <strong>" + balance +
                    "</strong></p>");
        out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
        out.println("</body>");
        out.println("</html>");

      } else {
        response.sendRedirect(
            "WithDrawAmount.html?error=Error in Withdrawing amount.");
      }

      conn.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error : " + e.getMessage());
    }
  }
}
