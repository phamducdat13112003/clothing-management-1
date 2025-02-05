package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;

@WebServlet(name="ProductDetail", urlPatterns = "/product-detail")
public class ProductDetailController extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = Integer.valueOf(req.getParameter("id"));
        boolean check = productService.getProductById(productId).isPresent();

        if(check) {
                Product product = productService.getProductById(productId).get();
                req.setAttribute("product", product);
                req.getRequestDispatcher("/product-detail.jsp").forward(req, resp);
        }
        else{
            Product product = null;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
