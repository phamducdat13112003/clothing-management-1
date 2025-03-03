package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.CategoryService;
import org.example.clothingmanagement.service.ProductDetailService;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@WebServlet(name = "ProductList", urlPatterns = "/product-list")
public class ProductListController extends HttpServlet {
    private final ProductService ps = new ProductService();
    private final ProductDetailService pds = new ProductDetailService();
    private final CategoryService cs = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<Product,String> list = ps.getAllProducts();

        //run through all the product
        for(Product product : list.keySet()) {

            // get all the product detail that belong to 1 product
            List<ProductDetail> listPDs = pds.getAllProductDetailByProductId(product.getId());
            int totalQuan =0;

            // run through all the product detail
            for(ProductDetail productDetail : listPDs) {
                // calculate the sum of all quantity of all product detail
                totalQuan += productDetail.getQuantity();
            }
            product.setTotalQuantity(totalQuan);
        }

        req.setAttribute("products", list);

        req.getRequestDispatcher("product-list.jsp").forward(req, resp);
    }

}




