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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

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
                boolean isCanceled = transferOrderDAO.cancelTransferOrder(toID);
                if (isCanceled) {
                    // Redirect to the list page after successful cancellation with filter params
                    response.sendRedirect(buildRedirectUrl("/ClothingManagement_war/TOList", page, search,
                            statusFilter, dateFrom, dateTo, createdBy));
                } else {
                    // If cancellation failed, set an error message
                    request.setAttribute("errorMessage", "Error canceling the transfer order.");
                    loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                            statusFilter, dateFrom, dateTo, createdBy);
                }
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
        } else {
            // Default: Show all transfer orders with pagination, search and filter
            loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                    statusFilter, dateFrom, dateTo, createdBy);
        }
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

    private void completeTransferOrder(HttpServletRequest request, HttpServletResponse response, String toID,
                                       int page, String search, String statusFilter, String dateFrom,
                                       String dateTo, String createdBy) throws ServletException, IOException {

        System.out.println("Starting completion process for Transfer Order: " + toID);

        try (Connection conn = DBContext.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            System.out.println("Transaction started");

            // Check if the transfer order exists and has valid status
            TransferOrder transferOrder = transferOrderDAO.getTransferOrderByID(toID);
            if (transferOrder == null) {
                request.setAttribute("errorMessage", "Transfer Order not found.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Allow completion from either Pending or Processing status

            if (!"Pending".equals(transferOrder.getStatus()) && !"Processing".equals(transferOrder.getStatus())) {
                request.setAttribute("errorMessage", "Only orders in Pending or Processing status can be completed.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);

            // if (!"Processing".equals(transferOrder.getStatus())) {
            //     request.setAttribute("errorMessage", "Only orders in Processing status can be completed.");
            //     request.getRequestDispatcher("to-list.jsp").forward(request, response);

            //     return;
            // }

            System.out.println("Transfer Order status is: " + transferOrder.getStatus());

            // Get all details of this transfer order
            List<TODetail> details = transferOrderDAO.getTODetailsByTOID(toID);
            if (details == null || details.isEmpty()) {
                request.setAttribute("errorMessage", "No details found for this transfer order.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            System.out.println("Found " + details.size() + " details for this transfer order");

            // Process each detail - update bin quantities
            boolean allUpdatesSuccessful = true;
            for (TODetail detail : details) {
                String productDetailID = detail.getProductDetailID();
                int quantity = detail.getQuantity();
                String originBinID = detail.getOriginBinID();
                String finalBinID = detail.getFinalBinID();

                System.out.println("Processing transfer: " + quantity + " units of " + productDetailID +
                        " from bin " + originBinID + " to bin " + finalBinID);

                // REMOVED: Check for available quantity in origin bin

                // Update bin quantities - increment in final bin
                boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(conn, finalBinID, productDetailID, quantity);

                if (!isFinalBinUpdated) {
                    allUpdatesSuccessful = false;
                    System.out.println("Final updated: " + isFinalBinUpdated);
                    break;
                }

                // Update destination bin status to 1 (active)
                boolean isFinalBinStatusUpdated = transferOrderDAO.updateBinStatus(conn, originBinID, 1);

                if (!isFinalBinStatusUpdated) {
                    allUpdatesSuccessful = false;
                    System.out.println("Final bin status update failed");
                    break;
                }
            }

            if (allUpdatesSuccessful) {
                // Update transfer order status to Completed
                try (PreparedStatement ps = conn.prepareStatement("UPDATE transferorder SET Status = ? WHERE TOID = ?")) {
                    ps.setString(1, "Done");
                    ps.setString(2, toID);
                    int updatedRows = ps.executeUpdate();

                    if (updatedRows > 0) {
                        conn.commit();
                        System.out.println("Transaction committed - Transfer Order completed successfully");

                        request.getSession().setAttribute("successMessage", "Transfer Order completed successfully.");
                        response.sendRedirect(buildRedirectUrl("/ClothingManagement_war/TOList", page, search,
                                statusFilter, dateFrom, dateTo, createdBy));
                    } else {
                        conn.rollback();
                        System.out.println("Transaction rolled back - Failed to update transfer order status");

                        request.setAttribute("errorMessage", "Error updating transfer order status.");
                        loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                                statusFilter, dateFrom, dateTo, createdBy);
                    }
                }
            } else {
                conn.rollback();
                System.out.println("Transaction rolled back - Failed to update bin quantities");

                request.setAttribute("errorMessage", "Error updating bin quantities.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during transfer completion: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                    statusFilter, dateFrom, dateTo, createdBy);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý form tìm kiếm và filter
        String search = request.getParameter("search");
        String statusFilter = request.getParameter("statusFilter");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String createdBy = request.getParameter("createdByFilter");

        StringBuilder redirectUrl = new StringBuilder("/ClothingManagement_war/TOList?page=1");

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
}
