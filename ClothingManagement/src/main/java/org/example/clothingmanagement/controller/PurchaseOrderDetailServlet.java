package org.example.clothingmanagement.controller;import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.service.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        //lấy PO
        PurchaseOrder purchaseOrder;
        try {
            purchaseOrder = purchaseOrderService.getPObyPoID(purchaseOrderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(purchaseOrder);

        //từ PO lấy list PoDetail
        List<PurchaseOrderDetail> purchaseOrderDetail;
        try {
            purchaseOrderDetail = purchaseOrderDetailService.getListPODetailbypoID(purchaseOrderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Lấy productdetailID
        List<String> productDetailIds = new ArrayList<>();
        for (PurchaseOrderDetail detail : purchaseOrderDetail) {
            productDetailIds.add(detail.getProductDetailID());
        }
        System.out.println(productDetailIds);


        //Lấy ProductID bằng ProductDetailID
        List<String> productIds = new ArrayList<>();
        for (String productDetailId : productDetailIds) {
            try {
                String productId = productDetailService.getProductIDByProductDetailID(productDetailId);
                productIds.add(productId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(productIds);

        //Lây ra list productDetails và Product
        List<ProductDetail> productDetails = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        for (String productDetailId : productDetailIds) {
            try {
                productDetails.add(productDetailService.getProductDetailByProductDetailID(productDetailId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (String productId : productIds) {
            try {
                products.add(productService.getProductByProductID(productId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(productDetails);
        System.out.println(products);

        //Lấy ra nhà cung câps (Supplier)
        List<Supplier> suppliers = new ArrayList<>();

        for (Product product : products) {
            try {
                Supplier supplier = supplierService.getSupplierBySupplierID(product.getSupplierId());
                suppliers.add(supplier);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(suppliers);

        //Lấy employee
        String employeeid = purchaseOrder.getCreatedBy();
        Employee employee;
        try {
            employee = employeeService.getEmployeeByID(employeeid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(employee);

        //Product, purchaseOrderDetail, productDetail cho chung vào 1 list rồi đẩy lên
        List<PurchaseOrderDetailDTO> purchaseOrderDetailDTOs = new ArrayList<>();
        PurchaseOrderDetailDTO dto = new PurchaseOrderDetailDTO(productDetails.)
            try {
                // Tạo DTO và thêm vào danh sách
                PurchaseOrderDetailDTO dto = new PurchaseOrderDetailDTO(
                        productDetail.getImage(),
                        product.getName(),
                        productDetail.getWeight(),
                        productDetail.getColor(),
                        productDetail.getSize(),
                        detail.getQuantity(),
                        detail.getPrice(),
                        detail.getTotalPrice()
                );
                purchaseOrderDetailDTOs.add(dto);
            } catch (Exception e) {
                throw new RuntimeException(e);

        }

        //hết gửi lên JSP
        request.setAttribute("purchaseOrder", purchaseOrder);
        request.setAttribute("purchaseOrderId", purchaseOrderId);
        request.setAttribute("productdetailid", productDetailIds);
        request.setAttribute("productid", productIds);
        request.setAttribute("product", products);
        request.setAttribute("productDetail", productDetails);
        request.setAttribute("purchaseOrderDetail", purchaseOrderDetail);
        request.setAttribute("supplier", suppliers);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("purchaseorderdetail.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}