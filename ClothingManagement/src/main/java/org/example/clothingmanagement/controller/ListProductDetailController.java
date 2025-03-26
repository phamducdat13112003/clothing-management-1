package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.service.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet(name="ProductDetail", urlPatterns = "/list-product-detail")
public class ListProductDetailController extends HttpServlet {
    private final ProductService ps = new ProductService();
    private final ProductDetailService pds = new ProductDetailService();
    private final CategoryService cs = new CategoryService();
    private final SupplierService ss = new SupplierService();
    private final EmployeeService es = new EmployeeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get product that of product detail
        String id = req.getParameter("id");
        Product product = new Product();
        if(ps.getProductById(id).isPresent()){
            product = ps.getProductById(id).get();
        }
        else{
            product = new Product();
        }
        // pagination
        int page = 1;
        int pageSize = 5;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }



        List<ProductDetail> productDetails = pds.getProductDetailByProductIdWithPagination(id,page,pageSize);
        int totalProduct = pds.getAllProductDetailByProductId(product.getId()).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);

        req.setAttribute("list", productDetails);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("product", product);
        req.getRequestDispatcher("list-product-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        String nameSearch = req.getParameter("search") != null ? req.getParameter("search").trim() : "";
        String pageParam = req.getParameter("page");

        Product product = new Product();
        if(ps.getProductById(productId).isPresent()){
            product = ps.getProductById(productId).get();
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

        int page = 1;
        int pageSize = 5;
        if(!nameSearch.isEmpty()){
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
        }

        List<ProductDetail> list = pds.SearchProductDetailByProductIdWithPagination(productId,nameSearch,page,pageSize);
        int totalProducts = pds.SearchProductDetailByProductIdAndNameSearch(productId,nameSearch).size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        req.setAttribute("list", list);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("search", nameSearch);
        req.setAttribute("product", product);
        req.getRequestDispatcher("list-product-detail.jsp").forward(req, resp);
    }
}
