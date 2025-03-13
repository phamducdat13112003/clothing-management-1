package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;


@WebServlet(name = "ConfirmDOServlet", value = "/ConfirmDOServlet")
public class ConfirmDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String doID = request.getParameter("doId");
        Date receiptDate = Date.valueOf(LocalDate.now());
        String accountID = request.getParameter("createdBy");
        String createdBy = DeliveryOrderDAO.getEmployeeIDByAccountID(accountID);

        DeliveryOrderDAO dao = new DeliveryOrderDAO();
        boolean update = dao.confirmDOForWS(doID, receiptDate, createdBy);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if(update) {
            response.getWriter().write("<script>alert('Done DO thành công!'); window.location.href = 'DeliveryOrderConfirmServlet';</script>");
        }else{
            response.getWriter().write("<script>alert('Không thể hoàn thành DO vì lỗi!');history.back(); </script>");

        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}