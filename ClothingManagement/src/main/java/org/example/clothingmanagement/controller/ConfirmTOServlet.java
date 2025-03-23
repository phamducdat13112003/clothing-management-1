package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.service.TOService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ConfirmTOServlet", value = "/confirmTO")
public class ConfirmTOServlet extends HttpServlet {

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
        TOService toService = new TOService();
        int page = 1;
        int pageSize = 5; // Số dòng trên mỗi trang

        // Lấy tham số trang từ request
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        String toID = request.getParameter("toID");
        boolean isUpdateStatus = false;
        if(toID != null) {
            isUpdateStatus=  toService.updateTransferOrderStatus(toID);
            if(isUpdateStatus) {
                request.setAttribute("messageSuccess", "Update TO Status Successful");
            }else{
                request.setAttribute("message", "Update TO Status Failed");
            }
        }
        int totalTO = toService.countTransferOrdersPending();
        int totalPages = (int) Math.ceil((double) totalTO / pageSize);
        List<TransferOrder> list = toService.getTransferOrdersPending(page, pageSize);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("transferOrders", list);
        request.getRequestDispatcher("./confirmTO.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TOService toService = new TOService();
        String search = request.getParameter("search").trim();
        int page = 1;
        int pageSize = 5; // Số dòng trên mỗi trang
        int totalTO =0;
        // Lấy tham số trang từ request
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<TransferOrder> list = null;

        if(search != null) {
            list = toService.searchTransferOrdersByTOID(search, page, pageSize);
            totalTO = toService.countTransferOrdersByTOID(search);
        }else{
            list = toService.getTransferOrdersPending(page, pageSize);
            totalTO = toService.countTransferOrdersPending();
        }
        int totalPages = (int) Math.ceil((double) totalTO / pageSize);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("transferOrders", list);
        request.getRequestDispatcher("./confirmTO.jsp").forward(request, response);
    }
}
