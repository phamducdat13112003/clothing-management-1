package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.EmployeeService;
import org.example.clothingmanagement.service.ProductDetailService;
import org.example.clothingmanagement.service.ProductService;
import org.example.clothingmanagement.service.SupplierService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AddInfomationPoServlet", value = "/addinfomationpo")
public class AddInfomationPoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String accountid = session.getAttribute("account_id").toString();
        ProductService productService = new ProductService();
        SupplierService supplierService = new SupplierService();
        EmployeeService employeeService = new EmployeeService();
        ProductDetailService productDetailService = new ProductDetailService();
        List<Supplier> suppliers = supplierService.getAllSuppliers();
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


        request.setAttribute("productList", productList);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("addpurchaseorder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}