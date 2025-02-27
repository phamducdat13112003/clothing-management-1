package org.example.clothingmanagement.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.repository.AccountDAO;
import org.example.clothingmanagement.repository.EmployeeDAO;
import org.example.clothingmanagement.Encryption.MD5;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/updatePassword")
public class UpdatePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String accountID = (String) session.getAttribute("account_id");

        if (accountID == null) {
            request.setAttribute("message", "Lỗi: Không tìm thấy AccountID trong phiên làm việc.");
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Get employee data using account_id
        Employee employee = EmployeeDAO.getEmployeeByAccountId(accountID); // Assuming this method exists

        // Get input data
        String employeeID = request.getParameter("employeeID");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String reenterPassword = request.getParameter("reenterPassword");

        // Validate password length and complexity
        if (!isValidPassword(newPassword)) {
            request.setAttribute("validError", "Password must have at least 8 characters, including one uppercase letter, one number, and one special character.");
            request.setAttribute("employee", employee); // Send employee data back to JSP
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Validate passwords match
        if (!newPassword.equals(reenterPassword)) {
            request.setAttribute("mismatchError", "The passwords do not match.");
            request.setAttribute("employee", employee); // Send employee data back to JSP
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Check if the old password is correct (compare MD5 hashes)
        AccountDAO accountDAO = new AccountDAO();
        String currentPasswordHash = accountDAO.getPasswordByAccountId(accountID); // Get hashed password from DB
        String oldPasswordHash = MD5.getMd5(oldPassword); // Hash the entered old password

        if (!oldPasswordHash.equals(currentPasswordHash)) {
            request.setAttribute("incorrectOldPassError", "Old password is incorrect.");
            request.setAttribute("employee", employee); // Send employee data back to JSP
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Update password (update with hashed new password)
        String newPasswordHash = MD5.getMd5(newPassword); // Hash the new password
        boolean isUpdated = false;
        try {
            isUpdated = accountDAO.updatePassword(accountID, newPasswordHash);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (isUpdated) {
            request.setAttribute("message", "Password updated successfully.");
        } else {
            request.setAttribute("message", "Failed to update the password. Please try again.");
        }

        // Set employee data again to request to preserve personal info
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("profile-info.jsp").forward(request, response);
    }

    // Password validation function: checks length, uppercase, number, and special character
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        return password.matches(regex);
    }
}

