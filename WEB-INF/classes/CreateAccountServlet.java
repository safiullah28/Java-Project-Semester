// import java.io.*;
// import java.sql.*;
// import javax.servlet.*;
// import javax.servlet.http.*;

// public class CreateAccountServlet extends HttpServlet {

//   public void doPost(HttpServletRequest request, HttpServletResponse
//   response)
//       throws ServletException, IOException {
//     PrintWriter out = response.getWriter();

//     HttpSession session = request.getSession(false);
//     if (session == null) {
//       String accountNumber = request.getParameter("accountNumber");
//       String pin = request.getParameter("pin");
//       String role = request.getParameter("role");

//       try {
//         AccountDAO accountDAO = new AccountDAO();
//         boolean isCreated = accountDAO.createAccount(accountNumber, pin,
//         role); if (isCreated) {
//           out.println("<!DOCTYPE html>");
//           out.println("<html lang='en'>");
//           out.println("<head>");
//           out.println("<style>");
//           out.println(
//               "body { font-family: Arial, sans-serif; background-color:
//               #dfe6e9; padding: 20px; }");
//           out.println("h1 { color: #2d3436; }");
//           out.println("a { color: #00cec9; text-decoration: none; }");
//           out.println("a:hover { text-decoration: underline; }");
//           out.println("</style>");
//           out.println("</head>");
//           out.println("<body>");
//           out.println("<h1>Account Created Successfully!</h1>");
//           out.println("<a href='Login.html'>Go to Login</a>");
//           out.println("</body>");
//           out.println("</html>");
//         }
//         out.close();
//         accountDAO.close();
//       } catch (Exception e) {
//         e.printStackTrace();
//         response.sendRedirect(
//             "createAccount.html?message=Error in creating account");
//         out.println("Error: " + e.getMessage());
//       }
//     }

//     else if ("admin".equals(session.getAttribute("role"))) {
//       String accountNumber = request.getParameter("accountNumber");
//       String pin = request.getParameter("pin");
//       String role = request.getParameter("role");

//       try {
//         AccountDAO accountDAO = new AccountDAO();
//         boolean isCreated = accountDAO.createAccount(accountNumber, pin,
//         role);

//         if (isCreated) {
//           out.println("<!DOCTYPE html>");
//           out.println("<html lang='en'>");
//           out.println("<head>");
//           out.println("<style>");
//           out.println(
//               "body { font-family: Arial, sans-serif; background-color:
//               #dfe6e9; padding: 20px; }");
//           out.println("h1 { color: #2d3436; }");
//           out.println("a { color: #00cec9; text-decoration: none; }");
//           out.println("a:hover { text-decoration: underline; }");
//           out.println("</style>");
//           out.println("</head>");
//           out.println("<body>");
//           out.println("<h1>Account Created Successfully!</h1>");
//           out.println(
//               "<a href='AdminDashboardServlet'>Go to Admin Dasboard</a>");
//           out.println("</body>");
//           out.println("</html>");
//         }
//         out.close();
//         accountDAO.close();
//       } catch (Exception e) {
//         e.printStackTrace();
//         response.sendRedirect(
//             "createAccountByAdmin.html?message=Error in creating account");
//         out.println("Error: " + e.getMessage());
//       }
//     }
//   }
// }

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateAccountServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(false);
    String accountNumber = request.getParameter("accountNumber");
    String pin = request.getParameter("pin");
    String role = request.getParameter("role");

    try {
      AccountDAO accountDAO = new AccountDAO();
      boolean isCreated = accountDAO.createAccount(accountNumber, pin, role);

      if (isCreated) {
        out.println("<!DOCTYPE html>");
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

        if (session != null && "admin".equals(session.getAttribute("role"))) {
          out.println(
              "<a href='AdminDashboardServlet'>Go to Admin Dashboard</a>");
        } else {
          out.println("<a href='Login.html'>Go to Login</a>");
        }

        out.println("</body>");
        out.println("</html>");
      } else {
        response.sendRedirect(
            "createAccount.html?message=Error in creating account");
      }
      accountDAO.close();
    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect(
          "createAccount.html?message=Error in creating account");
    } finally {
      out.close();
    }
  }
}
