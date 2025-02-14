package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.Encryption.MD5;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Email;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.service.AccountService;
import org.example.clothingmanagement.service.RoleService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "EditAccountServlet", value = "/editaccount")
public class EditAccountServlet extends HttpServlet {

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
        String accountID = request.getParameter("accountId");
        Account account = new Account();
        AccountService accountService = new AccountService();
        List<Role> list = null;
        RoleService roleService = new RoleService();
        try {
            list=roleService.getAllRoles();
            account = accountService.getAccountById(accountID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("roles", list);
        request.setAttribute("account", account);
        request.getRequestDispatcher("./editAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountService accountService = new AccountService();
        List<Role> list= null;
        RoleService roleService = new RoleService();
        Account account = new Account();
        String accountID = request.getParameter("accountID");
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        String roleId = request.getParameter("roleId");
        String status = request.getParameter("status");

        int page = 1;
        int pageSize = 5; // Số dòng trên mỗi trang
        try {
            list = roleService.getAllRoles();
            account = accountService.getAccountById(accountID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!isValidPassword(password)) {
            // Thông báo lỗi nếu mật khẩu không hợp lệ
            request.setAttribute("roles", list);
            request.setAttribute("account", account);
            request.setAttribute("errorPassword", "Password must be at least 8 characters, including uppercase letters, numbers, and special characters.");
            request.getRequestDispatcher("./editAccount.jsp").forward(request, response);
            return;
        }
        Account existingAccount;
        try {
             existingAccount = accountService.getAccountById(accountID);
            if (existingAccount == null) {
                request.setAttribute("roles", list);
                request.setAttribute("account", account);
                request.setAttribute("message", "Account not found");
                request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Mã hóa mật khẩu
        String encryptedPassword = MD5.getMd5(password);

        Account updatedAccount = new Account(accountID ,email, encryptedPassword, Integer.parseInt(roleId), status);
        List<Account> listAccount= null;
        int totalAccounts = 0;
        try {
            accountService.updateAccount(updatedAccount);
            listAccount = accountService.getAccountsByPage(page, pageSize);
            totalAccounts = accountService.getTotalAccounts();
            Email emailSender = new Email();
            emailSender.sendPasswordChangedEmail(email, password);
        } catch (SQLException e) {
            request.setAttribute("message", "Can't update account");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        int totalPages = (int) Math.ceil((double) totalAccounts / pageSize);

        request.setAttribute("list", listAccount);
        request.setAttribute("messageSuccess", "Update successful");
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
    }

    private boolean isValidPassword(String password) {
        // Biểu thức chính quy kiểm tra mật khẩu
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";

        // Kiểm tra mật khẩu bằng biểu thức chính quy
        return password.matches(regex);
    }
}
