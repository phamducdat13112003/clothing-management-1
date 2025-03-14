package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.SneakyThrows;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UpdateInfomationPoServlet", value = "/updateinfomationpo")
public class UpdateInfomationPoServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String accountid = session.getAttribute("account_id").toString();
        ProductService productService = new ProductService();
        SupplierService supplierService = new SupplierService();
        EmployeeService employeeService = new EmployeeService();
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        ProductDetailService productDetailService = new ProductDetailService();
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        Supplier supplier = new Supplier();
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        String poid = request.getParameter("poid");
        List<Map<String, Object>> podetailList = productService.getListPodetailByPoID(poid);
        purchaseOrder = purchaseOrderService.getPObyPoID(poid);
        String supplieid;
        try {
            supplieid = purchaseOrderService.getSupplierIDByPoID(poid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String employeeid = null;
        try {
            employeeid = employeeService.getEmployeeIdByAccountId(accountid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Employee employee;
        try {
            employee = employeeService.getEmployeeByEmployeeId(employeeid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        List<Product> products = productService.getAllProducts();
        List<Map<String, Object>> productList = null;
        try {
            productList = productService.getAllProductProductDetail();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        supplier = supplierService.getSupplierById(supplieid);
        request.setAttribute("purchaseordercreatedate", purchaseOrder.getCreatedDate());
        request.setAttribute("supplier", supplier);
        request.setAttribute("podetailList", podetailList);
        request.setAttribute("poid", poid);
        request.setAttribute("productList", productList);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("updatepurchaseorder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}