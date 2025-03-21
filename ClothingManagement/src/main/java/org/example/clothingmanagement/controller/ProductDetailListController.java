package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.ProductDetailService;

import java.io.IOException;
import java.util.List;

@WebServlet(name="ProductDetailList",urlPatterns = "/product-detail-list")
public class ProductDetailListController extends HttpServlet {
    ProductDetailService pds = new ProductDetailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<ProductDetail> pdList = pds.findAllWithPagination(page,pageSize);
        int totalProduct = pds.getAllProductDetails().size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);


        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("pdList", pdList);
        req.getRequestDispatcher("product-detail-list.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameSearch = req.getParameter("search") != null ? req.getParameter("search").trim() : "";
        String pageParam = req.getParameter("page");

        int page = 1;
        int pageSize = 5;
        if(!nameSearch.isEmpty()){
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
        }

        List<ProductDetail> pdList = pds.searchAllWithPagination(nameSearch,page,pageSize);
        int totalProducts = pds.searchAllWithoutPagination(nameSearch).size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("pdList", pdList);
        req.getRequestDispatcher("product-detail-list.jsp").forward(req, resp);
    }
}
