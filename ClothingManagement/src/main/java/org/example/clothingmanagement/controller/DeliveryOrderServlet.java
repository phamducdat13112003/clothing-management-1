package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "DeliveryOrderServlet", value = "/DeliveryOrderServlet")
public class DeliveryOrderServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Supplier> suppliers = DeliveryOrderDAO.getAllSuppliers(); // Lấy danh sách từ DB

        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("po-list.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}