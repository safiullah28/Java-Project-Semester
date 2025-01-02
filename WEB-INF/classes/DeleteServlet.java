// import java.io.*;
// import java.sql.*;
// import java.util.Map;
// import javax.servlet.*;
// import javax.servlet.http.*;

// public class DeleteServlet extends HttpServlet {

//   @Override
//   public void doGet(HttpServletRequest request, HttpServletResponse response)
//       throws ServletException, IOException {
//     PrintWriter out = response.getWriter();
//     HttpSession session = request.getSession(false);
//     if (session == null || session.getAttribute("accountDetails") == null) {
//       response.sendRedirect("Login.html");
//       return;
//     }
//     // String role = (String)session.getAttribute("role");
//     Account account = (Account)session.getAttribute("accountDetails");
//     String role = account.role;
//     if ("user".equals(role)) {
//       String accountNumber = account.account_number;

//       // String accountNumber =
//       (String)session.getAttribute("accountNumber");

//       try {

//         AccountDAO accountDAO = new AccountDAO();

//         boolean isDeleted = accountDAO.deleteAccount(accountNumber);

//         if (isDeleted) {
//           session.invalidate();
//           response.sendRedirect(
//               "Login.html?message=Account deleted successfully");
//         } else {
//           response.sendRedirect(
//               "UserDashboardServlet?error=Account deletion failed");
//         }
//         accountDAO.close();
//       } catch (Exception e) {
//         e.printStackTrace();
//         out.println("Error: " + e.getMessage());
//       }
//       out.close();
//     }
//   }

//   public void doPost(HttpServletRequest request, HttpServletResponse
//   response)
//       throws ServletException, IOException {
//     PrintWriter out = response.getWriter();
//     HttpSession session = request.getSession(false);
//     if (session == null) {
//       response.sendRedirect("Login.html?error=Please log in first");
//       return;
//     }

//     // String role = (String)session.getAttribute("role");
//     // String adminAccountNumber =
//     // (String)session.getAttribute("accountNumber");
//     Account account = (Account)session.getAttribute("accountDetails");
//     String role = account.role;
//     String adminAccountNumber = account.account_number;

//     if ("admin".equals(role)) {
//       String accountNumber = request.getParameter("accountNumber");

//       try {
//         Map<String, HttpSession> activeSessions =
//             LoginServlet.getActiveSessions();
//         HttpSession userSession = activeSessions.get(accountNumber);

//         if (!accountNumber.equals(adminAccountNumber)) {
//           // Invalidate the session if the user is logged in
//           if (userSession != null) {
//             userSession.invalidate();
//             activeSessions.remove(accountNumber);
//           }

//           // Proceed to delete the account from the database
//           AccountDAO accountDAO = new AccountDAO();

//           boolean isDeleted = accountDAO.deleteAccount(accountNumber);

//           if (isDeleted) {
//             response.sendRedirect(
//                 "AdminDashboardServlet?message=Account deleted
//                 successfully");
//           } else {
//             response.sendRedirect(
//                 "DeleteUser.html?error=Account deletion failed");
//           }
//           accountDAO.close();
//         } else {
//           response.sendRedirect("DeleteUser.html?error=Admin can't be
//           deleted");
//         }

//       } catch (Exception e) {
//         e.printStackTrace();
//         out.println("Error: " + e.getMessage());
//       }
//       out.close();
//     }
//   }
// }

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

    // Validate session and account details
    if (session == null || session.getAttribute("accountDetails") == null) {
      response.sendRedirect("Login.html");
      return;
    }

    Account account = (Account)session.getAttribute("accountDetails");
    String role = account.role;

    if ("user".equals(role)) {
      String accountNumber = account.account_number;
      AccountDAO accountDAO = null;

      try {
        accountDAO = new AccountDAO();

        boolean isDeleted = accountDAO.deleteAccount(accountNumber);

        if (isDeleted) {
          session.invalidate();
          response.sendRedirect(
              "Login.html?message=Account deleted successfully");
        } else {
          response.sendRedirect(
              "UserDashboardServlet?error=Account deletion failed");
        }
      } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect(
            "UserDashboardServlet?error=An error occurred during account deletion");
      } finally {
        if (accountDAO != null) {
          accountDAO.close();
        }
        out.close();
      }
    } else {
      response.sendRedirect("Login.html?error=Unauthorized access");
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);

    // Validate session and account details
    if (session == null || session.getAttribute("accountDetails") == null) {
      response.sendRedirect("Login.html?error=Please log in first");
      return;
    }

    Account account = (Account)session.getAttribute("accountDetails");
    String role = (String)session.getAttribute("role");
    String adminAccountNumber = account.account_number;

    if (!"admin".equals(role)) {
      response.sendRedirect("Login.html?error=Unauthorized access");
      return;
    }

    String accountNumber = request.getParameter("accountNumber");

    if (accountNumber == null || accountNumber.trim().isEmpty()) {
      response.sendRedirect("DeleteUser.html?error=Invalid account number");
      return;
    }

    // Prevent admin from deleting their own account
    if (accountNumber.equals(adminAccountNumber)) {
      response.sendRedirect(
          "DeleteUser.html?error=Admin can't delete their own account");
      return;
    }

    AccountDAO accountDAO = null;

    try {
      // Fetch active sessions
      Map<String, HttpSession> activeSessions =
          LoginServlet.getActiveSessions();
      HttpSession userSession = activeSessions.get(accountNumber);

      // Invalidate user session if active
      if (userSession != null) {
        userSession.invalidate();
        activeSessions.remove(accountNumber);
      }

      // Proceed to delete the account
      accountDAO = new AccountDAO();
      boolean isDeleted = accountDAO.deleteAccount(accountNumber);

      if (isDeleted) {
        response.sendRedirect(
            "AdminDashboardServlet?message=Account deleted successfully");
      } else {
        response.sendRedirect("DeleteUser.html?error=Account deletion failed");
      }
    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect(
          "DeleteUser.html?error=An error occurred during account deletion");
    } finally {
      if (accountDAO != null) {
        accountDAO.close();
      }
      out.close();
    }
  }
}
