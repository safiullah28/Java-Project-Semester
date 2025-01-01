import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LogOutServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    response.sendRedirect("Login.html?message=Logged out successfully");
  }
}