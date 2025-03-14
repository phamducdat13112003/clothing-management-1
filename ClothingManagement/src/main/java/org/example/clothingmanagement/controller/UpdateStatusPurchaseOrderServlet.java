package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.service.PurchaseOrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UpdateStatusPurchaseOrderServlet", value = "/updatestatuspurchaseorder")
public class UpdateStatusPurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        String status = request.getParameter("status");
        String poid = request.getParameter("poid");
        System.out.println(poid);
        try {
            purchaseOrderService.updateStatusPO(poid, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<PurchaseOrder> purchaseOrderList = null;
        try {
            purchaseOrderList = purchaseOrderService.getAllPurchaseOrder();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getSession().setAttribute("updatepostatussuccessfully", "Update PO status success have poid " + poid);
        request.setAttribute("purchaseOrderList", purchaseOrderList);
        request.getRequestDispatcher("viewpurchaseorder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}