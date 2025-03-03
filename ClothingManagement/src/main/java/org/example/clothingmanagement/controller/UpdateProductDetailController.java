package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.ProductDetailService;

import java.io.IOException;

@WebServlet(name="UpdateProductDetail",urlPatterns = "/update-product-detail")
public class UpdateProductDetailController extends HttpServlet {
    ProductDetailService pds = new ProductDetailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ProductDetail pd = new ProductDetail();
        if(pds.getOptionalProductDetailByProductDetailId(id).isPresent()){
            pd = pds.getOptionalProductDetailByProductDetailId(id).get();
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
