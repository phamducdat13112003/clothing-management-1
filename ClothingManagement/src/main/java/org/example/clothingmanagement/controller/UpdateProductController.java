package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.ProductService;
import org.example.clothingmanagement.service.SupplierService;

import java.io.IOException;
import java.util.List;

@WebServlet(name="UpdateProduct", urlPatterns = "/update-product")
public class UpdateProductController extends HttpServlet {
    private final ProductService ps = new ProductService();
    private final SupplierService ss = new SupplierService();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productID = req.getParameter("productId");
        String productName = req.getParameter("name");
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
        boolean checkDup = false;
        List<Product> list = ps.getAllProducts();
        for(Product p : list) {
            if (productName.equalsIgnoreCase(p.getName()) && season.equalsIgnoreCase(p.getSeasons()) && categoryID == p.getCategoryId() && supplierId.equalsIgnoreCase(p.getSupplierId())) {
                checkDup = true;
                break;
            }
        }

        if(!checkDup) {
            Product p = new Product(categoryID,description,gender,madeIn,material,minQuantity,productName,productPrice,season,productID,supplierId);
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
}
