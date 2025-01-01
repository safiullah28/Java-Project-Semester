import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangeUserPinServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);

    String accountNumber = request.getParameter("accountNumber");
    String newPin = request.getParameter("newPin");
    String adminAccountNumber = (String)session.getAttribute("accountNumber");

    try {
      // Class.forName("com.mysql.jdbc.Driver");
      // Connection conn = DriverManager.getConnection(
      //     "jdbc:mysql://localhost:3306/address-book", "root", "root");
      // Statement stmt = conn.createStatement();
      // String updateQuery = "UPDATE accounts SET pin = '" + newPin +
      //                      "' WHERE account_number = '" + accountNumber +
      //                      "'";
      // int rowsUpdated = stmt.executeUpdate(updateQuery);
      // if (rowsUpdated > 0)

      AccountDAO accountDAO = new AccountDAO();

      if (!accountNumber.equals(adminAccountNumber))

      {
        boolean isUpdated = accountDAO.updatePin(accountNumber, newPin);
        if (isUpdated) {
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
        accountDAO.close();
        out.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error : " + e.getMessage());
    }
  }
}
