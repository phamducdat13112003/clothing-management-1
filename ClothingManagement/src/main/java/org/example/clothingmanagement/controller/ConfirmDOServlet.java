package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "ConfirmDOServlet", value = "/ConfirmDOServlet")
public class ConfirmDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String doID = request.getParameter("doId");
        String receiptDate = request.getParameter("ReceiptDate");
        String accountID = request.getParameter("createBy");
        String createBy = DeliveryOrderDAO.getEmployeeIDByAccountID(accountID);
        String productDetailID = request.getParameter("productDetailID");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int quantityCheck = Integer.parseInt(request.getParameter("quantityCheck"));

        // Kiểm tra dữ liệu đầu vào
        if (doID == null || receiptDate == null || createBy == null || productDetailID == null) {
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ.");
            request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);
            return;
        }

        try {
            if (quantity == quantityCheck) {
                // Nếu số lượng nhập vào bằng số lượng kiểm tra, cập nhật DO và đổi trạng thái thành false
                DeliveryOrderDAO.updateDO(doID, receiptDate, createBy, false);
            } else if (quantity < quantityCheck) {
                // Nếu số lượng nhỏ hơn, chỉ cập nhật số lượng
                DeliveryOrderDAO.updateDODetailQuantity(productDetailID, quantity);
                DeliveryOrderDAO.updateDO(doID, receiptDate, createBy, true);
            } else {
                request.setAttribute("error", "Số lượng nhập vào không hợp lệ.");
                request.getRequestDispatcher("Category.jsp").forward(request, response);
                return;
            }

            response.sendRedirect("account");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra trong quá trình cập nhật.");
            request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);
        }
    }
}