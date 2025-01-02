// import java.io.*;
// import java.sql.*;
// import javax.servlet.*;
// import javax.servlet.http.*;

// public class ChangeUserPinServlet extends HttpServlet {

//   protected void doPost(HttpServletRequest request,
//                         HttpServletResponse response)
//       throws ServletException, IOException {
//     PrintWriter out = response.getWriter();
//     HttpSession session = request.getSession(false);

//     if (session == null || session.getAttribute("accountDetails") == null) {
//       response.sendRedirect("Login.html");
//       return;
//     }

//     String accountNumber = request.getParameter("accountNumber");
//     String newPin = request.getParameter("newPin");
//     Account account = (Account)session.getAttribute("accountDetails");
//     String adminAccountNumber = account.account_number;
//     String role = account.role;

//     try {

//       AccountDAO accountDAO = new AccountDAO();

//       if (!accountNumber.equals(adminAccountNumber))

//       {
//         boolean isUpdated = accountDAO.updatePin(accountNumber, newPin);
//         if (isUpdated) {
//           // session.setAttribute("accountDetails", account);
//           out.println("<html lang='en'>");
//           out.println("<head>");
//           out.println("<style>");
//           out.println(
//               "body { font-family: Arial, sans-serif; background-color:
//               #fdf6e3; padding: 20px; }");
//           out.println("h1 { color: #657b83; }");
//           out.println("a { color: #b58900; text-decoration: none; }");
//           out.println("a:hover { text-decoration: underline; }");
//           out.println("</style>");
//           out.println("</head>");
//           out.println("<body>");
//           out.println("<h1>PIN Updated Successfully</h1>");
//           out.println("<a href='AdminDashboardServlet'>Back to
//           Dashboard</a>"); out.println("</body>"); out.println("</html>");
//         } else {
//           response.sendRedirect(
//               "changePinbyadmin.html?error=Account not found");
//         }
//         accountDAO.close();
//         out.close();
//       } else {
//         response.sendRedirect(
//             "changePinbyadmin.html?error=Admin's pin is changed from other
//             page");
//         out.close();
//       }
//     } catch (Exception e) {
//       e.printStackTrace();
//       out.println("Error : " + e.getMessage());
//     }
//   }
// }

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

    // Validate session and admin privileges
    if (session == null || session.getAttribute("accountDetails") == null) {
      response.sendRedirect("Login.html");
      return;
    }

    Account account = (Account)session.getAttribute("accountDetails");
    String role = (String)session.getAttribute("role");

    if (role == null || !role.equals("admin")) {
      response.sendRedirect("Login.html?error=Unauthorized access");
      return;
    }

    String accountNumber = request.getParameter("accountNumber");
    String newPin = request.getParameter("newPin");

    if (accountNumber == null || newPin == null || newPin.length() != 6) {
      response.sendRedirect("changePinbyadmin.html?error=Invalid input");
      return;
    }

    AccountDAO accountDAO = null;
    try {
      accountDAO = new AccountDAO();
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
        response.sendRedirect("changePinbyadmin.html?error=Account not found");
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error: " + e.getMessage());
    } finally {
      if (accountDAO != null) {
        accountDAO.close();
      }
      out.close();
    }
  }
}
