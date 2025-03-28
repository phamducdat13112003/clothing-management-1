package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.DBContext;
import org.example.clothingmanagement.repository.EmployeeDAO;
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
    private EmployeeDAO employeeDAO;
    private static final int RECORDS_PER_PAGE = 2; // Số lượng record trên mỗi trang

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
        employeeDAO = new EmployeeDAO();
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
        // Retrieve all possible statuses
        List<String> allStatuses = transferOrderDAO.getAllUniqueStatuses();
        request.setAttribute("allStatuses", allStatuses);
        System.out.println("allStatuses: " + allStatuses);

        // Retrieve all possible creators/users
        List<String> allCreatedBy = null;
        try {
            allCreatedBy = employeeDAO.getAllEmployeeIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("allCreatedBy", allCreatedBy);
        System.out.println("allCreatedBy: " + allCreatedBy);

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
                                      int page, String search, String statusFilter, String dateFrom, String dateTo, String createdBy) throws ServletException, IOException {
        // Get context path for dynamic URL generation
        String contextPath = request.getContextPath();

        try (Connection conn = DBContext.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            System.out.println("Starting process for Transfer Order: " + toID);

            // Check if the transfer order exists
            TransferOrder transferOrder = transferOrderDAO.getTransferOrderByID(toID);
            if (transferOrder == null) {
                request.setAttribute("errorMessage", "Transfer Order not found.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Check if the transfer order is in a valid status for processing
            if (!"Pending".equals(transferOrder.getStatus())) {
                request.setAttribute("errorMessage", "Only Pending orders can be processed.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Get all details of this transfer order
            List<TODetail> details = transferOrderDAO.getTODetailsByTOID(toID);
            if (details == null || details.isEmpty()) {
                request.setAttribute("errorMessage", "No details found for this transfer order.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            boolean allProcessingSuccessful = true;
            List<String> insufficientProducts = new ArrayList<>();

            // Check inventory availability for all products
            for (TODetail detail : details) {
                String productDetailID = detail.getProductDetailID();
                int requiredQuantity = detail.getQuantity();
                String originalBinID = detail.getOriginBinID();

                // Check current available quantity in the origin bin
                int currentQuantity = transferOrderDAO.getCurrentBinQuantity(conn, originalBinID, productDetailID);

                if (currentQuantity < requiredQuantity) {
                    insufficientProducts.add(productDetailID);
                    allProcessingSuccessful = false;
                }
            }

            // If any product has insufficient quantity, abort processing
            if (!insufficientProducts.isEmpty()) {
                request.setAttribute("errorMessage", "Insufficient inventory for product(s): " +
                        insufficientProducts.stream().collect(Collectors.joining(", ")));
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Validate final bin capacity
            String finalBinID = details.get(0).getFinalBinID(); // Assuming all details have the same final bin
            double totalTransferWeight = 0.0;
            for (TODetail detail : details) {
                String productDetailID = detail.getProductDetailID();
                int quantity = detail.getQuantity();

                // Calculate total transfer weight
                double productWeight = transferOrderDAO.getProductWeight(productDetailID);
                totalTransferWeight += productWeight * quantity;
            }

            // Check bin capacity
            double binMaxCapacity = transferOrderDAO.getBinMaxCapacity(finalBinID);
            double currentBinWeight = transferOrderDAO.getCurrentBinWeight(finalBinID);
            double processingTransferWeight = transferOrderDAO.getProcessingTransferTotalWeight(finalBinID);
            double totalWeightAfterTransfer = currentBinWeight + totalTransferWeight + processingTransferWeight;

            System.out.println("Final Bin Max Capacity: " + binMaxCapacity);
            System.out.println("Current Bin Weight: " + currentBinWeight);
            System.out.println("Processing Transfer Weight: " + processingTransferWeight);
            System.out.println("Total Transfer Order Weight: " + totalTransferWeight);
            System.out.println("Total After Add TO: " + totalWeightAfterTransfer);

            // Check capacity bin đích
            if (totalWeightAfterTransfer > binMaxCapacity) {
                request.setAttribute("errorMessage", "Destination bin does not have sufficient capacity. " +
                        "Max Capacity: " + binMaxCapacity + " kg. " +
                        "Current Weight: " + currentBinWeight + " kg. " +
                        "Processing TO Weight: " + processingTransferWeight + " kg." +
                        "Transfer Weight: " + totalTransferWeight + " kg.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // trừ quantity ở bin chuyển
            for (TODetail detail : details) {
                String productDetailID = detail.getProductDetailID();
                int quantity = detail.getQuantity();
                String originalBinID = detail.getOriginBinID();

                // Remove quantity from source bin
                boolean isOriginBinUpdated = transferOrderDAO.updateBinQuantity(conn, originalBinID, productDetailID, -quantity);
                if (!isOriginBinUpdated) {
                    allProcessingSuccessful = false;
                    System.out.println("Failed to update temporary bin quantity");
                    break;
                }
                // Kiểm tra và xóa bin detail nếu số lượng còn lại là 0
                int remainingQuantity = transferOrderDAO.getCurrentBinQuantity(conn, originalBinID, productDetailID);
                if (remainingQuantity == 0) {
                    boolean isBinDetailDeleted = transferOrderDAO.deleteBinDetail(conn, originalBinID, productDetailID);
                    if (!isBinDetailDeleted) {
                        System.out.println("Failed to delete bin detail for bin " + originalBinID + " and product " + productDetailID);
                        // Có thể throw exception hoặc xử lý lỗi tùy theo logic của bạn
                    }
                }
            }

            if (allProcessingSuccessful) {
                // update status = processing
                try (PreparedStatement ps = conn.prepareStatement("UPDATE transferorder SET Status = ? WHERE TOID = ?")) {
                    ps.setString(1, "Processing");
                    ps.setString(2, toID);
                    int updatedRows = ps.executeUpdate();

                    if (updatedRows > 0) {
                        conn.commit();
                        System.out.println("Transaction committed - Transfer Order processed successfully");

                        request.getSession().setAttribute("successMessage", "Transfer Order processed successfully.");

                        response.sendRedirect(buildRedirectUrl(contextPath + "/TOList", page, search,
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
                System.out.println("Transaction rolled back - Failed to process transfer order");

                request.setAttribute("errorMessage", "Error processing transfer order.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during transfer order processing: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                    statusFilter, dateFrom, dateTo, createdBy);
        }
    }





    private void completeTransferOrder(HttpServletRequest request, HttpServletResponse response, String toID,
                                       int page, String search, String statusFilter, String dateFrom,
                                       String dateTo, String createdBy) throws ServletException, IOException {

        System.out.println("Starting completion process for Transfer Order: " + toID);
        // get address
        String contextPath = request.getContextPath();

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);
            System.out.println("Transaction started");

            // Check TO exist
            TransferOrder transferOrder = transferOrderDAO.getTransferOrderByID(toID);
            if (transferOrder == null) {
                request.setAttribute("errorMessage", "Transfer Order not found.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // chỉ status = processing mới đc
            if (!"Processing".equals(transferOrder.getStatus())) {
                request.setAttribute("errorMessage", "Only orders in Processing status can be completed.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Retrieve details directly from TODetail
            String transferDetailsQuery = "SELECT ProductDetailID, Quantity, FinalBinId " +
                    "FROM todetail " +
                    "WHERE TOID = ?";


            List<TODetail> details = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(transferDetailsQuery)) {
                ps.setString(1, toID);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        TODetail detail = new TODetail();
                        detail.setProductDetailID(rs.getString("ProductDetailID"));
                        detail.setQuantity(rs.getInt("Quantity"));
                        detail.setFinalBinID(rs.getString("FinalBinId"));
                        details.add(detail);
                    }
                }
            }

            if (details.isEmpty()) {
                request.setAttribute("errorMessage", "No details found for this transfer order.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Process each detail - update bin quantities
            boolean allUpdatesSuccessful = true;
            for (TODetail detail : details) {
                String productDetailID = detail.getProductDetailID();
                int quantity = detail.getQuantity();
                String finalBinID = detail.getFinalBinID();

                System.out.println("Processing transfer: " + quantity + " units of " + productDetailID +
                        " to final bin " + finalBinID);


                // Update final (destination) bin quantity
                boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(conn, finalBinID, productDetailID, quantity);
                if (!isFinalBinUpdated) {
                    allUpdatesSuccessful = false;
                    System.out.println("Failed to update final bin quantity");
                    break;
                }
            }

            if (allUpdatesSuccessful) {
                // Update transfer order status to Completed
                try (PreparedStatement ps = conn.prepareStatement("UPDATE transferorder SET Status = ? WHERE TOID = ?")) {
                    ps.setString(1, "Completed");
                    ps.setString(2, toID);
                    int updatedRows = ps.executeUpdate();

                    if (updatedRows > 0) {
                        conn.commit();
                        System.out.println("Transaction committed - Transfer Order completed successfully");

                        request.getSession().setAttribute("successMessage", "Transfer Order completed successfully.");

                        response.sendRedirect(buildRedirectUrl(contextPath + "/TOList", page, search,
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

    private void cancelTransferOrder(HttpServletRequest request, HttpServletResponse response, String toID,
                                     int page, String search, String statusFilter, String dateFrom, String dateTo, String createdBy)
            throws ServletException, IOException {
        // Get context path for dynamic URL generation
        String contextPath = request.getContextPath();

        try (Connection conn = DBContext.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            System.out.println("Starting cancel process for Transfer Order: " + toID);

            // Check if the transfer order exists
            TransferOrder transferOrder = transferOrderDAO.getTransferOrderByID(toID);
            if (transferOrder == null) {
                request.setAttribute("errorMessage", "Transfer Order not found.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Only allow cancellation for Pending status
            if (!"Pending".equals(transferOrder.getStatus())) {
                request.setAttribute("errorMessage", "Only Pending transfer orders can be canceled.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            // Retrieve transfer order details
            List<TODetail> details = transferOrderDAO.getTODetailsByTOID(toID);
            if (details == null || details.isEmpty()) {
                request.setAttribute("errorMessage", "No details found for this transfer order.");
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
                return;
            }

            boolean allCancellationSuccessful = true;

            // Update transfer order status to Canceled
            try (PreparedStatement ps = conn.prepareStatement("UPDATE transferorder SET Status = ? WHERE TOID = ?")) {
                ps.setString(1, "Canceled");
                ps.setString(2, toID);
                int updatedRows = ps.executeUpdate();

                if (updatedRows > 0) {
                    conn.commit();
                    System.out.println("Transaction committed - Transfer Order canceled successfully");

                    request.getSession().setAttribute("successMessage", "Transfer Order canceled successfully.");

                    response.sendRedirect(buildRedirectUrl(contextPath + "/TOList", page, search,
                            statusFilter, dateFrom, dateTo, createdBy));
                } else {
                    conn.rollback();
                    System.out.println("Transaction rolled back - Failed to update transfer order status");

                    request.setAttribute("errorMessage", "Error updating transfer order status.");
                    loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                            statusFilter, dateFrom, dateTo, createdBy);
                }
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Exception occurred during transfer order cancellation: " + e.getMessage());
                e.printStackTrace();

                request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
                loadTransferOrdersWithPaginationAndFilter(request, response, page, search,
                        statusFilter, dateFrom, dateTo, createdBy);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during transfer order cancellation: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
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
