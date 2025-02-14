package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.service.AccountService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "DeleteAccountServlet", value = "/deleteaccount")
public class DeleteAccountServlet extends HttpServlet {

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
        AccountService accountService = new AccountService();
        int page = 1;
        int pageSize = 5;
        int totalAccounts = 0;
        if(accountID != null) {
            try {
                boolean isDeleted= accountService.deleteAccount(accountID);
                totalAccounts = accountService.getTotalAccounts();
                if (isDeleted) {
                    List<Account> list= accountService.getAccountsByPage(page, pageSize);
                    request.setAttribute("messageSuccess", "Account deleted");
                    request.setAttribute("list", list);
                } else {
                    List<Account> list= accountService.getAccountsByPage(page, pageSize);
                    request.setAttribute("message", "Failed to delete account");
                    request.setAttribute("list", list);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        int totalPages = (int) Math.ceil((double) totalAccounts / pageSize);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./manageAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
