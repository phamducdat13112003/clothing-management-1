package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.DeliveryOrderDetail;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@WebServlet(name = "DODetailServlet", value = "/DODetailServlet")
public class DODetailServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String doID = request.getParameter("doId");

        if (doID != null && !doID.isEmpty()) {
            // Lấy danh sách DODetail theo DOID
            List<DeliveryOrderDetail> doDetails = DeliveryOrderDAO.getDODetailsByDOID(doID);

            // Lấy danh sách ProductDetailID từ DODetail
            List<String> productDetailIDs = doDetails.stream()
                    .map(DeliveryOrderDetail::getProductDetailID)
                    .toList();

            // Lấy thông tin chi tiết của sản phẩm
            List<Map<String, Object>> productDetails = DeliveryOrderDAO.getProductDetailsByProductDetailIDs(productDetailIDs);

            // Gửi dữ liệu qua JSP
            request.setAttribute("doDetails", doDetails);
            request.setAttribute("productDetails", productDetails);
        }

        request.getRequestDispatcher("doDetail.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}