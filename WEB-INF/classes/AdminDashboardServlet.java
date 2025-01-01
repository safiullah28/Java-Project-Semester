import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AdminDashboardServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || !"admin".equals(session.getAttribute("role"))) {
      response.sendRedirect("Login.html");
      return;
    }

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String accountNumber = (String)session.getAttribute("accountNumber");
    try {
      Class.forName("com.mysql.jdbc.Driver");

      String url = "jdbc:mysql://127.0.0.1/address-book";
      Connection conn = DriverManager.getConnection(url, "root", "root");
      Statement stmt = conn.createStatement();
      String query =
          "SELECT account_number, balance FROM accounts WHERE role = 'user'";
      ResultSet rs = stmt.executeQuery(query);

      // out.println("<!DOCTYPE html>");
      // out.println("<html lang='en'>");
      // out.println("<head><title>Admin Dashboard</title></head>");
      // out.println("<body>");
      // out.println("<h1>Admin Dashboard</h1>");
      // out.println("<table border='1'>");
      // out.println("<tr><th>Account Number</th><th>Balance</th></tr>");

      // while (rs.next()) {
      //   String accountnumb = rs.getString("account_number");

      //   Double amount = rs.getDouble("balance");
      //   out.println("<tr>");
      //   out.println("<td>" + accountnumb + "</td>");
      //   out.println("<td>" + amount + "</td>");
      //   out.println("</tr>");
      // }

      // out.println("</table>");
      // out.println("<a href='UpdatePin.html'>Change PIN</a><br>");
      // out.println("<a href='changePinbyadmin.html'>Change User PIN</a><br>");
      // out.println("<a href='LogOutServlet'>Logout</a>");
      // out.println("</body>");
      // out.println("</html>");

      out.println("<html lang='en'>");
      out.println("<head>");
      out.println("<style>");
      out.println(
          "body { font-family: Arial, sans-serif; background-color: #eef2f7; padding: 20px; }");
      out.println("h1 { color: #2a9d8f; }");
      out.println(
          "table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
      out.println(
          "th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
      out.println("th { background-color: #264653; color: #fff; }");
      out.println("tr:hover { background-color: #f1f1f1; }");
      out.println("a { color: #2a9d8f; text-decoration: none; }");
      out.println("a:hover { text-decoration: underline; }");
      out.println("</style>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Admin Account Number : " + accountNumber + "</h1>");
      out.println("<h1>Admin Dashboard</h1>");
      out.println("<table>");
      out.println("<tr><th>Account Number</th><th>Balance</th></tr>");
      while (rs.next()) {
        out.println("<tr>");
        out.println("<td>" + rs.getString("account_number") + "</td>");
        out.println("<td>" + rs.getDouble("balance") + "</td>");
        out.println("</tr>");
      }
      out.println("</table>");
      out.println("<a href='UpdatePin.html'>Change PIN</a><br>");
      out.println(
          "<a href='createAccountByAdmin.html'>Create New User's Account</a><br>");

      out.println("<a href='changePinbyadmin.html'>Change User PIN</a><br>");
      out.println("<a href='LogOutServlet'>Logout</a>");
      out.println("</body>");
      out.println("</html>");
      conn.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error : " + e.getMessage());
    }
  }
}
