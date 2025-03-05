package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;
import org.example.clothingmanagement.repository.DeliveryOrderDetailDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "AddDOServlet", value = "/AddDOServlet")
public class AddDOServlet extends HttpServlet {


protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    // Lấy dữ liệu từ request
    String doId = request.getParameter("doId");
    String quantityStr = request.getParameter("quantity");
    String plannedShippingDateStr = request.getParameter("plannedShippingDate");
    String poId = request.getParameter("poId");
    String accountID = request.getParameter("createBy");
    String createdBy = DeliveryOrderDAO.getEmployeeIDByAccountID(accountID);
    String productDetailID = request.getParameter("productDetailID");
    Date receiptDate = Date.valueOf("1970-01-01");
    ; // Ngày hiện tại làm ReceiptDate
    boolean status = true;
    int quantity = 0;

    // Kiểm tra dữ liệu nhập vào
    if (doId == null || doId.trim().isEmpty() || plannedShippingDateStr == null || plannedShippingDateStr.trim().isEmpty()) {
        out.println("<script>alert('Vui lòng điền đầy đủ thông tin!'); window.history.back();</script>");
        return;
    }

    // Chuyển đổi ngày giao hàng
    Date plannedShippingDate;
    try {
        plannedShippingDate = Date.valueOf(plannedShippingDateStr);
    } catch (IllegalArgumentException e) {
        out.println("<script>alert('Ngày giao hàng không hợp lệ!'); window.history.back();</script>");
        return;
    }

    // Kiểm tra số lượng
    if (quantityStr != null && !quantityStr.isEmpty()) {
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            out.println("<script>alert('Số lượng không hợp lệ!'); window.history.back();</script>");
            return;
        }
    }

    // Kiểm tra nếu DOID đã tồn tại
    DeliveryOrderDAO dao = new DeliveryOrderDAO();
    if (dao.isDOIDExist(doId)) {
        out.println("<script>alert('DOID đã tồn tại! Không thể tạo mới.'); window.history.back();</script>");
        return;
    }

    // Kiểm tra ngày plannedShippingDate và receiptDate
    if (!dao.isValidDate(poId, plannedShippingDate)) {
        out.println("<script>alert('Ngày giao hàng hoặc ngày nhận hàng không hợp lệ!'); window.history.back();</script>");
        return;
    }

    // Thêm DO vào database
    boolean success = dao.addDO(doId, plannedShippingDate, receiptDate, poId, createdBy, null, status);
    if (!success) {
        out.println("<script>alert('Lỗi khi thêm DO! Vui lòng thử lại.'); window.history.back();</script>");
        return;
    }

    // Tạo DODetailID
    String doDetailID;
    try {
        doDetailID = DeliveryOrderDetailDAO.generateDODetailID();
    } catch (Exception e) {
        out.println("<script>alert('Lỗi khi tạo DODetailID, vui lòng thử lại!'); window.history.back();</script>");
        return;
    }

    // Thêm DODetail vào database
    boolean doDetailSuccess = DeliveryOrderDetailDAO.addDODetail(doDetailID, productDetailID, quantity, doId);
    if (!doDetailSuccess) {
        out.println("<script>alert('Lỗi khi thêm DODetail! Vui lòng thử lại.'); window.history.back();</script>");
        return;
    }

    // Nếu thành công, chuyển hướng
    response.sendRedirect("Category.jsp");
}
}
