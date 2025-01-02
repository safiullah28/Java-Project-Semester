// import java.io.*;
// import java.sql.*;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
// import javax.servlet.*;
// import javax.servlet.http.*;

// public class LoginServlet extends HttpServlet {

//   private static final Map<String, HttpSession> activeSessions =
//       new ConcurrentHashMap<>();
//   public void doPost(HttpServletRequest request, HttpServletResponse
//   response)
//       throws ServletException, IOException {

//     PrintWriter out = response.getWriter();
//     String accountNumber = request.getParameter("accountNumber");
//     String pin = request.getParameter("pin");

//     HttpSession session = request.getSession(true);
//     try {
//       Class.forName("com.mysql.jdbc.Driver");
//       String url = "jdbc:mysql://127.0.0.1:3306/address-book";
//       Connection conn = DriverManager.getConnection(url, "root", "root");

//       Statement stmt = conn.createStatement();
//       String query = "SELECT * FROM accounts WHERE account_number = '" +
//                      accountNumber + "' AND pin = '" + pin + "'";
//       ResultSet rs = stmt.executeQuery(query);

//       if (rs.next()) {
//         session.setAttribute("accountNumber", accountNumber);
//         String role = rs.getString("role");
//         session.setAttribute("role", role);
//         String pin = rs.getString("pin");
//         session.setAttribute("pin", pin);
//         session.setAttribute("balance", rs.getDouble("balance"));
//         activeSessions.put(accountNumber, session);

//         if (role.equals("admin")) {
//           response.sendRedirect("AdminDashboardServlet");
//         } else {
//           response.sendRedirect("UserDashboardServlet");
//         }
//       } else {
//         response.sendRedirect("Login.html?error=Invalid credentials");
//       }
//       conn.close();
//       out.close();
//     } catch (Exception e) {
//       e.printStackTrace();

//       out.println(e.getMessage());
//     }
//   }
//   public static Map<String, HttpSession> getActiveSessions() {
//     return activeSessions;
//   }
// }

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

  private static final Map<String, HttpSession> activeSessions =
      new ConcurrentHashMap<>();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    String accountNumber = request.getParameter("accountNumber");
    String pin = request.getParameter("pin");

    HttpSession session = request.getSession();

    try {
      AccountDAO accountDAO = new AccountDAO();
      Account account = accountDAO.login(accountNumber, pin);

      if (account != null) {
        String accountnumber = account.account_number;
        session.setAttribute("accountNumber", accountnumber);

        String role = account.role;
        session.setAttribute("role", role);
        String pinn = account.pin;
        session.setAttribute("pin", pinn);
        double balance = account.balance;
        session.setAttribute("balance", balance);
        activeSessions.put(accountNumber, session);
        session.setAttribute("accountDetails", account);

        if ("admin".equals(account.role)) {
          response.sendRedirect("AdminDashboardServlet");
        } else {
          response.sendRedirect("UserDashboardServlet");
        }
      } else {
        response.sendRedirect("Login.html?error=Invalid credentials");
      }

    } catch (Exception e) {
      e.printStackTrace();
      out.println("<h1>Error: " + e.getMessage() + "</h1>");
    } finally {
      out.close();
    }
  }

  public static Map<String, HttpSession> getActiveSessions() {
    return activeSessions;
  }
}
