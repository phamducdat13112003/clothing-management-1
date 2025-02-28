package org.example.clothingmanagement.controller;import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.service.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "PurchaseOrderDetailServlet", value = "/purchaseorderdetail")
public class PurchaseOrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        PurchaseOrderDetailService purchaseOrderDetailService = new PurchaseOrderDetailService();
        ProductService productService = new ProductService();
        ProductDetailService productDetailService = new ProductDetailService();
        SupplierService supplierService = new SupplierService();
        EmployeeService employeeService = new EmployeeService();
        String purchaseOrderId = request.getParameter("poID");
        System.out.println(purchaseOrderId);
        PurchaseOrder purchaseOrder;
        try {
            purchaseOrder = purchaseOrderService.getPObyPoID(purchaseOrderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Xem chi tiết PO qua poID tham chiếu sang bảng podetail lấy được productdetailid(1 hàm) + lấy ra được PO detail --> từ productdetailid lấy được productid(+ 1 hàm) + lấy ra được productdetail(1 hàm)--> từ producid lấy thông tin Product(+ 1 hàm)
        PurchaseOrderDetail purchaseOrderDetail;
        try {
            purchaseOrderDetail = purchaseOrderDetailService.getPODetailbypoID(purchaseOrderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String productdetailid = purchaseOrderDetail.getProductDetailID();
        System.out.println(productdetailid);
        String productid = null;
        try {
            productid = productDetailService.getProductIDByProductDetailID(productdetailid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(productid);
        ProductDetail productDetail;
        try {
            productDetail = productDetailService.getProductDetailByProductDetailID(productdetailid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(productDetail);
        Product product = null;
        try {
            product = productService.getProductByProductID(productid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(product);
        String supplierid = purchaseOrder.getSupplierID();
        System.out.println(supplierid);
        Supplier supplier;
        try {
            supplier = supplierService.getSupplierBySupplierID(supplierid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(supplier);
        String employeeid = purchaseOrder.getCreatedBy();
        Employee employee;
        try {
            employee = employeeService.getEmployeeByID(employeeid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(employee);
        request.setAttribute("purchaseOrder", purchaseOrder);
        request.setAttribute("purchaseOrderId", purchaseOrderId);
        request.setAttribute("productdetailid", productdetailid);
        request.setAttribute("productid", productid);
        request.setAttribute("product", product);
        request.setAttribute("productDetail", productDetail);
        request.setAttribute("purchaseOrderDetail", purchaseOrderDetail);
        request.setAttribute("supplierid", supplierid);
        request.setAttribute("supplier", supplier);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("purchaseorderdetail.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}