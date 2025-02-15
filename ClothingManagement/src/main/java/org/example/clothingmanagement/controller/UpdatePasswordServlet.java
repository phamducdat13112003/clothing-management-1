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

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/updatePassword")
public class UpdatePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer accountID = (Integer) session.getAttribute("account_id");

        if (accountID == null) {
            request.setAttribute("message", "Lỗi: Không tìm thấy AccountID trong phiên làm việc.");
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Get employee data using account_id
        Employee employee = EmployeeDAO.getEmployeeByAccountId(accountID); // Assuming this method exists

        // Update password
        int employeeID = Integer.parseInt(request.getParameter("employeeID"));
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String reenterPassword = request.getParameter("reenterPassword");

        // Validate password length (at least 8 characters)
        if (newPassword.length() < 8) {
            request.setAttribute("lengthError", "Password must be at least 8 characters long.");
            request.setAttribute("employee", employee); // Send employee data back to JSP
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Validate passwords
        if (!newPassword.equals(reenterPassword)) {
            request.setAttribute("mismatchError", "The passwords do not match.");
            request.setAttribute("employee", employee); // Send employee data back to JSP
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Check if the old password is correct
        AccountDAO accountDAO = new AccountDAO();
        String currentPassword = accountDAO.getPasswordByAccountId(accountID);
        if (!oldPassword.equals(currentPassword)) {
            request.setAttribute("incorrectOldPassError", "Old password is incorrect.");
            request.setAttribute("employee", employee); // Send employee data back to JSP
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Update password
        boolean isUpdated = false;
        try {
            isUpdated = accountDAO.updatePassword(accountID, newPassword);
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

}
