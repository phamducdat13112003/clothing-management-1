package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
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
        //lấy PO
        PurchaseOrder purchaseOrder;
        try {
            purchaseOrder = purchaseOrderService.getPObyPoID(purchaseOrderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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

        //Lấy ra nhà cung câps (Supplier)
        String supplierid = null;
        try {
            supplierid = purchaseOrderService.getSupplierIDByPoID(purchaseOrderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Supplier supplier;
        try {
            supplier = supplierService.getSupplierBySupplierID(supplierid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Lấy employee
        String employeeid = purchaseOrder.getCreatedBy();
        Employee employee;
        try {
            employee = employeeService.getEmployeeByID(employeeid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Product, purchaseOrderDetail, productDetail cho chung vào 1 list rồi đẩy lên
        // Lấy danh sách DTO
        List<PurchaseOrderDetailDTO> purchaseOrderDetailDTOs = new ArrayList<>();

        for (PurchaseOrderDetail detail : purchaseOrderDetail) {
            try {
                String productDetailId = detail.getProductDetailID();
                ProductDetail productDetail = productDetailService.getProductDetailByProductDetailID(productDetailId);
                String productId = productDetailService.getProductIDByProductDetailID(productDetailId);
                Product product = productService.getProductByProductID(productId);
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
                e.printStackTrace();
            }
        }
//        System.out.println("====== Liste des PurchaseOrderDetailDTO ======");
//        for (PurchaseOrderDetailDTO dto : purchaseOrderDetailDTOs) {
//            System.out.println("Image: " + dto.getImage());
//            System.out.println("Product Name: " + dto.getProductName());
//            System.out.println("Weight: " + dto.getWeight());
//            System.out.println("Color: " + dto.getColor());
//            System.out.println("Size: " + dto.getSize());
//            System.out.println("Quantity: " + dto.getQuantity());
//            System.out.println("Price: " + dto.getPrice());
//            System.out.println("Total Price: " + dto.getTotalPrice());
//            System.out.println("---------------------------------------------");
//        }

        //hết gửi lên JSP
        request.setAttribute("purchaseOrder", purchaseOrder);
//        request.setAttribute("purchaseOrderId", purchaseOrderId);
//        request.setAttribute("productdetailid", productDetailIds);
//        request.setAttribute("productid", productIds);
//        request.setAttribute("product", products);
//        request.setAttribute("productDetail", productDetails);
//        request.setAttribute("purchaseOrderDetail", purchaseOrderDetail);
        request.setAttribute("purchaseOrderDetailDTOs", purchaseOrderDetailDTOs);
        request.setAttribute("supplier", supplier);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("purchaseorderdetail.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}