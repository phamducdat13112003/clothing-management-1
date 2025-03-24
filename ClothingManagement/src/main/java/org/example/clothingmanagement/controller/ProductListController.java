package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.service.BinService;
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
    private final BinService bs = new BinService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 5; // Số dòng trên mỗi trang

        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        HashMap<Product, String> list = ps.getAllProductsWithPagination(page, pageSize);

        //run through all the product
        for (Product product : list.keySet()) {

            // get all the product detail that belong to 1 product
            List<ProductDetail> listPDs = pds.getAllProductDetailByProductId(product.getId());
            int totalQuan = 0;

            // run through all the product detail
            for (ProductDetail productDetail : listPDs) {
                // calculate the sum of all quantity of all product detail
                totalQuan += productDetail.getQuantity();
            }
            product.setTotalQuantity(totalQuan);
        }

        int totalProduct = ps.getAllProducts().size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        req.setAttribute("list", list);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("product-list.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameSearch = req.getParameter("search") != null ? req.getParameter("search").trim() : "";
        String pageParam = req.getParameter("page");

        HashMap<Product, String> list = null;
        int page = 1;
        int pageSize = 5;
        int totalProducts = 0;
        if (!nameSearch.isEmpty()) {
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
        }
        list = ps.searchProductsWithPagination(nameSearch, page, pageSize);
        totalProducts = ps.searchProductsByNameSearch(nameSearch).size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        req.setAttribute("list", list);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("search", nameSearch);
        req.getRequestDispatcher("product-list.jsp").forward(req, resp);
    }

}




