import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateAccountServlet extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession();
    if (session == null) {
      String accountNumber = request.getParameter("accountNumber");
      String pin = request.getParameter("pin");
      String role = request.getParameter("role");

      try {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://127.0.0.1:3306/address-book";
        Connection conn = DriverManager.getConnection(url, "root", "root");
        Statement stmt = conn.createStatement();
        String query =
            "INSERT INTO accounts (account_number, pin, role, balance) VALUES ('" +
            accountNumber + "', '" + pin + "', '" + role + "', 0.0)";
        int rs = stmt.executeUpdate(query);
        if (rs == 1) {

          out.println("<html lang='en'>");
          out.println("<head>");
          out.println("<style>");
          out.println(
              "body { font-family: Arial, sans-serif; background-color: #dfe6e9; padding: 20px; }");
          out.println("h1 { color: #2d3436; }");
          out.println("a { color: #00cec9; text-decoration: none; }");
          out.println("a:hover { text-decoration: underline; }");
          out.println("</style>");
          out.println("</head>");
          out.println("<body>");
          out.println("<h1>Account Created Successfully!</h1>");
          out.println("<a href='Login.html'>Go to Login</a>");
          out.println("</body>");
          out.println("</html>");

          //   response.sendRedirect(
          //       "Login.html?message=Account created successfully");
          // }
          conn.close();
          out.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect(
            "CreateAccount.html?error=Error in creating account");
        out.println("Error: " + e.getMessage());
      }
    }

    else if ("admin".equals(session.getAttribute("role"))) {
      String accountNumber = request.getParameter("accountNumber");
      String pin = request.getParameter("pin");
      String role = request.getParameter("role");

      try {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://127.0.0.1/address-book";
        Connection conn = DriverManager.getConnection(url, "root", "root");
        Statement stmt = conn.createStatement();
        String query =
            "INSERT INTO accounts (account_number, pin, role, balance) VALUES ('" +
            accountNumber + "', '" + pin + "', '" + role + "', 0.0)";
        int rs = stmt.executeUpdate(query);
        if (rs == 1) {

          out.println("<html lang='en'>");
          out.println("<head>");
          out.println("<style>");
          out.println(
              "body { font-family: Arial, sans-serif; background-color: #dfe6e9; padding: 20px; }");
          out.println("h1 { color: #2d3436; }");
          out.println("a { color: #00cec9; text-decoration: none; }");
          out.println("a:hover { text-decoration: underline; }");
          out.println("</style>");
          out.println("</head>");
          out.println("<body>");
          out.println("<h1>Account Created Successfully!</h1>");
          out.println(
              "<a href='AdminDashboardServlet'>Go to Admin Dasboard</a>");
          out.println("</body>");
          out.println("</html>");
        }
        out.close();
        conn.close();
      } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect(
            "createAccountByAdmin.html?message=Error in creating account");
        out.println("Error: " + e.getMessage());
      }
    }
  }
}