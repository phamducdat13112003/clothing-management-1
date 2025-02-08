package org.example.clothingmanagement.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.AccountDAO;
import org.example.clothingmanagement.repository.EmployeeDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("updatePassword")
public class UpdatePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        int employeeID = Integer.parseInt(request.getParameter("employeeID"));
        int accountId = EmployeeDAO.getAccountIdByEmployeeId(employeeID);
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String reenterPassword = request.getParameter("reenterPassword");


        if (accountId == 0 || oldPassword == null || newPassword == null || reenterPassword == null ||
                oldPassword.isEmpty() || newPassword.isEmpty() || reenterPassword.isEmpty()) {
            request.setAttribute("emptyError", "Please fill in all fields.");
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Password matching check
        if (!newPassword.equals(reenterPassword)) {
            request.setAttribute("mismatchError", "The passwords do not match.");
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        try {
            // Initialize AccountDAO to access the account data
            AccountDAO accountDAO = new AccountDAO();

            // Check if the old password matches the current password in the database
            String currentPassword = accountDAO.getPasswordByAccountId(accountId);  // Assuming getPasswordByAccountId() method exists

            if (!oldPassword.equals(currentPassword)) {
                request.setAttribute("incorrectOldPassError", "Old password is incorrect.");
                request.getRequestDispatcher("profile-info.jsp").forward(request, response);
                return;
            }

            // Update password if validation passes
            boolean isUpdated = accountDAO.updatePassword(accountId, newPassword);

            if (isUpdated) {
                request.setAttribute("message", "Password updated successfully.");
            } else {
                request.setAttribute("message", "Failed to update the password. Please try again.");
            }

            // Forward to the same JSP to display the result
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid account ID.");
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("message", "An error occurred while updating the password.");
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            e.printStackTrace();
        }
    }

}
