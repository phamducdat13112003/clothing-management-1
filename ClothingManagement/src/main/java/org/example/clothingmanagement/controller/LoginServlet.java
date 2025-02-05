package org.example.clothingmanagement.controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.service.AccountService;
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
        String remember = request.getParameter("remember");
        Account account = null;
        try {
            account = accountDAO.findAccount(email,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cookie cookie_email = new Cookie("email", email);
        Cookie cookie_password = new Cookie("password", password);
        if (account == null) {
            session.setAttribute("error_login", "your information is incorrect!");
            response.addCookie(cookie_email);
            response.addCookie(cookie_password);
            response.sendRedirect("login.jsp");
        } else {
            session.removeAttribute("error_login");
            session.setAttribute("account", account);
            session.setAttribute("role", account.getRoleId());
            session.setAttribute("account_id", account.getId());
            session.setMaxInactiveInterval(60 * 60 * 24);
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
                response.sendRedirect("Dashboard.jsp");
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