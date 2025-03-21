package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.BinDetailService;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.ProductDetailService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DeleteProductInBin", value = "/deleteproductinbin")
public class DeleteProductInBin extends HttpServlet {

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
        BinDetailService binDetailService = new BinDetailService();
        ProductDetailService productDetailService = new ProductDetailService();
        String binId = request.getParameter("binId");
        String productDetailId= request.getParameter("productDetailId");
        int page=1;
        int pageSize = 5;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<BinDetail> list = binDetailService.getBinDetailsWithPagination(binId,page,pageSize);
        List<ProductDetail> productDetails = productDetailService.getAllProductDetails();
        for(BinDetail bd : list) {
            for(ProductDetail pd : productDetails) {
                if(bd.getProductDetailId().equalsIgnoreCase(pd.getId())){
                    bd.setProductDetail(pd);
                }
            }
        }
        int totalProduct = binDetailService.getBinDetailsByBinId(binId).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        if (binId == null || productDetailId == null) {
            request.setAttribute("message", "Can't get binId");
            request.setAttribute("list", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("./view-bin-detail.jsp").forward(request, response);
            return;
        }
        if(!binDetailService.canDeleteProduct(binId, productDetailId)) {
            request.setAttribute("message", "In Bin is still product");
            request.setAttribute("list", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("./view-bin-detail.jsp").forward(request, response);
            return;
        }
        boolean isDeleted = binDetailService.deleteProductFromBin(binId, productDetailId);
        if (isDeleted) {
            request.setAttribute("messageSuccess", "Bin deleted successfully");
        }else{
            request.setAttribute("message", "Bin can't be deleted");
        }
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("list", list);
        request.getRequestDispatcher("./view-bin-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
