package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
@WebServlet(name = "UpdatePurchaseOrderServlet", value = "/updatepurchaseorder")
public class UpdatePurchaseOrderServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService = new SupplierService();
    private final EmployeeService employeeService = new EmployeeService();
    private final PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
    private final ProductDetailService productDetailService = new ProductDetailService();
    private final PurchaseOrderDetailService purchaseOrderDetailService = new PurchaseOrderDetailService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String poid = request.getParameter("poid");
        String[] productdetailid = request.getParameterValues("productdetailid[]");
        String[] podetailid = request.getParameterValues("podetailid[]");
        String[] priceinput = request.getParameterValues("priceinput[]");
        String[] quantity = request.getParameterValues("quantity[]");
        String[] totalpricepod = request.getParameterValues("totalpricepod[]");
        String[] supplieridbyproduct = request.getParameterValues("supplierid[]");
        String supplierid = request.getParameter("supplierid");

        // Kiểm tra nếu không có sản phẩm được chọn
        if (productdetailid == null) {
            forwardWithError(request, response, poid, "You must choose a product");
            return;
        }
        // Kiểm tra nếu tất cả sản phẩm có cùng supplier ID không
        HashSet<String> supplierSet = new HashSet<>(Arrays.asList(supplieridbyproduct));
        if (!supplierSet.contains(supplierid) || supplierSet.size() > 1) {
            forwardWithError(request, response, poid, "The supplier in the product does not match the selected supplier.");
            return;
        }

        try {
            // Debug: In ra giá trị của các tham số
            System.out.println("poid: " + poid);
            System.out.println("priceinput: " + Arrays.toString(priceinput));
            System.out.println("quantity: " + Arrays.toString(quantity));
            System.out.println("totalpricepod: " + Arrays.toString(totalpricepod));
            System.out.println("totalAmountPO: " + request.getParameter("totalAmountPO"));

            // Xóa bản cũ và cập nhật PO
            purchaseOrderDetailService.deletePoDetailByPoID(poid);
            float totalAmountPO = Float.parseFloat(request.getParameter("totalAmountPO"));
            purchaseOrderService.updatePO(poid, supplierid, totalAmountPO);
            purchaseOrderDetailService.addPurchaseOrderDetail(poid, productdetailid, podetailid, priceinput, quantity, totalpricepod);

            // Lấy danh sách PO để hiển thị sau khi cập nhật thành công
            List<PurchaseOrder> purchaseOrderList = purchaseOrderService.getAllPurchaseOrder();
            request.getSession().setAttribute("updateposuccessfully", "Update " + poid + " Success");
            request.setAttribute("purchaseOrderList", purchaseOrderList);
            request.getRequestDispatcher("viewpurchaseorder.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }

    }
    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String poid, String errorMessage) throws ServletException, IOException {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.getPObyPoID(poid);
            String supplierId = purchaseOrderService.getSupplierIDByPoID(poid);
            Supplier supplier = supplierService.getSupplierById(supplierId);
            List<Map<String, Object>> podetailList = productService.getListPodetailByPoID(poid);
            List<Map<String, Object>> productList = productService.getAllProductProductDetail();
            List<Supplier> suppliers = supplierService.getAllSuppliers();
            HttpSession session = request.getSession();
            String accountid = session.getAttribute("account_id").toString();
            String employeeid = employeeService.getEmployeeIdByAccountId(accountid);
            Employee employee = employeeService.getEmployeeByEmployeeId(employeeid);

            request.setAttribute("purchaseordercreatedate", purchaseOrder.getCreatedDate());
            request.setAttribute("supplier", supplier);
            request.setAttribute("podetailList", podetailList);
            request.setAttribute("poid", poid);
            request.setAttribute("productList", productList);
            request.setAttribute("suppliers", suppliers);
            request.setAttribute("employee", employee);
            session.setAttribute("errorMessage1", errorMessage);
            request.getRequestDispatcher("updatepurchaseorder.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}