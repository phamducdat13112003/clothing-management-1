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


@WebServlet(name = "DeliveryOrderConfirmServlet", value = "/DeliveryOrderConfirmServlet")
public class DeliveryOrderConfirmServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, IOException {
        String action = request.getParameter("action"); // Kiểm tra người dùng chọn Filter hay Select All
        String supplierID = request.getParameter("supplier");
        String poID = request.getParameter("poID");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String createdBy = request.getParameter("createdBy");
        List<Supplier> suppliers = DeliveryOrderDAO.getAllSuppliers();
        List<DeliveryOrder> deliveryOrders;

        if ("selectAll".equals(action)) {
            // Nếu chọn "Select All", gọi filterPO mà không truyền điều kiện
            deliveryOrders = DeliveryOrderDAO.filterDOs(null, null, null, null,null);
        } else {
            // Nếu chọn "Filter", gọi filterPO với các giá trị từ form
            deliveryOrders = DeliveryOrderDAO.filterDOs(supplierID, startDate, endDate, poID,createdBy);
        }
        Map<String, String> createdByList = new HashMap<>();
        Map<String, String> recipientList = new HashMap<>();

        for (DeliveryOrder dos : deliveryOrders) {
            String createdBi = dos.getCreatedBy();
            String recipient = dos.getRecipient();

            if (createdBy != null && !recipientList.containsKey(recipient)) {
                recipientList.put(recipient, DeliveryOrderDAO.getEmployeeNameByEmployeeID(recipient));
            }

            if (createdBy != null && !createdByList.containsKey(createdBi)) {
                createdByList.put(createdBi, DeliveryOrderDAO.getEmployeeNameByEmployeeID(createdBi));
            }
        }

        request.setAttribute("deliveryOrders", deliveryOrders);
        request.setAttribute("createdByList", createdByList);
        request.setAttribute("recipientList", recipientList);
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("ViewDOListConfirm.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}