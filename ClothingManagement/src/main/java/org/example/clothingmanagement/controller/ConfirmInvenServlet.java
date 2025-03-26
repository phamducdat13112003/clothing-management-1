package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.InventoryDocDAO;

import java.io.IOException;


@WebServlet(name = "ConfirmInvenServlet", value = "/ConfirmInvenServlet")
public class ConfirmInvenServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String inventoryDocId = request.getParameter("inventoryDocId");
        String binId = request.getParameter("binId");
        String need = request.getParameter("need");
        if(need.equals("daydu")){
            InventoryDocDAO.changeBinStatus(binId,1);
            InventoryDocDAO.updateInventoryDocStatus(inventoryDocId,"Done");

        }else {
            InventoryDocDAO.updateInventoryDocStatus(inventoryDocId, "Confirmed");
        }
        response.sendRedirect("ViewInventoryDocList");


    }


}