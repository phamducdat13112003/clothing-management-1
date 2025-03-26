package org.example.clothingmanagement.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.Encryption.MD5;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.service.AccountService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/changePassword")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in
        if (session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get employeeID from session
        Integer employeeID = (Integer) session.getAttribute("employeeID");

        // Add employeeID to request scope for the JSP
        request.setAttribute("employeeID", employeeID);




        // Forward to change password page
        RequestDispatcher dispatcher = request.getRequestDispatcher("changePassword.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in
        if (session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }



        // Get current account
        Account account = (Account) session.getAttribute("account");

        // Get form data
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Hash the current password for comparison
        String hashedCurrentPassword = MD5.getMd5(currentPassword);

        System.out.println("Change Password Request:");
        System.out.println("User ID: " + account.getId());
        System.out.println("Current Password Hashed: " + hashedCurrentPassword);

        try {
            AccountService accountService = new AccountService();
            Account currentAccount = accountService.getAccountById(account.getId());

            // Verify current password
            if (!currentAccount.getPassword().equals(hashedCurrentPassword)) {
                System.out.println("Password Verification Failed: Incorrect Current Password");
                request.setAttribute("passwordError", "Current password is incorrect");
                RequestDispatcher dispatcher = request.getRequestDispatcher("changePassword.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Verify new password matches confirmation
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Password Verification Failed: New Password Mismatch");
                request.setAttribute("passwordError", "New password and confirmation do not match");
                RequestDispatcher dispatcher = request.getRequestDispatcher("changePassword.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Hash the new password
            String hashedNewPassword = MD5.getMd5(newPassword);

            System.out.println("New Password Hashed: " + hashedNewPassword);

            // Update password
            accountService.updatePassword(currentAccount.getId(), hashedNewPassword);

            System.out.println("Password Updated Successfully for User ID: " + currentAccount.getId());

            // Update session with new password
            currentAccount.setPassword(hashedNewPassword);
            session.setAttribute("account", currentAccount);

            // Update method
            request.setAttribute("passwordSuccess", "Password changed successfully");
            RequestDispatcher dispatcher = request.getRequestDispatcher("profile-info.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.out.println("SQL Error During Password Change:");
            e.printStackTrace();
            request.setAttribute("passwordError", "Error changing password: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("changePassword.jsp");
            dispatcher.forward(request, response);
        }
    }
}
