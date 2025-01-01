import java.io.*;
import java.sql.*;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;

public class DeleteServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect("Login.html?error=Please log in first");
      return;
    }
    String role = (String)session.getAttribute("role");
    if ("user".equals(role)) {

      if (session == null || session.getAttribute("accountNumber") == null) {
        response.sendRedirect("Login.html?error=Please log in first");
        return;
      }

      String accountNumber = (String)session.getAttribute("accountNumber");

      try {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/address-book";
        Connection conn = DriverManager.getConnection(url, "root", "root");
        Statement stmt = conn.createStatement();

        String query = "DELETE FROM accounts WHERE account_number = '" +
                       accountNumber + "'";
        int rowsAffected = stmt.executeUpdate(query);

        conn.close();

        if (rowsAffected > 0) {
          session.invalidate();
          response.sendRedirect(
              "Login.html?message=Account deleted successfully");
        } else {
          response.sendRedirect(
              "UserDashboardServlet?error=Account deletion failed");
        }
      } catch (Exception e) {
        e.printStackTrace();
        out.println("Error: " + e.getMessage());
      }
      out.close();
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect("Login.html?error=Please log in first");
      return;
    }
    String role = (String)session.getAttribute("role");
    if ("admin".equals(role)) {

      String accountNumber = request.getParameter("accountNumber");

      try {
        Map<String, HttpSession> activeSessions =
            LoginServlet.getActiveSessions();
        HttpSession userSession = activeSessions.get(accountNumber);

        if (userSession != null) {
          userSession.invalidate();
          activeSessions.remove(accountNumber);
        }

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/address-book";
        Connection conn = DriverManager.getConnection(url, "root", "root");
        Statement stmt = conn.createStatement();

        String query = "DELETE FROM accounts WHERE account_number = '" +
                       accountNumber + "'";
        int rowsAffected = stmt.executeUpdate(query);

        conn.close();

        if (rowsAffected > 0) {
          response.sendRedirect(
              "AdminDashboardServlet?message=Account deleted successfully");
        } else {
          response.sendRedirect(
              "DeleteUser.html?error=Account deletion failed");
        }
      } catch (Exception e) {
        e.printStackTrace();
        out.println("Error: " + e.getMessage());
      }
      out.close();
    }
  }
}