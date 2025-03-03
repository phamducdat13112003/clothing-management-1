package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.PurchaseOrderDetail;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@WebServlet(name = "GetPoServlet", value = "/GetPoServlet")
public class GetPoServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    // Lấy danh sách PO ID từ request
    List<String> poIDList = request.getParameterValues("poID") != null
            ? new ArrayList<>(Arrays.asList(request.getParameterValues("poID")))
            : new ArrayList<>();

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

    // Gửi dữ liệu đến trang JSP
    request.setAttribute("poDetails", poDetails);
    request.setAttribute("productDetails", productDetails);
    request.setAttribute("poIDList", poIDList); // Gửi danh sách POID sang JSP
    request.getRequestDispatcher("deliveryOrder.jsp").forward(request, response);
}


}