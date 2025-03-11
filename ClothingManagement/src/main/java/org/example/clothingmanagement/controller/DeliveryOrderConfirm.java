package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.DeliveryOrder;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "DeliveryOrderConfirm", value = "/DeliveryOrderConfirm")
public class DeliveryOrderConfirm extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Supplier> suppliers = DeliveryOrderDAO.getAllSuppliers(); // Lấy danh sách từ DB
        List<DeliveryOrder> deliveryOrders =  DeliveryOrderDAO.getAllDOs();

        Map<String, String> createdByList = new HashMap<>();
        Map<String, String> recipientList = new HashMap<>();

        for (DeliveryOrder dos : deliveryOrders) {
            String createdBy = dos.getCreatedBy();
            String recipient = dos.getRecipient();

            if (createdBy != null && !recipientList.containsKey(recipient)) {
                recipientList.put(recipient, DeliveryOrderDAO.getEmployeeNameByEmployeeID(recipient));
            }

            if (createdBy != null && !createdByList.containsKey(createdBy)) {
                createdByList.put(createdBy, DeliveryOrderDAO.getEmployeeNameByEmployeeID(createdBy));
            }
        }

        request.setAttribute("deliveryOrders", deliveryOrders);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("createdByList", createdByList);
        request.setAttribute("recipientList", recipientList);
        request.getRequestDispatcher("ViewDOListConfirm.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}