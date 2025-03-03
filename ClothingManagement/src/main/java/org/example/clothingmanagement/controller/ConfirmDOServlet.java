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
        String receiptDate = request.getParameter("ReceiptDate");
        String accountID = request.getParameter("createBy");
        String createdBy = DeliveryOrderDAO.getEmployeeIDByAccountID(accountID);
        String[] dodetailIds = request.getParameterValues("DODetailID[]");
        String[] quantityArray = request.getParameterValues("quantity[]");

        // Kiểm tra dữ liệu đầu vào
        if (receiptDate == null || createdBy == null || dodetailIds == null || quantityArray == null || dodetailIds.length != quantityArray.length) {
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ.");
            request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);
            return;
        }

        try {
            // Cập nhật ReceiptDate và đặt Status = false cho đơn hàng
            System.out.println("createdBy: " + createdBy);
            DeliveryOrderDAO.updateDO(receiptDate,createdBy);

            // Duyệt danh sách DODetailID và cập nhật số lượng tương ứng
            for (int i = 0; i < dodetailIds.length; i++) {
                String dodetailId = dodetailIds[i];
                int quantity = Integer.parseInt(quantityArray[i]);

                if (quantity < 1) {
                    request.setAttribute("error", "Số lượng không hợp lệ.");
                    request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);
                    return;
                }

                DeliveryOrderDAO.updateDODetailQuantity(dodetailId, quantity);
            }

            // Chuyển hướng về danh sách đơn hàng sau khi cập nhật thành công
            
            response.sendRedirect("deliveryOrderWS.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra trong quá trình cập nhật.");
            request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);
        }
    }
}