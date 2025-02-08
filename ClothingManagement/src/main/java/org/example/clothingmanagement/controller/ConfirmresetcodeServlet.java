package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.service.AccountService;

import java.io.IOException;

@WebServlet(name = "ConfirmresetcodeServlet", value = "/confirmresetcode")
public class ConfirmresetcodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountService accountService = new AccountService();
        String resetCode = request.getParameter("resetcode");
        String code = (String) session.getAttribute("code");
        String email = request.getParameter("email");
        String message = (String) request.getAttribute("message");
        String check = (String) request.getAttribute("check");
        if (code.equalsIgnoreCase(resetCode)) {
            check = "true";
//            String userName = accountDAO.getUserNameByEmail(email);

            request.removeAttribute("code");
//            request.setAttribute("uName", userName);
            request.setAttribute("check", check);
            request.setAttribute("email", email);
            request.getRequestDispatcher("newpassword.jsp").forward(request, response);
        } else {
            check = "true";
            message = "Sorry, reset code incorrect";
            session.setAttribute("code", code);
//            request.setAttribute("email", email);
            request.setAttribute("check", check);
            request.setAttribute("message", message);
            request.getRequestDispatcher("forgot.jsp").forward(request, response);
        }
    }
}