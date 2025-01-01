import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangeUserPinServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
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

        // response.sendRedirect(
        //     "AdminDashboardServlet?message=PIN updated successfully for
        //     account: " + accountNumber);
      } else {
        response.sendRedirect("changePinbyadmin.html?error=Account not found");
      }
      conn.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error : " + e.getMessage());
    }
  }
}
