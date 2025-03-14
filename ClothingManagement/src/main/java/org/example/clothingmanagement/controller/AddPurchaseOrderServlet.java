package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.lang.model.element.NestingKind;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AddPurchaseOrderServlet", value = "/addpurchaseorder")
public class AddPurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Bảng PoDetail
        String[] productdetailid = request.getParameterValues("productdetailid[]");
        String[] podetailid = request.getParameterValues("podetailid[]"); // podetailid = poid +"pod"+ sô thứ tự
        String[] priceinput = request.getParameterValues("priceinput[]");//giá của 1 sản phẩm
        String[] quantity = request.getParameterValues("quantity[]");
        String[] totalpricepod = request.getParameterValues("totalpricepod[]");// tổng tiền podetail = quantity * priceinput
        Float totalAmountPO = Float.valueOf(request.getParameter("totalAmountPO"));
        //Bảng Po
        String dateStr = request.getParameter("createddate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        Date date = Date.valueOf(localDate);
        String poid;
        String[] supplieridbyproduct = request.getParameterValues("supplierid[]");
        String supplierid = request.getParameter("supplierid");
        String createbyid = request.getParameter("employeeid"); //tự gen ra
        String status = "Pending";
        if (productdetailid == null || productdetailid.length == 0) {
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
            request.getSession().setAttribute("errorMessage", "You must choose a product");
            request.getRequestDispatcher("addpurchaseorder.jsp").forward(request, response);

        }
        System.out.println("Danh sách supplieridbyproduct: " + Arrays.toString(supplieridbyproduct));
        System.out.println("Supplier ID đã chọn: " + supplierid);
        if (!Arrays.stream(supplieridbyproduct).allMatch(s -> s.equals(supplierid))) {
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
            List<Map<String, Object>> productList = null;
            try {
                productList = productService.getAllProductProductDetail();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            request.setAttribute("productList", productList);
            request.setAttribute("suppliers", suppliers);
            request.setAttribute("employee", employee);
            request.getSession().setAttribute("errorMessageSupplier", "The supplier in the product does not match the selected supplier.");
            request.getRequestDispatcher("addpurchaseorder.jsp").forward(request, response);
        }
        else {
            PurchaseOrderService purchaseorderservice = new PurchaseOrderService();
            PurchaseOrderDetailService purchaseorderdetailservice = new PurchaseOrderDetailService();
            try {
                poid = purchaseorderservice.generateUniquePoId(String.valueOf(date));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            PurchaseOrder purchaseOrder = new PurchaseOrder(poid, date, supplierid, createbyid, status, totalAmountPO);
            try {
                purchaseorderservice.addPurchaseOrder(purchaseOrder);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                purchaseorderdetailservice.addPurchaseOrderDetail(poid, productdetailid, podetailid, priceinput, quantity, totalpricepod);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<PurchaseOrder> purchaseOrderList = null;
            try {
                purchaseOrderList = purchaseorderservice.getAllPurchaseOrder();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Trạng thaí khi lấy về mặc định là chưa xác nhận
//        1. ch xac nhan // xóa, sửa được: chưa update vào sản phẩm Pending (Đạt)
//        2. xac nhan roi nhung ch co don DO tham chieu den // xóa, sửa được: chưa update vào sản phẩm Confirmed (Đạt)
//        3. co don DO tham chieu den // sửa được: chưa update vào sản phẩm Processing (Cường)
//        4. Hoàn Thành: update vào sản phẩm Done --> kho (Cường)
//        5. Hủy: update vào sản phẩm Cancel (Đạt)
            System.out.println("Bảng PO:");
            System.out.println("Po ID: " + poid);
            System.out.println("Date: " + date);
            System.out.println("Supplier ID: " + supplierid);
            System.out.println("Employee ID: " + createbyid);
            System.out.println("Status :" + status);
            System.out.println("Total Amount: " + totalAmountPO);
            System.out.println("Bảng PoDetail:");
            System.out.println("PO Detail ID: " + Arrays.toString(podetailid));
            System.out.println("Po ID:" + poid);
            System.out.println("Product Detail ID: " + Arrays.toString(productdetailid));
            System.out.println("Quantity: " + Arrays.toString(quantity));
            System.out.println("Price Input: " + Arrays.toString(priceinput));
            System.out.println("Total Price POD: " + Arrays.toString(totalpricepod));// tổng tiền podetail = quantity * priceof1product

            request.getSession().setAttribute("addposuccessfully", "Add PO  Success");
            request.setAttribute("purchaseOrderList", purchaseOrderList);
            request.getRequestDispatcher("viewpurchaseorder.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}