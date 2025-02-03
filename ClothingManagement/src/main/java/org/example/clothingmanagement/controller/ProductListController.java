package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.service.ProductDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name="ProductList",urlPatterns = "/product-list")
public class ProductListController extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách sản phẩm từ ProductDAO
        List<Product> products = productDAO.getAllProducts();

        // Đặt sản phẩm vào trong request để chuyển đến JSP
        req.setAttribute("products", products);

        // Chuyển tiếp đến JSP để hiển thị
        req.getRequestDispatcher("/product-list.jsp").forward(req, resp);
    }



}
