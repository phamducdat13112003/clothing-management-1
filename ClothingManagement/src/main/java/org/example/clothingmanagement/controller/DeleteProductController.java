package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.ProductDAO;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;

@WebServlet(name="DeleteProduct", urlPatterns = "/delete-product")
public class DeleteProductController extends HttpServlet {
    private final ProductService productService =  new ProductService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("productId"));
        boolean check = productService.deleteProduct(id);
        if (check) {
            resp.sendRedirect(req.getContextPath() + "/product-list?message=1");
        } else {
            resp.sendRedirect(req.getContextPath() + "/product-list?message=2");
        }
    }
}
