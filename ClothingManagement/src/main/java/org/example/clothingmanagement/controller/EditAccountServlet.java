package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.service.AccountDAO;

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
        AccountDAO dao = new AccountDAO();
        try {
            account = dao.getAccountById(Integer.parseInt(accountID));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("account", account);
        request.getRequestDispatcher("./editAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDAO dao = new AccountDAO();
        String accountID = request.getParameter("accountID");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Account account = new Account(Integer.parseInt(accountID), email, password);
        try {
            if (dao.isAccountExist(email)) {
                request.setAttribute("message", "Email is existed!");
                List<Account> list = dao.getAllAccount();
                request.setAttribute("list", list);
            } else {
                dao.updateAccount(account);
                List<Account> list = dao.getAllAccount();
                request.setAttribute("list", list);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);

    }
}
