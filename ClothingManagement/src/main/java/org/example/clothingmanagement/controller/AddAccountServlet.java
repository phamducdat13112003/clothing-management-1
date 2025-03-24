package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.Encryption.MD5;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Email;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.service.AccountService;
import org.example.clothingmanagement.service.EmployeeService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
            List<Employee> employees = employeeService.getEmployeeIDsWithoutAccount();
            request.setAttribute("employees", employees);
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
        String roleId = request.getParameter("roleId");
        String employeeId = request.getParameter("employeeId");
        LocalDateTime lastUpdate = LocalDateTime.now();
        String accountId = null;
        try {
            accountId = generateNewAccountId();
        } catch (SQLException e) {
            request.setAttribute("message", "Cannot create accountID. Please check again.");
        }
        List<Role> list = null;
        List<Employee> employees = null;
        boolean hasError = false;
        int page = 1;
        int pageSize = 5;
        int totalAccounts = 0;
        try {
            list = accountService.getAllRoles();
            employees = employeeService.getEmployeeIDsWithoutAccount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!isEmployeeIdValid(employeeId)) {
            request.setAttribute("errorID", "You must choose an employee ID.");
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("roles", list);
            request.setAttribute("employees", employees);
            request.getRequestDispatcher("./addAccount.jsp").forward(request, response);
            return;
        }
        String password = generateRandomPassword();
        String encryptedPassword = MD5.getMd5(password);

        Account account = new Account(accountId, email, encryptedPassword, Integer.parseInt(roleId), lastUpdate, "Active", employeeId);
        try {
            try {
                accountService.createAccount(account);
            } catch (SQLException e) {
                request.setAttribute("message", "Cannot create account. Please check again.");
                request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
            }
            totalAccounts = accountService.getTotalAccounts();
            String subject = "Account Created Successfully!";
            Email emailSender = new Email();
            emailSender.sendEmail(email, subject, password, employeeId);
            List<Account> listAccount = accountService.getAccountsByPage(page, pageSize);
            request.setAttribute("list", listAccount);
            request.setAttribute("messageSuccess", "Account successfully added!");
        } catch (SQLException e) {
            request.setAttribute("message", "Appear error can't add account");
            request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
        } catch (MessagingException e) {
            request.setAttribute("message", "Account successfully added but failed to send email!");
            request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
        }
        int totalPages = (int) Math.ceil((double) totalAccounts / pageSize);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
    }

    // Phương thức kiểm tra Employee ID
    private boolean isEmployeeIdValid(String employeeId) {
        return employeeId != null && !employeeId.trim().isEmpty();
    }

    private static String generateRandomPassword() {
        final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        final String DIGITS = "0123456789";
        final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?/";
        final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;
        final int PASSWORD_LENGTH = 8;
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        // Đảm bảo có ít nhất 1 ký tự của từng loại
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Điền các ký tự còn lại ngẫu nhiên
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // Xáo trộn mật khẩu để tránh thứ tự cố định
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }

    public String generateNewAccountId() throws SQLException {
        AccountService accountService = new AccountService();
        String prefix = "ACC00"; // Luôn có "ACC00"
        int maxId = accountService.getMaxAccountId(); // Lấy số lớn nhất từ database
        return prefix + (maxId + 1); // Tăng lên 1 và ghép vào
    }

}
