package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.DeliveryOrderDetail;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;
import org.example.clothingmanagement.repository.DeliveryOrderDetailDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;


@WebServlet(name = "AddDOServlet", value = "/AddDOServlet")
public class AddDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // **1. Nhận danh sách PO ID**
        List<String> poIDList = request.getParameterValues("poID") != null
                ? new ArrayList<>(Arrays.asList(request.getParameterValues("poID")))
                : new ArrayList<>();

        if (poIDList.isEmpty()) {
            request.setAttribute("error", "No purchase order selected.");
            request.getRequestDispatcher("deliveryOrder.jsp").forward(request, response);
            return;
        }

        // **2. Nhận dữ liệu từ request**
        String plannedShippingDateStr = request.getParameter("plannedShippingDate");
        String accountID = request.getParameter("createBy");
        String createdBy = DeliveryOrderDAO.getEmployeeIDByAccountID(accountID);
        Date plannedShippingDate = Date.valueOf(plannedShippingDateStr);
        Date receiptDate = new Date(System.currentTimeMillis()); // Ngày hiện tại làm ReceiptDate
        boolean status = true; // Trạng thái mặc định

        // **3. Tạo DO và lưu vào database**
        for (String poID : poIDList) {
            String newDOID = null;
            try {
                newDOID = DeliveryOrderDAO.generateDOID();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            boolean isDOAdded = DeliveryOrderDAO.addDO(newDOID, plannedShippingDate, receiptDate, poID, createdBy, null, status);

            if (!isDOAdded) {
                request.setAttribute("error", "Failed to create Delivery Order for POID: " + poID);
                request.getRequestDispatcher("deliveryOrder.jsp").forward(request, response);
                return;
            }

            // **4. Thêm danh sách sản phẩm vào bảng DODetail**
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                if (paramName.startsWith("quantity_")) {
                    String productDetailID = paramName.substring(9); // Lấy ID từ "quantity_{productDetailID}"
                    int quantity = Integer.parseInt(request.getParameter(paramName));

                    // Tạo ID tự động cho DODetail
                    String doDetailID = null;
                    try {
                        doDetailID = DeliveryOrderDetailDAO.generateDODetailID();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    // Thêm vào bảng DODetail
                    DeliveryOrderDetailDAO.addDODetail(doDetailID, productDetailID, quantity, newDOID);
                }
            }
        }

        // **5. Chuyển hướng sau khi thêm thành công**

        response.sendRedirect("deliveryOrder.jsp");
    }






}