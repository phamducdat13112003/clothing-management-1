package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;
import org.example.clothingmanagement.service.DeliveryOrderService;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "POListOpen", value = "/POListOpen")
public class POListOpen extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<PurchaseOrder> poList = DeliveryOrderDAO.getPurchaseOrderOpen();

        // Map lưu SupplierName và EmployeeName để tránh truy vấn lặp lại
        Map<String, String> supplierNames = new HashMap<>();
        Map<String, String> employeeNames = new HashMap<>();

        for (PurchaseOrder po : poList) {
            String supplierID = po.getSupplierID();
            String createdBy = po.getCreatedBy();

            // Kiểm tra null để tránh lỗi
            if (supplierID != null && !supplierNames.containsKey(supplierID)) {
                supplierNames.put(supplierID, DeliveryOrderDAO.getSupplierNameByID(supplierID));
            }

            if (createdBy != null && !employeeNames.containsKey(createdBy)) {
                employeeNames.put(createdBy, DeliveryOrderDAO.getEmployeeNameByEmployeeID(createdBy));
            }
        }

        request.setAttribute("poList", poList);
        request.setAttribute("supplierNames", supplierNames);
        request.setAttribute("employeeNames", employeeNames);
        request.getRequestDispatcher("po-list.jsp").forward(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}