package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.InventoryDoc;
import org.example.clothingmanagement.service.InventoryDocService;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "ViewInventoryDocList", value = "/ViewInventoryDocList")
public class ViewInventoryDocList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        InventoryDocService service = new InventoryDocService();
        List<InventoryDoc> listDoc= service.getAllInventoryDocs();

        request.setAttribute("listDoc", listDoc);

        request.getRequestDispatcher("inventoryDocList.jsp").forward(request,response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}