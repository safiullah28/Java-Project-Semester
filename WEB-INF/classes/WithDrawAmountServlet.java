// import java.io.*;
// import java.sql.*;
// import javax.servlet.*;
// import javax.servlet.http.*;

// public class WithDrawAmountServlet extends HttpServlet {

//   public void doPost(HttpServletRequest request, HttpServletResponse
//   response)
//       throws ServletException, IOException {
//     PrintWriter out = response.getWriter();
//     HttpSession session = request.getSession(false);
//     if (session == null || session.getAttribute("accountNumber") == null) {
//       response.sendRedirect("Login.html");
//       return;
//     }

//     String accountNumber = (String)session.getAttribute("accountNumber");
//     String pin = (String)session.getAttribute("pin");
//     double balance = (double)session.getAttribute("balance");
//     String role = (String)session.getAttribute("role");

//     String amountt = request.getParameter("amount");
//     double amount = 0.0;
//     response.setContentType("text/html");
//     out.println("<html lang='en'>");

//     try {
//       amount = Double.parseDouble(amountt);
//       if (amount <= 0) {
//         throw new NumberFormatException("Amount must be positive");
//       }
//     } catch (NumberFormatException e) {
//       response.setContentType("text/html");

//       out.println("<body>");
//       out.println("<h3>Invalid Amount. Please enter a positive
//       number.</h3>"); out.println("<a href='WithDrawAmount.html'>WithDraw
//       Amount</a>"); out.println("<a href='UserDashboardServlet'>Back to
//       Dashboard</a>"); out.println("</body></html>"); return;
//     }

//     try {

//       AccountDAO accountDAO = new AccountDAO();
//       boolean balanceAdded = accountDAO.withDrawBalance(accountNumber,
//       amount); if (balanceAdded) {

//         balance -= amount;
//         session.setAttribute("balance", balance);
//         Account account = new Account(accountNumber, pin, balance, role);
//         out.println("<html lang='en'>");
//         out.println("<head>");
//         out.println("<style>");
//         out.println(
//             "body { font-family: Arial, sans-serif; background-color:
//             #f4f4f9; margin: 0; padding: 20px; }");
//         out.println("h1, h3 { color: #333; }");
//         out.println("a { color: #0066cc; text-decoration: none; }");
//         out.println("a:hover { text-decoration: underline; }");
//         out.println("</style>");
//         out.println("</head>");
//         out.println("<body>");
//         out.println("<h1>Amount WithDraw Successfully!</h1>");
//         out.println("<p>Account Number: <strong>" + account.accountNumber +
//                     "</strong></p>");
//         out.println("<p>Your new Balance is <strong>" + account.balance +
//                     "</strong></p>");
//         out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
//         out.println("</body>");
//         out.println("</html>");

//       } else {
//         response.sendRedirect(
//             "WithDrawAmount.html?error=Error in Withdrawing amount.");
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

public class WithDrawAmountServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("accountDetails") == null) {
      response.sendRedirect("Login.html");
      return;
    }

    Account account = (Account)session.getAttribute("accountDetails");

    double balance = account.balance;
    String accountNumber = account.account_number;
    String pin = account.pin;
    String role = account.role;

    String amountParam = request.getParameter("amount");
    double amount;

    response.setContentType("text/html");
    out.println("<html lang='en'>");

    try {
      amount = Double.parseDouble(amountParam);
      if (amount <= 0) {
        throw new NumberFormatException("Amount must be positive");
      }
    } catch (NumberFormatException e) {
      out.println("<body>");
      out.println("<h3>Invalid Amount. Please enter a positive number.</h3>");
      out.println("<a href='WithDrawAmount.html'>WithDraw Amount</a>");
      out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
      out.println("</body></html>");
      return;
    }

    try {
      AccountDAO accountDAO = new AccountDAO();

      // Check if sufficient balance is available
      if (amount > balance) {
        out.println("<body>");
        out.println(
            "<h3>Insufficient balance. Please try a smaller amount.</h3>");
        out.println("<a href='WithDrawAmount.html'>WithDraw Amount</a>");
        out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
        out.println("</body></html>");
        return;
      }

      // Perform withdrawal
      boolean isWithdrawn = accountDAO.withDrawBalance(accountNumber, amount);
      if (isWithdrawn) {
        balance -= amount;

        // Update session account details
        account.balance = balance;
        session.setAttribute("accountDetails", account);

        out.println("<head>");
        out.println("<style>");
        out.println(
            "body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 20px; }");
        out.println("h1, h3 { color: #333; }");
        out.println("a { color: #0066cc; text-decoration: none; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Amount Withdrawn Successfully!</h1>");
        out.println("<p>Account Number: <strong>" + account.account_number +
                    "</strong></p>");
        out.println("<p>Your new Balance is <strong>" + account.balance +
                    "</strong></p>");
        out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
        out.println("</body>");
        out.println("</html>");
      } else {
        response.sendRedirect(
            "WithDrawAmount.html?error=Error in Withdrawing amount.");
      }

      accountDAO.close();
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error : " + e.getMessage());
    } finally {
      out.close();
    }
  }
}
