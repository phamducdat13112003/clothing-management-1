package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.service.AccountService;

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
        List<Role> roles = null;
        try {
            account = accountService.getAccountById(Integer.parseInt(accountID));
            roles= accountService.getAllRoles();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("account", account);
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("./editAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountService accountService = new AccountService();
        String accountID = request.getParameter("accountID");
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        String confirmPassword = request.getParameter("confirmPassword").trim();
        String roleID = request.getParameter("roleID");

        if (!isValidPassword(password)) {
            // Thông báo lỗi nếu mật khẩu không hợp lệ
            request.setAttribute("message", "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ in hoa, số và ký tự đặc biệt.");
            request.getRequestDispatcher("./editAccount.jsp").forward(request, response);
            return;
        }
        Account account;
        if (confirmPassword.isEmpty()) {
            // Nếu confirmPassword trống, không thay đổi mật khẩu
            try {
                account = new Account(Integer.parseInt(accountID), email, accountService.getAccountById(Integer.parseInt(accountID)).getPassword(), Integer.parseInt(roleID));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Nếu confirmPassword không trống, kiểm tra xem mật khẩu và confirmPassword có trùng khớp không
            if (!password.equals(confirmPassword)) {
                request.setAttribute("message", "Passwords do not match");
                request.getRequestDispatcher("./editAccount.jsp").forward(request, response);
                return;
            }
            // Nếu mật khẩu và confirmPassword khớp, cập nhật mật khẩu mới
            account = new Account(Integer.parseInt(accountID), email, password);
        }
        try {
            if (accountService.isAccountExist(email, Integer.parseInt(accountID))){
                request.setAttribute("message", "Email is existed!");
                List<Account> list = accountService.getAllAccounts();
                request.setAttribute("list", list);
            } else {
                accountService.updateAccount(account);
                List<Account> list = accountService.getAllAccounts();
                request.setAttribute("list", list);
                request.setAttribute("message", "Update successful");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);

    }

    private boolean isValidPassword(String password) {
        // Biểu thức chính quy kiểm tra mật khẩu
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";

        // Kiểm tra mật khẩu bằng biểu thức chính quy
        return password.matches(regex);
    }
}
