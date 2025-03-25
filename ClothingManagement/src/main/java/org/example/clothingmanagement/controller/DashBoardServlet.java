package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.DashBoardService;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashBoardServlet", value = "/dashboard")
public class DashBoardServlet extends HttpServlet {

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
        DashBoardService dashBoardService = new DashBoardService();
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String productId= request.getParameter("productId");
        String pageParam = request.getParameter("page");
        int page = 1;
        int pageSize = 5;
        int totalBinId =0;
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        List<BinDetail> list = null;
        try {
            list = dashBoardService.getBinCapacityPercentage(page, pageSize);
            totalBinId = dashBoardService.getTotalPages();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int totalPages = (int) Math.ceil((double) totalBinId / pageSize);
        int totalOrders = dashBoardService.getTotalOrders();
        int totalSuppliers = dashBoardService.getTotalSuppliers();
        int totalEmployees = dashBoardService.getTotalEmployees();
        double totalOrderValue = dashBoardService.getTotalOrderValue();
        Map<String, Double> totalPOByMonth;
        if (startDate != null && endDate != null) {
            totalPOByMonth = dashBoardService.getTotalPOByMonth(startDate, endDate);
        } else {
            totalPOByMonth = dashBoardService.getTotalPOByMonth(); // Nếu không có filter, lấy tất cả
        }
        BinDetail binDetail = dashBoardService.getTotalQuantityByProductDetailId(productId);
        List<ProductDetail> productList = dashBoardService.getProductStorageList(productId);
//  Định dạng số tiền PO để tránh lỗi hiển thị 1.18E8
        DecimalFormat df = new DecimalFormat("#,###.##");
        String formattedTotalOrderValue = df.format(totalOrderValue);
        String totalPOByMonthJson = new Gson().toJson(totalPOByMonth);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("binDetail", binDetail);
        request.setAttribute("productList", productList);
        request.setAttribute("list", list);
        request.setAttribute("totalSuppliers", totalSuppliers);
        request.setAttribute("totalEmployees", totalEmployees);
        request.setAttribute("totalOrderValue", formattedTotalOrderValue);
        request.setAttribute("totalPOByMonthJson", totalPOByMonthJson);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./Dashboard.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
