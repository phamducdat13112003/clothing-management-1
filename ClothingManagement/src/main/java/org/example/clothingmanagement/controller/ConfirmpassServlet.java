package org.example.clothingmanagement.controller;import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.service.AccountService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ConfirmpassServlet", value = "/confirmpass")
public class ConfirmpassServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newpass = request.getParameter("password");
        String confirmpass = request.getParameter("cfpassword");
        String email = request.getParameter("email");
        if (!confirmpass.equals(newpass)) {
            request.setAttribute("error", "Passwords do not match, please try again");
            request.getRequestDispatcher("newpassword.jsp").forward(request, response);
        }else {
            AccountService accountService = new AccountService();
            try {
                accountService.updatePassByEmail(newpass,email);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            request.getSession().setAttribute("successfully", "Password changed successfully! Please log in again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}