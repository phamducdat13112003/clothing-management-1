package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.service.EmployeeService;
import org.example.clothingmanagement.service.PurchaseOrderService;

import java.sql.SQLException;
import java.util.List;
import java.io.IOException;

@WebServlet(name = "ViewPurchaseOrderServlet", value = "/viewpurchaseorder")
public class ViewPurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();

        List<PurchaseOrder> purchaseOrderList = null;
        try {
            purchaseOrderList = purchaseOrderService.getAllPurchaseOrder();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("purchaseOrderList", purchaseOrderList);
        request.getRequestDispatcher("viewpurchaseorder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}