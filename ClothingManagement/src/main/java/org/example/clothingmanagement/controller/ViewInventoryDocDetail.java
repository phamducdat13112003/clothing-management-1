package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.InventoryDocDetail;
import org.example.clothingmanagement.repository.InventoryDocDAO;
import org.example.clothingmanagement.repository.InventoryDocDetailDAO;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "ViewInventoryDocDetail", value = "/ViewInventoryDocDetail")
public class ViewInventoryDocDetail extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String binId = request.getParameter("binId");
        String inventoryDocId = request.getParameter("inventoryDocId");
        List<Employee> employeeList = InventoryDocDAO.getAllEmployee();

        List<InventoryDocDetail> listInvenDoc = InventoryDocDetailDAO.getInventoryDocDetailsByDocID(inventoryDocId);

        request.setAttribute("listInvenDoc", listInvenDoc);
        request.setAttribute("inventoryDocId", inventoryDocId);
        request.setAttribute("employeeList", employeeList);
        request.setAttribute("binId", binId);
        request.getRequestDispatcher("viewInventoryDocDetail.jsp").forward(request,response);


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}