package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.entity.PurchaseOrderDetail;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@WebServlet(name = "GetDODetailData", value = "/GetDODetailData")
public class GetDODetailData extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Lấy PO ID từ request
        String action = request.getParameter("action"); // Kiểm tra người dùng chọn Filter hay Select All
        String supplierID = request.getParameter("supplier");
        String poID = request.getParameter("poID");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        List<Supplier> suppliers = DeliveryOrderDAO.getAllSuppliers();
        List<PurchaseOrder> poList;

        if ("selectAll".equals(action)) {
            // Nếu chọn "Select All", gọi filterPO mà không truyền điều kiện
            poList = DeliveryOrderDAO.filterPO(null, null, null, null);
        } else {
            // Nếu chọn "Filter", gọi filterPO với các giá trị từ form
            poList = DeliveryOrderDAO.filterPO(supplierID, startDate, endDate, poID);
        }

        // **Chuyển đổi dữ liệu để lấy SupplierName
        Map<String, String> supplierNames = new HashMap<>();

        for (PurchaseOrder po : poList) {
            String supplierID_PO = po.getSupplierID();

            // Kiểm tra null để tránh lỗi
            if (supplierID_PO != null && !supplierNames.containsKey(supplierID_PO)) {
                supplierNames.put(supplierID_PO, DeliveryOrderDAO.getSupplierNameByID(supplierID_PO));
            }
        }

        List<String> poIDs = poList.stream()
                .map(PurchaseOrder::getPoID)
                .collect(Collectors.toList());
// Lấy danh sách PODETAIL từ database theo poID
        List<PurchaseOrderDetail> poDetails = DeliveryOrderDAO.getPODetailsByPOIDs(poIDs);
        poDetails = poDetails.stream()
                .filter(poDetail -> !DeliveryOrderDAO.isDOQuantityValid(poDetail.getProductDetailID()))
                .collect(Collectors.toList());


// Trích xuất danh sách các fields cần thiết từ poDetails thông qua productdetailID
        List<String> productDetailIDs = poDetails.stream()
                .map(PurchaseOrderDetail::getProductDetailID)
                .distinct()
                .collect(Collectors.toList());
        List<Map<String, Object>> productDetails = DeliveryOrderDAO.getProductDetailsByProductDetailIDs(productDetailIDs);

// Gửi dữ liệu đến trang JSP
        request.setAttribute("poDetails", poDetails);
        request.setAttribute("productDetails", productDetails);
        request.setAttribute("poID", poID); // Gửi POID thay vì danh sách
        request.setAttribute("supplierID", supplierID);
        request.setAttribute("poList", poList);
        request.setAttribute("supplierNames", supplierNames);
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("po-list.jsp").forward(request, response);
    }


}