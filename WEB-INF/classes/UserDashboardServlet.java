import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserDashboardServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("accountNumber") == null) {
      response.sendRedirect("Login.html");
      return;
    }

    String accountNumber = (String)session.getAttribute("accountNumber");
    double balance = 0.0;

    try {
      Class.forName("com.mysql.jdbc.Driver");
      String url = "jdbc:mysql://127.0.0.1:3306/address-book";
      Connection conn = DriverManager.getConnection(url, "root", "root");
      Statement stmt = conn.createStatement();

      String query = "SELECT balance FROM accounts WHERE account_number = '" +
                     accountNumber + "'";
      ResultSet rs = stmt.executeQuery(query);

      if (rs.next()) {
        balance = rs.getDouble("balance");

        session.setAttribute("balance", balance);
      }

      conn.close();

    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error: " + e.getMessage());
    }

    response.setContentType("text/html");

    out.println("<html lang='en'>");
    out.println("<head>");
    out.println("<style>");
    out.println(
        "body { font-family: Arial, sans-serif; background-color: #eceff1; padding: 20px; }");
    out.println("h1 { color: #37474f; }");
    out.println("p { color: #546e7a; }");
    out.println(
        "a { color: #0288d1; text-decoration: none; margin-right: 15px; }");
    out.println("a:hover { text-decoration: underline; }");
    out.println("</style>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>User Dashboard</h1>");
    out.println("<p>Account Number: <strong>" + accountNumber +
                "</strong></p>");
    out.println("<p>Balance: <strong>" + balance + "</strong></p>");
    out.println("<a href='Transaction.html'>Send Amount</a>");
    out.println("<a href='WithDrawAmount.html'>WithDraw Amount</a>");

    out.println("<a href='AddAmount.html'>Add Amount</a>");
    out.println("<a href='UpdatePin.html'>Change PIN</a>");
    out.println("<a href='LogOutServlet'>Logout</a>");
    out.println("<br><a href='DeleteServlet'>Delete Account</a>");
    out.println("</body>");
    out.println("</html>");
    out.close();
  }
}
