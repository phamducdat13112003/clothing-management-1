package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.DBContext;
import org.example.clothingmanagement.repository.TransferOrderDAO;
import org.example.clothingmanagement.repository.VirtualBinDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "TransferOrderListServlet", value = "/TOList")
public class TransferOrderListServlet extends HttpServlet {

    private TransferOrderDAO transferOrderDAO;
    private static final int RECORDS_PER_PAGE = 5; // Số lượng record trên mỗi trang

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String toID = request.getParameter("toID");


        // Get context path for dynamic URL generation
        String contextPath = request.getContextPath();

        // Xử lý tham số phân trang, tìm kiếm và filter
        int page = 1;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        String search = request.getParameter("search");
        if (search == null) {
            search = "";
        }

        // Các tham số filter
        String statusFilter = request.getParameter("statusFilter");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String createdBy = request.getParameter("createdByFilter");

        if ("cancel".equals(action)) {
            if (toID != null && !toID.trim().isEmpty()) {
                cancelTransferOrder(request, response, toID, page, search, statusFilter, dateFrom, dateTo, createdBy);
            } else {
                request.setAttribute("errorMessage", "Invalid Transfer Order ID.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            }
        } else if ("done".equals(action)) {
            if (toID != null && !toID.trim().isEmpty()) {
                completeTransferOrder(request, response, toID, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            } else {
                request.setAttribute("errorMessage", "Invalid Transfer Order ID.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            }
        } else if ("process".equals(action)) {
            if (toID != null && !toID.trim().isEmpty()) {
                processTransferOrder(request, response, toID, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            } else {
                request.setAttribute("errorMessage", "Invalid Transfer Order ID.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            }
        } else {
            // Default: Show all transfer orders with pagination, search and filter
            loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                    statusFilter, dateFrom, dateTo, createdBy);
        }
    }

    private void processTransferOrder(HttpServletRequest request, HttpServletResponse response, String toID,
                                      int page, String search, String statusFilter, String dateFrom, String dateTo, String createdBy)
            throws ServletException, IOException {
        String contextPath = request.getContextPath();

        boolean processResult = transferOrderDAO.processTransferOrder(toID);

        if (processResult) {
            request.getSession().setAttribute("successMessage", "Transfer Order processed successfully.");
            response.sendRedirect(buildRedirectUrl(contextPath + "/TOList", page, search,
                    statusFilter, dateFrom, dateTo, createdBy));
        } else {
            request.setAttribute("errorMessage", "Error processing transfer order.");
            loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                    statusFilter, dateFrom, dateTo, createdBy);
        }
    }



    private void completeTransferOrder(HttpServletRequest request, HttpServletResponse response, String toID,
                                       int page, String search, String statusFilter, String dateFrom,
                                       String dateTo, String createdBy) throws ServletException, IOException {
        String contextPath = request.getContextPath();

        boolean completionResult = transferOrderDAO.completeTransferOrder(toID);

        if (completionResult) {
            request.getSession().setAttribute("successMessage", "Transfer Order completed successfully.");
            response.sendRedirect(buildRedirectUrl(contextPath + "/TOList", page, search,
                    statusFilter, dateFrom, dateTo, createdBy));
        } else {
            request.setAttribute("errorMessage", "Error completing transfer order.");
            loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                    statusFilter, dateFrom, dateTo, createdBy);
        }
    }

    private void cancelTransferOrder(HttpServletRequest request, HttpServletResponse response, String toID,
                                     int page, String search, String statusFilter, String dateFrom,
                                     String dateTo, String createdBy) throws ServletException, IOException {
        String contextPath = request.getContextPath();

        boolean cancellationResult = transferOrderDAO.cancelTransferOrder(toID);

        if (cancellationResult) {
            request.getSession().setAttribute("successMessage", "Transfer Order canceled successfully.");
            response.sendRedirect(buildRedirectUrl(contextPath + "/TOList", page, search,
                    statusFilter, dateFrom, dateTo, createdBy));
        } else {
            request.setAttribute("errorMessage", "Error canceling transfer order.");
            loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                    statusFilter, dateFrom, dateTo, createdBy);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get context path for dynamic URL generation
        String contextPath = request.getContextPath();

        // Xử lý form tìm kiếm và filter
        String search = request.getParameter("search");
        String statusFilter = request.getParameter("statusFilter");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String createdBy = request.getParameter("createdByFilter");

        StringBuilder redirectUrl = new StringBuilder(contextPath + "/TOList?page=1");

        if (search != null && !search.isEmpty()) {
            redirectUrl.append("&search=").append(search);
        }

        if (statusFilter != null && !statusFilter.isEmpty()) {
            redirectUrl.append("&statusFilter=").append(statusFilter);
        }

        if (dateFrom != null && !dateFrom.isEmpty()) {
            redirectUrl.append("&dateFrom=").append(dateFrom);
        }

        if (dateTo != null && !dateTo.isEmpty()) {
            redirectUrl.append("&dateTo=").append(dateTo);
        }

        if (createdBy != null && !createdBy.isEmpty()) {
            redirectUrl.append("&createdByFilter=").append(createdBy);
        }

        response.sendRedirect(redirectUrl.toString());
    }

    private void loadTransferOrdersWithPaginationAndFilter(HttpServletRequest request, HttpServletResponse response,
                                                           int page, String search, String statusFilter, String dateFrom,
                                                           String dateTo, String createdBy)
            throws ServletException, IOException {



        // Tính vị trí bắt đầu cho LIMIT trong SQL
        int offset = (page - 1) * RECORDS_PER_PAGE;

        // Lấy danh sách trạng thái để hiển thị trong dropdown filter
        List<String> allStatuses = transferOrderDAO.getAllDistinctStatuses();
        List<String> allCreatedBy = transferOrderDAO.getAllDistinctCreatedBy();

        // Lấy danh sách đơn hàng với phân trang, tìm kiếm và filter
        List<TransferOrder> transferOrders = transferOrderDAO.getTransferOrdersWithPaginationAndFilter(
                offset, RECORDS_PER_PAGE, search, statusFilter, dateFrom, dateTo, createdBy);

        // Lấy tổng số đơn hàng để tính số trang
        int totalRecords = transferOrderDAO.getTotalTransferOrdersWithFilter(search, statusFilter, dateFrom, dateTo, createdBy);
        int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

        // Đặt các thuộc tính cho JSP
        request.setAttribute("transferOrders", transferOrders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("dateFrom", dateFrom);
        request.setAttribute("dateTo", dateTo);
        request.setAttribute("createdByFilter", createdBy);
        request.setAttribute("allStatuses", allStatuses);
        request.setAttribute("allCreatedBy", allCreatedBy);

        // Forward đến trang JSP
        request.getRequestDispatcher("to-list.jsp").forward(request, response);
    }

    private String buildRedirectUrl(String baseUrl, int page, String search,
                                    String statusFilter, String dateFrom,
                                    String dateTo, String createdBy) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?page=").append(page);

        if (search != null && !search.isEmpty()) {
            url.append("&search=").append(search);
        }

        if (statusFilter != null && !statusFilter.isEmpty()) {
            url.append("&statusFilter=").append(statusFilter);
        }

        if (dateFrom != null && !dateFrom.isEmpty()) {
            url.append("&dateFrom=").append(dateFrom);
        }

        if (dateTo != null && !dateTo.isEmpty()) {
            url.append("&dateTo=").append(dateTo);
        }

        if (createdBy != null && !createdBy.isEmpty()) {
            url.append("&createdByFilter=").append(createdBy);
        }

        return url.toString();
    }
}
