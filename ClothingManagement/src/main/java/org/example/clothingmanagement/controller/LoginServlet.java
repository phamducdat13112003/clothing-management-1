package org.example.clothingmanagement.controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.Encryption.MD5;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.service.AccountService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountService accountDAO = new AccountService();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String hashedPassword = MD5.getMd5(password); // Mã hóa mật khẩu trước khi kiểm tra
        String remember = request.getParameter("remember");
        Account account = null;
        try {
            account = accountDAO.findAccount(email, hashedPassword); // Kiểm tra với mật khẩu đã mã hóa
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cookie cookie_email = new Cookie("cookie_email", email);
        Cookie cookie_password = new Cookie("cookie_password", password);
        if (account == null) {
            session.setAttribute("error_login", "your information is incorrect!");
            response.addCookie(cookie_email);
            response.addCookie(cookie_password);
            response.sendRedirect("login.jsp");
        } else if (account.getStatus().equals("Inactive")) {
            session.setAttribute("error_inactive", "Account is inactive!");
            response.sendRedirect("login.jsp");
        }else {
                session.removeAttribute("error_login");
                response.addCookie(cookie_email);
                response.addCookie(cookie_password);
                session.setAttribute("account", account);
                session.setAttribute("role", account.getRoleId());
                session.setAttribute("account_id", account.getId());
                session.setAttribute("email", account.getEmployeeId());
            System.out.println("role account id"+ account.getRoleId() + account.getId() + account.getEmployeeId());
                session.setMaxInactiveInterval(60 * 60 * 24);

            String employeeId = account.getEmployeeId();
            System.out.println("employeeId: " + employeeId);

            // Fetch the employee name based on the EmployeeID from Account
            try {
                String employeeName = accountDAO.getEmployeeNameById(account.getEmployeeId());
                session.setAttribute("employeeID", account.getEmployeeId());
                session.setAttribute("employeeName", employeeName); // Store employee name in session
            } catch (SQLException e) {
                e.printStackTrace();
            }

                if (remember != null && remember.equalsIgnoreCase("1")) {
                    cookie_email.setMaxAge(60 * 60);
                    cookie_password.setMaxAge(60 * 60);
                    response.addCookie(cookie_password);
                } else {
                    cookie_email.setMaxAge(0);
                    cookie_password.setMaxAge(0);
                    response.addCookie(cookie_password);
                }
                if (account.getRoleId() == 1) {//Manager
                    response.sendRedirect("Dashboard.jsp");
                } else if (account.getRoleId() == 2) {//Sale
                    response.sendRedirect("viewpurchaseorder");
                } else if (account.getRoleId() == 3) {//Storage Staff
                    response.sendRedirect("Dashboard.jsp");
                } else if (account.getRoleId() == 4) {//Admin
                    response.sendRedirect("account");
                }
                response.addCookie(cookie_email);
                response.addCookie(cookie_password);

            }
        }
    }
