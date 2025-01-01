import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdatePinServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect("Login.html");
      return;
    }
    String role = (String)session.getAttribute("role");
    if ("user".equals(role)) {

      if (session.getAttribute("accountNumber") == null) {
        response.sendRedirect("Login.html");
        return;
      }

      String accountNumber = (String)session.getAttribute("accountNumber");

      String newPin = request.getParameter("newPin");

      try {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://127.0.0.1/address-book";
        Connection conn = DriverManager.getConnection(url, "root", "root");
        Statement stmt = conn.createStatement();
        String query = "UPDATE accounts SET pin = '" + newPin +
                       "' WHERE account_number = '" + accountNumber + "'";
        int rs = stmt.executeUpdate(query);
        if (rs == 1) {
          if ("admin".equals(role)) {
            response.sendRedirect(
                "AdminDashboardServlet?message=PIN updated successfully");
          } else {
            response.sendRedirect(
                "UserDashboardServlet?message=PIN updated successfully");
          }
        } else {
          response.sendRedirect("UpdatePin.html?error=Failed to update PIN");
        }
        conn.close();
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
        out.println("Error : " + e.getMessage());
      }
    } else if ("admin".equals(role)) {
      String accountNumber = request.getParameter("accountNumber");
      String newPin = request.getParameter("newPin");

      try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/address-book", "root", "root");
        Statement stmt = conn.createStatement();
        String updateQuery = "UPDATE accounts SET pin = '" + newPin +
                             "' WHERE account_number = '" + accountNumber + "'";
        int rowsUpdated = stmt.executeUpdate(updateQuery);

        if (rowsUpdated > 0) {
          out.println("<html lang='en'>");
          out.println("<head>");
          out.println("<style>");
          out.println(
              "body { font-family: Arial, sans-serif; background-color: #fdf6e3; padding: 20px; }");
          out.println("h1 { color: #657b83; }");
          out.println("a { color: #b58900; text-decoration: none; }");
          out.println("a:hover { text-decoration: underline; }");
          out.println("</style>");
          out.println("</head>");
          out.println("<body>");
          out.println("<h1>PIN Updated Successfully</h1>");
          out.println("<a href='AdminDashboardServlet'>Back to Dashboard</a>");
          out.println("</body>");
          out.println("</html>");

        } else {
          response.sendRedirect(
              "changePinbyadmin.html?error=Account not found");
        }
        conn.close();
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
        out.println("Error : " + e.getMessage());
      }
    }
  }
}