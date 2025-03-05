package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.Map;


@WebServlet(name = "SelectDataForDO", value = "/SelectDataForDO")
public class SelectDataForDO extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            // Nhận dữ liệu từ request
            String poID = request.getParameter("poID");
            String productDetailID = request.getParameter("productDetailID");
            String supplierID = request.getParameter("supplierID");

            int quantity = DeliveryOrderDAO.getRemainingQuantity(productDetailID);

            Map<String, Object> productDetail = DeliveryOrderDAO.getProductDetailByProductDetailID(productDetailID);

            // Chuyển dữ liệu đến JSP nếu cần
            request.setAttribute("poID", poID);
            request.setAttribute("productDetail", productDetail);
            request.setAttribute("quantity", quantity);
            request.setAttribute("supplierID", supplierID);

            // Forward đến JSP hiển thị dữ liệu (thay đổi 'result.jsp' thành JSP phù hợp)
            request.getRequestDispatcher("addDO.jsp").forward(request, response);
        }

}