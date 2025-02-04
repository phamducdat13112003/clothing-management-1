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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name="ProductList",urlPatterns = "/product-list")
public class ProductListController extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final ProductDetailService productDetailService = new ProductDetailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách sản phẩm từ ProductDAO
//        List<Product> products = new ArrayList<>();
        List<Product> products = productService.getAllProducts();



        for(Product product : products){
            List<ProductDetail> list = productDetailService.getProductDetailById(product.getId());
            product.setTotalQuantity(list.size());
            ProductDetail productDetail = list.get(0);

            // Biểu thức chính quy để lấy ID file
            String regex = "https://drive.google.com/file/d/([a-zA-Z0-9_-]+)/.*";

            // Tạo Pattern từ biểu thức chính quy
            Pattern pattern = Pattern.compile(regex);

            // Tạo matcher từ chuỗi URL
            Matcher matcher = pattern.matcher(productDetail.getImage());

            if (matcher.find()) {
                String img = matcher.group(1);
                product.setUrlImage(img);
            } else {
                product.setUrlImage("");
            }


        }




        // Đặt sản phẩm vào trong request để chuyển đến JSP
        req.setAttribute("products", products);


        // Chuyển tiếp đến JSP để hiển thị
        req.getRequestDispatcher("/product-list.jsp").forward(req, resp);
    }

}




