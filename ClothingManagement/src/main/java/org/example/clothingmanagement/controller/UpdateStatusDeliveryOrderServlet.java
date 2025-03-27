package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.service.PurchaseOrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UpdateStatusDeliveryOrderServlet", value = "/updatestatusdeliveryorder")
public class UpdateStatusDeliveryOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        String status = request.getParameter("status");
        String poid = request.getParameter("poid");
        if ("Processing".equals(status)) {
            try {
                purchaseOrderService.updateStatusPO(poid, status);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<PurchaseOrder> purchaseOrderList = null;
            try {
                purchaseOrderList = purchaseOrderService.getAllPurchaseOrderHaveStatusProcessingAndConfirmed();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            request.getSession().setAttribute("updatepostatussuccessfully", "Update "+poid+" status success, now select Section and Bin to Import Good!");
            request.setAttribute("purchaseOrderList", purchaseOrderList);
            request.getRequestDispatcher("deliveryorderdetail?poID="+poid).forward(request, response);
        }else {
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
            request.getRequestDispatcher("viewdeliveryorder.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}