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
import java.util.HashMap;

@WebServlet(name="UpdateProductDetail",urlPatterns = "/update-product-detail")
public class UpdateProductDetailController extends HttpServlet {
    ProductDetailService pds = new ProductDetailService();
    ProductService ps = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ProductDetail pd = new ProductDetail();
        Product p = new Product();
        if(pds.getOptionalProductDetailByProductDetailId(id).isPresent()){
            pd = pds.getOptionalProductDetailByProductDetailId(id).get();
            if(ps.getProductById(pd.getProductId()).isPresent()){
                p = ps.getProductById(pd.getProductId()).get();
            }
            else{
                p = null;
            }
            req.setAttribute("p", p);
            req.setAttribute("pd", pd);
            req.getRequestDispatcher("update-product-detail.jsp").forward(req, resp);
        }
        else{
            pd=null;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
