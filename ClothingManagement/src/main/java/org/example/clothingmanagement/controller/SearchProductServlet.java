package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SearchProductServlet", value = "/searchproduct")
public class SearchProductServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddAcc</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddAcc at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService productService = new ProductService();
        String nameSearch = request.getParameter("search") != null ? request.getParameter("search").trim() : "";
        String pageParam = request.getParameter("page");

        List<Product> list= null;
        int page = 1;
        int pageSize = 5;
        int totalProducts = 0;
        if(nameSearch.isEmpty()){
            try {
                list= productService.getAllProductsWithPagination(page, pageSize);
                totalProducts = productService.countAllProducts();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            try {
                list = productService.searchProduct(nameSearch, page, pageSize);
                totalProducts = productService.getTotalProductCount(nameSearch);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        request.setAttribute("list", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", nameSearch);
        request.getRequestDispatcher("./ViewBinInventory.jsp").forward(request, response);
    }
}
