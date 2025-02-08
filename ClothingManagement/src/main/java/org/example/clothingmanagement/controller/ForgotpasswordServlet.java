package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Email;
import org.example.clothingmanagement.service.AccountService;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

@WebServlet(name = "ForgotpasswordServlet", value = "/forgotpassword")
public class ForgotpasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String emailInput = request.getParameter("email");
        AccountService accountService = new AccountService();
        Email handleEmail = new Email();
        String email = null;
        try {
            email = accountService.checkEmailExist(emailInput);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String message = "";
        String check = null;

        if (email != null) {
            Random random = new Random();
            message = "EXIST - valid email, check your email to have resetcode";
            check = "true";
//            String userName = accountDAO.getUserNameByEmail(email);
            // Tạo số nguyên ngẫu nhiên có 6 chữ số
            Integer code = 100000 + random.nextInt(900000);
            String code_str = code.toString();
            String subject = handleEmail.subjectForgotPass();
            System.out.println(code_str);
            String msgEmail = handleEmail.messageForgotPass( code);
            handleEmail.sendEmail(subject, msgEmail, email);

            //
            session.setAttribute("code", code_str);
            request.setAttribute("email", emailInput);
            request.setAttribute("check", check);
            request.setAttribute("message", message);
            request.getRequestDispatcher("forgot.jsp").forward(request, response);
        } else {
            message = "NOT EXIST - Invalid email";
            check = "false";
            request.setAttribute("message", message);
            request.setAttribute("check", check);
            request.getRequestDispatcher("forgot.jsp").forward(request, response);
        }
    }
}