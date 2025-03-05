package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.DeliveryOrder;
import org.example.clothingmanagement.entity.DeliveryOrderDetail;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;
import org.example.clothingmanagement.repository.DeliveryOrderDetailDAO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet(name = "GetDOServlet", value = "/GetDOServlet")
public class GetDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String supplierID = request.getParameter("supplier");
        String poID = request.getParameter("poID");
        String createdBy = request.getParameter("createdBy");
        List<Supplier> suppliers = DeliveryOrderDAO.getAllSuppliers();
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        DeliveryOrderDAO dao = new DeliveryOrderDAO();


        List<DeliveryOrder> activeDOs = DeliveryOrderDAO.filterDOs(supplierID,  startDate, endDate, poID, createdBy);

        request.setAttribute("activeDOs", activeDOs);
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("do-list.jsp").forward(request, response);
    }




}