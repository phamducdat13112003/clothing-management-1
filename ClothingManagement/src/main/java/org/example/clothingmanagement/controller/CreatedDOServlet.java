package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.PurchaseOrderDetail;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;
import org.example.clothingmanagement.repository.DeliveryOrderDetailDAO;
import java.sql.Date;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet(name = "CreatedDOServlet", value = "/CreatedDOServlet")
public class CreatedDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // **1. Nhận danh sách PO ID**
        String poID = request.getParameter("poID");
        if (poID.isEmpty()) {
            request.setAttribute("error", "No purchase order selected.");
            request.getRequestDispatcher("deliveryOrder.jsp").forward(request, response);
            return;
        }

        // **2. Nhận dữ liệu từ request**
        String plannedShippingDateStr = request.getParameter("plannedShippingDate");
        String accountID = request.getParameter("createBy");
        String createdBy = DeliveryOrderDAO.getEmployeeIDByAccountID(accountID);
        Date plannedShippingDate = Date.valueOf(plannedShippingDateStr);
        Date receiptDate = Date.valueOf("1970-01-01"); // Ngày hiện tại làm ReceiptDate
        boolean status = false; // Trạng thái mặc định

        // **3. Tạo DO và lưu vào database**

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
        // **5. Chuyển hướng sau khi thêm thành công**
        response.sendRedirect("DeliveryOrderServlet");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<String> poIDList = request.getParameterValues("poID") != null
                ? new ArrayList<>(Arrays.asList(request.getParameterValues("poID")))
                : new ArrayList<>();
        String poID = request.getParameter("poID");
        if (poIDList.isEmpty()) {
            request.setAttribute("error", "No purchase order selected.");
            request.getRequestDispatcher("deliveryOrder.jsp").forward(request, response);
            return;
        }

        // Lấy danh sách chi tiết PO từ database
        List<PurchaseOrderDetail> poDetails = DeliveryOrderDAO.getPODetailsByPOIDs(poIDList);

        // Trích xuất danh sách ProductDetailID từ poDetails
        List<String> productDetailIDs = poDetails.stream()
                .map(PurchaseOrderDetail::getProductDetailID)
                .distinct()
                .collect(Collectors.toList());

        // Lấy thông tin sản phẩm chi tiết
        List<Map<String, Object>> productDetails = DeliveryOrderDAO.getProductDetailsByProductDetailIDs(productDetailIDs);
        Map<String, Integer> remainingQuantities = DeliveryOrderDAO.getRemainingQuantities(poID);

        request.setAttribute("remainingQuantities", remainingQuantities);
        request.setAttribute("poDetails", poDetails);
        request.setAttribute("productDetails", productDetails);
        request.setAttribute("poIDList", poIDList); // Gửi danh sách POID sang JSP
        request.getRequestDispatcher("deliveryOrder.jsp").forward(request, response);

    }


}