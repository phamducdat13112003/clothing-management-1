package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.Encryption.MD5;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.service.AccountService;
import org.example.clothingmanagement.service.EmployeeService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountService accountDAO = new AccountService();
        EmployeeService employeeService = new EmployeeService();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String hashedPassword = MD5.getMd5(password); // Mã hóa mật khẩu trước khi kiểm tra
        String remember = request.getParameter("remember");

        Account account = null;
        Employee employee = null;

        try {
            account = accountDAO.findAccount(email, hashedPassword); // Kiểm tra với mật khẩu đã mã hóa
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Kiểm tra nếu account == null thì không tiếp tục xử lý
        if (account == null) {
            session.setAttribute("error_login", "Your information is incorrect!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            employee = employeeService.getEmployeeByEmployeeId(account.getEmployeeId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Kiểm tra nếu employee == null
        if (employee == null) {
            session.setAttribute("error_login", "Employee information not found!");
            response.sendRedirect("login.jsp");
            return;
        }

        System.out.println(employee);

        Cookie cookie_email = new Cookie("cookie_email", email);
        Cookie cookie_password = new Cookie("cookie_password", password);

        if (account.getStatus().equals("Inactive")) {
            session.setAttribute("error_inactive", "Account is inactive!");
            response.sendRedirect("login.jsp");
            return;
        }

        session.removeAttribute("error_login");
        response.addCookie(cookie_email);
        response.addCookie(cookie_password);
        session.setAttribute("account", account);
        session.setAttribute("role", account.getRoleId());
        session.setAttribute("account_id", account.getId());
        session.setAttribute("employeeId", account.getEmployeeId());
        session.setAttribute("employeeName", employee.getEmployeeName());
        session.setAttribute("warehouseId", employee.getWarehouseID());
        session.setMaxInactiveInterval(60 * 60 * 24);

        if (remember != null && remember.equalsIgnoreCase("1")) {
            cookie_email.setMaxAge(60 * 60);
            cookie_password.setMaxAge(60 * 60);
            response.addCookie(cookie_password);
        } else {
            cookie_email.setMaxAge(0);
            cookie_password.setMaxAge(0);
            response.addCookie(cookie_password);
        }

        if (account.getRoleId() == 1) {// Manager
            response.sendRedirect("dashboard");
        } else if (account.getRoleId() == 2) {// Purchase Order Staff
            response.sendRedirect("viewpurchaseorder");
        } else if (account.getRoleId() == 4) {// Storage Staff
            response.sendRedirect("viewdeliveryorder");
        } else if (account.getRoleId() == 3) {// Admin
            response.sendRedirect("account");
        }
    }

}

