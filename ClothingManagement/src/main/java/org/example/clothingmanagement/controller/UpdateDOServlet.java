package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.DeliveryOrder;
import org.example.clothingmanagement.entity.DeliveryOrderDetail;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;
import org.example.clothingmanagement.repository.DeliveryOrderDetailDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@WebServlet(name = "UpdateDOServlet", value = "/UpdateDOServlet")
public class UpdateDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String doID = request.getParameter("doId");
        String poID = request.getParameter("poId");
        if (doID != null && !doID.isEmpty()) {
            // Lấy danh sách DODetail theo DOID
            List<DeliveryOrderDetail> doDetails = DeliveryOrderDAO.getDODetailsByDOID(doID);

            // Lấy danh sách ProductDetailID từ DODetail
            List<String> productDetailIDs = doDetails.stream()
                    .map(DeliveryOrderDetail::getProductDetailID)
                    .toList();

            // Lấy thông tin chi tiết của sản phẩm
            List<Map<String, Object>> productDetails = DeliveryOrderDAO.getProductDetailsByProductDetailIDs(productDetailIDs);
            DeliveryOrder dor = DeliveryOrderDAO.getDOByID(doID);
            Map<String, Integer> remainingQuantities = DeliveryOrderDAO.getRemainingQuantities(poID);

            // Gửi dữ liệu qua JSP
            request.setAttribute("remainingQuantities", remainingQuantities);
            request.setAttribute("dor", dor);
            request.setAttribute("doDetails", doDetails);
            request.setAttribute("productDetails", productDetails);

        }

        request.getRequestDispatcher("updateDO.jsp").forward(request, response);

    }

//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // Lấy dữ liệu từ request
//        String doID = request.getParameter("doID");
//        String plannedShippingDate = request.getParameter("plannedShippingDate");
//        String createdBy = request.getParameter("createdBy");
//
//        // Lấy danh sách quantity của DODetail từ request
//        Map<String, Integer> quantityUpdates = new HashMap<>();
//        request.getParameterMap().forEach((key, values) -> {
//            if (key.startsWith("quantity_")) {
//                String doDetailID = key.substring(9); // Cắt bỏ "quantity_" để lấy DODetailID
//                int quantity = Integer.parseInt(values[0]);
//                quantityUpdates.put(doDetailID, quantity);
//            }
//        });
//
//        // Kết nối database
//
//        DeliveryOrderDAO doDAO = new DeliveryOrderDAO();
//        DeliveryOrderDetailDAO dodetailDAO = new DeliveryOrderDetailDAO();
//
//        // Thực hiện cập nhật
//        boolean updateDO = doDAO.updateDOForPS(doID, plannedShippingDate, createdBy);
//        boolean updateDODetails = dodetailDAO.updateDODetailForPS(quantityUpdates);
//
//
//        // Điều hướng dựa trên kết quả cập nhật
//        if (updateDO && updateDODetails) {
//            response.sendRedirect("DeliveryOrderServlet");
//        } else {
//            response.sendRedirect("DeliveryOrderServlet");
//        }
//
//
//    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String doID = request.getParameter("doID");
        String plannedShippingDate = request.getParameter("plannedShippingDate");
        String accountID = request.getParameter("createdBy");
        String createdBy = DeliveryOrderDAO.getEmployeeIDByAccountID(accountID);
        DeliveryOrderDAO dao = new DeliveryOrderDAO();
        boolean update = dao.updateDOForPS(doID, plannedShippingDate, createdBy);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if(update) {
            response.getWriter().write("<script>alert('Update DO thành công!'); window.location.href = 'DeliveryOrderServlet';</script>");
        }else{
            response.getWriter().write("<script>alert('Không thể update DO vì đã hoàn thành!');history.back(); </script>");

        }

    }

}