package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.service.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name="UpdateProduct", urlPatterns = "/update-product")
public class UpdateProductController extends HttpServlet {
    private final ProductService ps = new ProductService();
    private final SupplierService ss = new SupplierService();
    private final ProductDetailService pds = new ProductDetailService();
    private final EmployeeService es = new EmployeeService();
    private final CategoryService cs = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Product product = new Product();
        if(ps.getProductById(id).isPresent()){
            product = ps.getProductById(id).get();
        }
        else{
            product = new Product();
        }
        List<Category> categories = cs.selectAll();
        List<Supplier> suppliers = ss.getAllSuppliers();
        List<Employee> employees = es.getAllEmployee();
        for(Category c : categories){
            if(product.getCategoryId().equals(c.getCategoryID())){
                product.setCategory(c);
            }
        }
        for(Supplier s : suppliers){
            if(product.getSupplierId().equalsIgnoreCase(s.getSupplierId())){
                product.setSupplier(s);
            }
        }
        for(Employee e : employees){
            if(product.getCreatedBy().equalsIgnoreCase(e.getEmployeeID())){
                product.setEmployee(e);
            }
        }

        req.setAttribute("product", product);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("update-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productID = req.getParameter("productId");
        String productName = capitalizeWords(req.getParameter("name").trim());
        Double productPrice = Double.parseDouble(req.getParameter("price"));
        String material = req.getParameter("material");
        String gender = req.getParameter("gender");
        String season = req.getParameter("season");
        int categoryID = Integer.parseInt(req.getParameter("categoryID"));
        int minQuantity = Integer.parseInt(req.getParameter("minQuantity"));
        String madeIn = req.getParameter("madeIn");
        String description = req.getParameter("description");
        String supplierName = req.getParameter("supplier");
        String supplierId="";
        List<Supplier> suppliers = ss.getAllSuppliers();
        for(Supplier s : suppliers) {
            if(s.getSupplierName().equalsIgnoreCase(supplierName)) {
                supplierId = s.getSupplierId();
            }
        }
        Product p = new Product(categoryID,description,gender,madeIn,material,minQuantity,productName,productPrice,season,productID,supplierId);
        boolean checkDup = ps.checkDup(p);
        if(!checkDup) {
            boolean check = ps.updateProduct(p);
            if (check) {
                HttpSession session = req.getSession();
                session.setAttribute("alertMessage", "Update Product Success");
                session.setAttribute("alertType", "success");
                resp.sendRedirect("list-product-detail?id=" + productID);
            } else {
                req.setAttribute("alertMessage", "Update Product Failed");
                req.setAttribute("alertType", "error");
                req.getRequestDispatcher("list-product-detail.jsp").forward(req, resp);
            }
        }
        else{
            HttpSession session = req.getSession();
            session.setAttribute("alertMessage", "Duplicated.");
            session.setAttribute("alertType", "error");
            resp.sendRedirect("list-product-detail?id=" + productID);
        }



    }
    public static String capitalizeWords(String str) {
        String[] words = str.split("\\s+");  // Tách chuỗi theo khoảng trắng
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            // Viết hoa chữ cái đầu và ghép lại thành chuỗi
            capitalizedString.append(word.substring(0, 1).toUpperCase())  // Viết hoa chữ cái đầu
                    .append(word.substring(1).toLowerCase())  // Giữ nguyên phần còn lại của từ
                    .append(" ");  // Thêm khoảng trắng
        }

        return capitalizedString.toString().trim();  // Loại bỏ khoảng trắng dư thừa ở cuối
    }
}
