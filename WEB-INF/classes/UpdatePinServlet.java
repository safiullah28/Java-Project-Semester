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
    if (session == null || session.getAttribute("accountNumber") == null) {
      response.sendRedirect("Login.html");
      return;
    }

    String accountNumber = (String)session.getAttribute("accountNumber");
    String role = (String)session.getAttribute("role");
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
  }
}