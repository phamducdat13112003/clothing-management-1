package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.service.BinService;
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
        BinService binService = new BinService();
        String nameSearch = request.getParameter("search") != null ? request.getParameter("search").trim() : "";
        String binID = request.getParameter("binID") != null ? request.getParameter("binID").trim() : "";
        String pageParam = request.getParameter("page");
        double maxCapacity =0.0;

        List<BinDetail> list= null;
        int page = 1;
        int pageSize = 5;
        int totalBins = 0;
        if(!nameSearch.isEmpty() || !binID.isEmpty()){
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            try {
                maxCapacity = binService.getMaxCapacityByBinID(binID);
                list = binService.searchBinDetail(nameSearch, binID, page, pageSize);
                totalBins = binService.countBinDetail(nameSearch, binID);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        int totalPages = (int) Math.ceil((double) totalBins / pageSize);
        List<Bin> listBin = binService.getAllBins();
        request.setAttribute("list", list);
        request.setAttribute("selectedBin", binID);
        request.setAttribute("binList", listBin);
        request.setAttribute("maxCapacity", maxCapacity);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", nameSearch);
        request.getRequestDispatcher("./ViewBinInventory.jsp").forward(request, response);
    }
}
