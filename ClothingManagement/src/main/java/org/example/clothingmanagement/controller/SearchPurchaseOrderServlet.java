package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.service.PurchaseOrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "SearchPurchaseOrderServlet", value = "/searchpurchaseorder")
public class SearchPurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String txtSearch= request.getParameter("txt").trim();
        PurchaseOrderService purchaseorderservice = new PurchaseOrderService();
        List<PurchaseOrder> purchaseOrderList = null;
        try {
            purchaseOrderList = purchaseorderservice.searchPO(txtSearch);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("purchaseOrderList", purchaseOrderList);
        request.setAttribute("txtS",txtSearch);
        request.getRequestDispatcher("viewpurchaseorder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}