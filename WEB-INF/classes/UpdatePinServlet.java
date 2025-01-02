// import java.io.*;
// import java.sql.*;
// import javax.servlet.*;
// import javax.servlet.http.*;

// public class UpdatePinServlet extends HttpServlet {

//   public void doPost(HttpServletRequest request, HttpServletResponse
//   response)
//       throws ServletException, IOException {
//     PrintWriter out = response.getWriter();
//     HttpSession session = request.getSession(false);

//     String role = (String)session.getAttribute("role");

//     if (session == null || session.getAttribute("accountNumber") == null) {
//       response.sendRedirect("Login.html");
//       return;
//     }

//     String accountNumber = (String)session.getAttribute("accountNumber");

//     String newPin = request.getParameter("newPin");

//     try {

//       AccountDAO accountDAO = new AccountDAO();

//       boolean isUpdated = accountDAO.updatePin(accountNumber, newPin);
//       if (isUpdated) {
//         if ("admin".equals(role)) {
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
//           out.println("<a href='UserDashboardServlet'>Back to
//           Dashboard</a>"); out.println("</body>"); out.println("</html>");
//         }
//       } else {
//         response.sendRedirect("UpdatePin.html?error=Failed to update PIN");
//       }

//       accountDAO.close();
//       out.close();
//     } catch (Exception e) {
//       e.printStackTrace();
//       out.println("Error : " + e.getMessage());
//     }
//   }
// }

// import java.io.*;
// import javax.servlet.*;
// import javax.servlet.http.*;

// public class UpdatePinServlet extends HttpServlet {
//   public void doPost(HttpServletRequest request, HttpServletResponse
//   response)
//       throws ServletException, IOException {
//     PrintWriter out = response.getWriter();
//     HttpSession session = request.getSession(false);

//     if (session == null || session.getAttribute("accountDetails") == null) {
//       response.sendRedirect("Login.html");
//       return;
//     }
//     Account account = (Account)session.getAttribute("accountDetails");

//     // Extract necessary details from the Account object
//     String accountNumber = account.account_number;
//     String role = account.role;

//     String newPin = request.getParameter("newPin");

//     try {
//       AccountDAO accountDAO = new AccountDAO();

//       boolean isUpdated = accountDAO.updatePin(accountNumber, newPin);
//       if (isUpdated) {
//         out.println("<html lang='en'>");
//         out.println("<head>");
//         out.println("<style>");
//         out.println(
//             "body { font-family: Arial, sans-serif; background-color:
//             #fdf6e3; padding: 20px; }");
//         out.println("h1 { color: #657b83; }");
//         out.println("a { color: #b58900; text-decoration: none; }");
//         out.println("a:hover { text-decoration: underline; }");
//         out.println("</style>");
//         out.println("</head>");
//         out.println("<body>");
//         out.println("<h1>PIN Updated Successfully</h1>");
//         if ("admin".equals(role)) {
//           out.println("<a href='AdminDashboardServlet'>Back to
//           Dashboard</a>");
//         } else {
//           out.println("<a href='UserDashboardServlet'>Back to
//           Dashboard</a>");
//         }
//         out.println("</body>");
//         out.println("</html>");
//       } else {
//         response.sendRedirect("UpdatePin.html?error=Failed to update PIN");
//       }

//       accountDAO.close();
//       out.close();
//     } catch (Exception e) {
//       e.printStackTrace();
//       out.println("Error : " + e.getMessage());
//     }
//   }
// }

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdatePinServlet extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);

    // Check if session is valid
    if (session == null || session.getAttribute("accountDetails") == null) {

      response.sendRedirect("Login.html?error=Session has expired");
      return;
    }

    Account account = (Account)session.getAttribute("accountDetails");

    // Extract details from the session Account object
    String accountNumber = account.account_number;
    String role = (String)session.getAttribute("role");

    String newPin = request.getParameter("newPin");

    if (newPin == null || newPin.length() != 6) {
      response.sendRedirect("UpdatePin.html?error=Invalid PIN format");
      return;
    }

    AccountDAO accountDAO = null;

    try {
      accountDAO = new AccountDAO();

      boolean isUpdated = accountDAO.updatePin(accountNumber, newPin);

      if (isUpdated) {
        // Update the session attribute with the new PIN
        account.pin = newPin;
        session.setAttribute("accountDetails", account);

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
        if (role.equals("admin")) {
          out.println("<a href='AdminDashboardServlet'>Back to Dashboard</a>");
        } else {
          out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
        }
        out.println("</body>");
        out.println("</html>");
      } else {
        response.sendRedirect("UpdatePin.html?error=Failed to update PIN");
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
