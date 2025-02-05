package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.CategoryDAO;
import org.example.clothingmanagement.service.ProductService;
import org.example.clothingmanagement.service.SupplierService;

import java.io.IOException;
import java.util.List;

@WebServlet(name="AddProduct", urlPatterns = "/add-product")
public class AddProductController extends HttpServlet {
    private final SupplierService supplierService = new SupplierService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = CategoryDAO.selectAll();
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        req.setAttribute("categories", categories);
        req.setAttribute("suppliers", suppliers);
        req.getRequestDispatcher("/add-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productName = req.getParameter("productName");
        double price = Double.parseDouble(req.getParameter("price"));
        int categoryID = Integer.parseInt(req.getParameter("categoryID"));
        String material = req.getParameter("material");
        String gender = req.getParameter("gender");
        String seasons = req.getParameter("seasons");
        int minQuantity = Integer.parseInt(req.getParameter("minQuantity"));
        String description = req.getParameter("description");
        int supplierID = Integer.parseInt(req.getParameter("supplierID"));
        String madeIn = req.getParameter("madeIn");

        Product product = new Product(productName,price,seasons,supplierID,material,madeIn,gender,description,categoryID,minQuantity,1,1);
        boolean check = productService.addProduct(product);
        if (check) {
            resp.sendRedirect(req.getContextPath()+ "/add-product?abc=1");
        }
        else{
            resp.sendRedirect(req.getContextPath()+ "/add-product?abc=2");
        }

    }
}
