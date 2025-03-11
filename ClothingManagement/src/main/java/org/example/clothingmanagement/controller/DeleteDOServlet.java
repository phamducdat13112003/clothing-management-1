package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "DeleteDOServlet", value = "/DeleteDOServlet")
public class DeleteDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String doID = request.getParameter("doId");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (doID == null || doID.trim().isEmpty()) {
            response.getWriter().write("<script>alert('DOID không hợp lệ!');history.back();</script>");
            return;
        }

        boolean isDeleted = DeliveryOrderDAO.deleteDOWithDetails(doID);

        if (isDeleted) {
            response.getWriter().write("<script>alert('Xóa DO thành công!'); window.location.href = 'DeliveryOrderServlet';</script>");
        } else {
            response.getWriter().write("<script>alert('Không thể xóa DO vì đã hoàn thành!');history.back(); </script>");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}