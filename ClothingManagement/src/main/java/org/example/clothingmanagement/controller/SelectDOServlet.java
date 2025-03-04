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
import java.util.List;
import java.util.Map;


@WebServlet(name = "SelectDOServlet", value = "/SelectDOServlet")
public class SelectDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
// Nhận dữ liệu từ request
        String doID = request.getParameter("doID"); // Đảm bảo khớp với input trong JSP
        String poID = request.getParameter("poId");
        String productDetailID = DeliveryOrderDAO.getProductDetailIDByDOID(doID);
        String createBy = request.getParameter("createBy");

        // Kiểm tra doID hợp lệ
        if (doID == null || doID.trim().isEmpty()) {
            request.setAttribute("error", "DOID không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Lấy danh sách chi tiết đơn hàng dựa trên DOID
        Map<String, Object> doDetails = DeliveryOrderDAO.getDODetailsByDOID(doID);

        // Lấy số lượng từ bảng DODetail
        int quantity = DeliveryOrderDAO.getDOQuantity(doID, productDetailID);

        // Lấy thông tin sản phẩm từ ProductDetailID
        Map<String, Object> productDetail = DeliveryOrderDAO.getProductDetailByProductDetailID(productDetailID);

        // Đưa dữ liệu vào request để gửi sang JSP
        request.setAttribute("doDetails", doDetails);
        request.setAttribute("poID", poID);
        request.setAttribute("doID", doID);
        request.setAttribute("productDetail", productDetail);
        request.setAttribute("quantity", quantity);
        request.setAttribute("createBy", createBy);

        // Forward đến JSP phù hợp
        request.getRequestDispatcher("confirmDO.jsp").forward(request, response);
    }


}