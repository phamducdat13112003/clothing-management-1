package org.example.clothingmanagement.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.service.AccountService;
import org.example.clothingmanagement.service.EmployeeService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 1024 * 1024 * 5,        // 5 MB
        maxRequestSize = 1024 * 1024 * 10     // 10 MB
)

@WebServlet(name = "ViewProfileServlet", value = "/employee")
public class EmployeeProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in
        if (session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get current account and employee info from session
        Account account = (Account) session.getAttribute("account");
        String employeeId = session.getAttribute("employeeId").toString();

        try {
            // Get fresh data from database
            AccountService accountService = new AccountService();
            EmployeeService employeeService = new EmployeeService();

            Account currentAccount = accountService.getAccountById(account.getId());
            Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);
            System.out.println("Employee Image Path: " + employee.getImage());

            // Set attributes for the JSP
            request.setAttribute("account", currentAccount);
            request.setAttribute("employee", employee);


            // Forward to profile page
            RequestDispatcher dispatcher = request.getRequestDispatcher("profile-info.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in
        if (session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get employee ID from session
        String employeeId = session.getAttribute("employeeId").toString();

        try {
            // Get the uploaded file
            Part filePart = request.getPart("profileImage");
            if (filePart == null || filePart.getSize() == 0) {
                session.setAttribute("imageUpdateError", "No image file was selected");
                response.sendRedirect("profile-info.jsp");
                return;
            }

            // Validate file type
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                session.setAttribute("imageUpdateError", "Only image files are allowed");
                response.sendRedirect("profile-info.jsp");
                return;
            }

            // Get original filename
            String originalFileName = getOriginalFileName(filePart);

            // Define upload directory
            String applicationPath = request.getServletContext().getRealPath("");
            String uploadPath = applicationPath + File.separator + "img";

            // Create directory if it doesn't exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the file with original name
            String filePath = uploadPath + File.separator + originalFileName;
            filePart.write(filePath);

            // Update employee record with new image URL
            EmployeeService employeeService = new EmployeeService();
            Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);

            // Set the relative path to the image
            employee.setImage(originalFileName);

            // Save to database
            employeeService.updateEmployeeImage(employee);

            // Set success message
            session.setAttribute("imageUpdateSuccess", "Profile image updated successfully");

            // Redirect to the employee servlet to reload the profile
            response.sendRedirect(request.getContextPath() + "/employee");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("imageUpdateError", "Error updating profile image: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/employee");
        }
    }

    private String getOriginalFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");

        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                // Extract filename and remove any path information
                String filename = item.substring(item.indexOf("=") + 2, item.length() - 1);
                // Remove potential path information from some browsers
                return filename.substring(filename.lastIndexOf("\\") + 1)
                        .substring(filename.lastIndexOf("/") + 1);
            }
        }
        return "unknown_file" + System.currentTimeMillis(); // Fallback filename
    }


}