package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.ProductDetailService;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet(name="ProductDetail", urlPatterns = "/list-product-detail")
public class ListProductDetailController extends HttpServlet {
    private final ProductService ps = new ProductService();
    private final ProductDetailService pds = new ProductDetailService();

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
        List<ProductDetail> productDetails = pds.getAllProductDetailByProductId(id);
        req.setAttribute("product", product);
        req.setAttribute("productDetails", productDetails);
        req.getRequestDispatcher("list-product-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
