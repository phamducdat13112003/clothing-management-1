package org.example.clothingmanagement.controller;import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@WebServlet(name = "AddDOServlet", value = "/AddDOServlet")
public class AddDOServlet extends HttpServlet {
   
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Supplier> suppliers = DeliveryOrderDAO.getAllSuppliers(); // Lấy danh sách từ DB

        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("addDO.jsp").forward(request, response);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action"); // Kiểm tra người dùng chọn Filter hay Select All
        String supplierID = request.getParameter("supplier");
        String poID = request.getParameter("poID");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        List<Supplier> suppliers = DeliveryOrderDAO.getAllSuppliers();
        List<PurchaseOrder> poList = DeliveryOrderDAO.filterPO(supplierID, startDate, endDate, poID);

// Lọc các PO chưa nhận đủ số lượng
        Iterator<PurchaseOrder> iterator = poList.iterator();
        while (iterator.hasNext()) {
            PurchaseOrder po = iterator.next();
            if (DeliveryOrderDAO.isPOFullyReceived(po.getPoID())) {
                iterator.remove(); // Loại bỏ PO đã nhận đủ
            }
        }

        Map<String, String> supplierNames = new HashMap<>();
        Map<String, String> employeeNames = new HashMap<>();

        for (PurchaseOrder po : poList) {
            String supplierID_PO = po.getSupplierID();
            String createdBy = po.getCreatedBy();

            if (createdBy != null && !employeeNames.containsKey(createdBy)) {
                employeeNames.put(createdBy, DeliveryOrderDAO.getEmployeeNameByEmployeeID(createdBy));
            }
            if (supplierID_PO != null && !supplierNames.containsKey(supplierID_PO)) {
                supplierNames.put(supplierID_PO, DeliveryOrderDAO.getSupplierNameByID(supplierID_PO));
            }
        }


        request.setAttribute("poList", poList);
        request.setAttribute("supplierNames", supplierNames);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("employeeNames", employeeNames);
        request.getRequestDispatcher("addDO.jsp").forward(request, response);

    }

    
}