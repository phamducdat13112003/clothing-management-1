package org.example.clothingmanagement.controller;import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.Encryption.MD5;
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
        if (!isValidPassword(newpass)) {
            request.setAttribute("error", "Password must have at least 8 characters, including one uppercase letter, one number, and one special character.");
            request.getRequestDispatcher("newpassword.jsp").forward(request, response);
            return;
        }
        if (!confirmpass.equals(newpass)) {
            request.setAttribute("error", "Passwords do not match, please try again");
            request.getRequestDispatcher("newpassword.jsp").forward(request, response);
        }else {
            MD5.getMd5(newpass);
            AccountService accountService = new AccountService();
            try {
                String MD5newpassword = MD5.getMd5(newpass);
                accountService.updatePassByEmail(MD5newpassword,email);
                System.out.println(newpass);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            request.getSession().setAttribute("successfully", "Password changed successfully! Please log in again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        return password.matches(regex);
    }
}