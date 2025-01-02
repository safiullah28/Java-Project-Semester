import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddAmountServlet extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("accountDetails") == null) {
      response.sendRedirect("Login.html");
      return;
    }

    Account account = (Account)session.getAttribute("accountDetails");
    String accountNumber = account.account_number;
    double balance = account.balance;

    String amountt = request.getParameter("amount");
    double amount = 0.0;
    response.setContentType("text/html");
    out.println("<html lang='en'>");

    try {
      amount = Double.parseDouble(amountt);
      if (amount <= 0) {
        throw new NumberFormatException("Amount must be positive");
      }
    } catch (NumberFormatException e) {
      response.setContentType("text/html");

      out.println("<body>");
      out.println("<h3>Invalid Amount. Please enter a positive number.</h3>");
      out.println("<a href='AddAmount.html'>Add Amount</a>");
      out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
      out.println("</body></html>");
      return;
    }

    try {

      AccountDAO accountDAO = new AccountDAO();
      boolean balanceAdded = accountDAO.addBalance(accountNumber, amount);
      if (balanceAdded) {
        balance += amount;

        account.balance = balance;
        session.setAttribute("accountDetails", account);

        out.println("<html lang='en'>");
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
        out.println("<h1>Amount Added Successfully!</h1>");
        out.println("<p>Account Number: <strong>" + accountNumber +
                    "</strong></p>");
        out.println("<p>Your new Balance is <strong>" + balance +
                    "</strong></p>");
        out.println("<a href='UserDashboardServlet'>Back to Dashboard</a>");
        out.println("</body>");
        out.println("</html>");
      } else {
        response.sendRedirect("AddAmount.html?error=Error in adding amount.");
      }

      accountDAO.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Error : " + e.getMessage());
    }
  }
}
