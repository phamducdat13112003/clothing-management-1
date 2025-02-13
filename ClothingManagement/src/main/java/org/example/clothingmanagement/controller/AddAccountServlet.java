package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.Encryption.MD5;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.EmailSender;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.service.AccountService;
import org.example.clothingmanagement.service.EmployeeService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AddAccountServlet", value = "/addaccount")
public class AddAccountServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddAcc</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddAcc at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountService accountService = new AccountService();
        EmployeeService employeeService = new EmployeeService();
        try {
            List<Role> list = accountService.getAllRoles();
            List<String> employeeIds = employeeService.getEmployeeIDsWithoutAccount();
            request.setAttribute("employeeIds", employeeIds);
            request.setAttribute("roles", list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("./addAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountService accountService = new AccountService();
        EmployeeService employeeService = new EmployeeService();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        String roleId = request.getParameter("roleId");
        String employeeId = request.getParameter("employeeId");
        List<Role> list = null;
        List<String> employeeIds =null;
        boolean hasError = false;
        try {
            list = accountService.getAllRoles();
            employeeIds = employeeService.getEmployeeIDsWithoutAccount();
            if (!isRoleCorrect(roleId, employeeId)) {
                request.setAttribute("errorRole", "You have selected the wrong role for this employee.");
                hasError = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!isEmployeeIdValid(employeeId)) {
            request.setAttribute("errorID", "You must choose an employee ID.");
            hasError = true;
        }

        try {
            if (!isEmailExistForEmployee(email, employeeId)) {
                request.setAttribute("errorEmail", "This email is not from an employee.");
                hasError = true;
            }
        } catch (SQLException e) {
            request.setAttribute("errorEmail", "Error checking email for employee.");
            hasError = true;
        }

        if (!isValidPassword(password)) {
            request.setAttribute("errorPassword", "Password must be at least 8 characters, including uppercase letters, numbers, and special characters.");
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("roles", list);
            request.setAttribute("employeeIds", employeeIds);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.getRequestDispatcher("./addAccount.jsp").forward(request, response);
            return;
        }

        // Mã hóa mật khẩu
        String encryptedPassword = MD5.getMd5(password);

        Account account = new Account(email,encryptedPassword, Integer.parseInt(roleId),"True", employeeId);
        try {
            accountService.createAccount(account);
            String subject = "Account Created Successfully!";
            EmailSender.sendEmail(email, subject, password, employeeId);
            List<Account> listAccount = accountService.getAllAccounts();
            request.setAttribute("list", listAccount);
            request.setAttribute("message", "Account successfully added!");
        } catch (SQLException e) {
            request.setAttribute("message", "Failed to add account!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
    }

    private boolean isValidPassword(String password) {
        // Biểu thức chính quy kiểm tra mật khẩu
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        return password.matches(regex);
    }

    // Phương thức kiểm tra Email
    private boolean isEmailExistForEmployee(String email, String employeeId) throws SQLException {
        EmployeeService employeeService = new EmployeeService();
        return employeeService.isEmailExistForEmployee(email, employeeId);
    }

    // Phương thức kiểm tra Role
    private boolean isRoleCorrect(String roleId, String employeeId) throws SQLException {
        EmployeeService employeeService = new EmployeeService();
        int employeeRoleId = employeeService.getRoleIdByEmployeeId(employeeId);
        return Integer.parseInt(roleId) == employeeRoleId;
    }

    // Phương thức kiểm tra Employee ID
    private boolean isEmployeeIdValid(String employeeId) {
        return employeeId != null && !employeeId.trim().isEmpty();
    }
}
