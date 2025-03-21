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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "TransferOrderUpdateServlet", value = "/TOUpdate")
public class TransferOrderUpdateServlet extends HttpServlet {


    private TransferOrderDAO transferOrderDAO;

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toID = request.getParameter("toID");

        // Fetch the Transfer Order based on the provided toID
        TransferOrder transferOrder = transferOrderDAO.getTransferOrderById(toID);

        if (transferOrder == null) {
            // Handle case where TransferOrder is not found
            request.setAttribute("errorMessage", "Transfer Order not found.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Fetch all TODetails for the given Transfer Order ID
        List<TODetail> toDetails = transferOrderDAO.getTODetailsByTransferOrderId(toID);
        for (TODetail detail : toDetails) {
            System.out.println("Product: " + detail.getProductDetailID() + ", Quantity: " + detail.getQuantity());
        }

        // Pass the Transfer Order and its details to the JSP
        request.setAttribute("transferOrder", transferOrder);
        request.setAttribute("toDetails", toDetails);
        System.out.println("transfer order:" + transferOrder);
        System.out.println("to details:" + toDetails);

        // Get all employee IDs (optional: for a list of employees)
        List<String> employeeIds = transferOrderDAO.getAllEmployeeIds();
        request.setAttribute("employeeIds", employeeIds);
        request.getRequestDispatcher("to-update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String toID = request.getParameter("toID");
            String createdBy = request.getParameter("createdBy");
            String status = request.getParameter("status");
            String originBinID = request.getParameter("originBinID");
            String finalBinID = request.getParameter("finalBinID");

            // Retrieve and validate createdDate
            LocalDate createdDate;
            try {
                createdDate = LocalDate.parse(request.getParameter("createdDate"));
            } catch (DateTimeParseException e) {
                request.setAttribute("errorMessage", "Invalid date format.");
                request.getRequestDispatcher("/test2.jsp").forward(request, response);
                return;
            }

            // Create TransferOrder object with updated values
            TransferOrder transferOrder = new TransferOrder(toID, createdDate, createdBy, status);

            // Use the connection-based approach to update the transfer order and its details
            updateTransferOrderWithDetails(request, response, transferOrder, originBinID, finalBinID);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
        }
    }

    private void updateTransferOrderWithDetails(HttpServletRequest request, HttpServletResponse response,
                                                TransferOrder transferOrder, String originBinID, String finalBinID)
            throws ServletException, IOException {

        System.out.println("Starting update process for Transfer Order: " + transferOrder.getToID());

        try (Connection conn = DBContext.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            System.out.println("Transaction started");

            // Update the Transfer Order in the database
            boolean result = transferOrderDAO.updateTransferOrder(transferOrder);

            if (result) {
                // Retrieve and process the list of product details and quantities to update
                String[] productDetailIDs = request.getParameterValues("productDetailID[]");
                String[] quantities = request.getParameterValues("quantity[]");

                // Calculate weight differences for bin capacity check
                double totalWeightDifference = 0.0;

                // First pass: calculate total weight difference for capacity check
                if (productDetailIDs != null && quantities != null && productDetailIDs.length == quantities.length) {
                    for (int i = 0; i < productDetailIDs.length; i++) {
                        try {
                            String productDetailID = productDetailIDs[i];
                            int newQuantity = Integer.parseInt(quantities[i]);

                            // Fetch existing TODetail
                            TODetail existingToDetail = transferOrderDAO.getTODetailByProductDetailID(transferOrder.getToID(), productDetailID);

                            if (existingToDetail != null) {
                                int oldQuantity = existingToDetail.getQuantity();
                                int quantityDifference = newQuantity - oldQuantity;

                                // Only add to weight difference if we're increasing quantity
                                if (quantityDifference > 0) {
                                    double productWeight = transferOrderDAO.getProductWeight(productDetailID);
                                    totalWeightDifference += productWeight * quantityDifference;
                                }
                            }
                        } catch (NumberFormatException e) {
                            conn.rollback();
                            System.out.println("Transaction rolled back - Invalid quantity format");
                            request.setAttribute("errorMessage", "Invalid quantity format.");
                            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                // Check final bin capacity if there's a positive weight difference
                if (totalWeightDifference > 0) {
                    double binMaxCapacity = transferOrderDAO.getBinMaxCapacity(finalBinID);
                    double currentBinWeight = transferOrderDAO.getCurrentBinWeight(finalBinID);
                    double totalWeightAfterTransfer = currentBinWeight + totalWeightDifference;

                    System.out.println("Final Bin Max Capacity: " + binMaxCapacity);
                    System.out.println("Current Bin Weight: " + currentBinWeight);
                    System.out.println("Additional Weight: " + totalWeightDifference);
                    System.out.println("Total After Update: " + totalWeightAfterTransfer);

                    if (totalWeightAfterTransfer > binMaxCapacity) {
                        conn.rollback();
                        System.out.println("Transaction rolled back - Final bin capacity exceeded");
                        request.setAttribute("errorCapacity", "Bin đích không đủ sức chứa cho số lượng sản phẩm này. " +
                                "Sức chứa tối đa: " + binMaxCapacity + " kg. " +
                                "Trọng lượng hiện tại: " + currentBinWeight + " kg. " +
                                "Trọng lượng cần thêm: " + totalWeightDifference + " kg.");
                        request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                        return;
                    }
                }

                // Second pass: process each product detail with the actual updates
                if (productDetailIDs != null && quantities != null && productDetailIDs.length == quantities.length) {
                    for (int i = 0; i < productDetailIDs.length; i++) {
                        try {
                            String productDetailID = productDetailIDs[i];
                            int newQuantity = Integer.parseInt(quantities[i]);

                            // Fetch existing TODetail for the transfer order and product detail
                            TODetail existingToDetail = transferOrderDAO.getTODetailByProductDetailID(transferOrder.getToID(), productDetailID);

                            if (existingToDetail != null) {
                                int oldQuantity = existingToDetail.getQuantity(); // Existing quantity in the transfer order

                                // If new quantity is 0, handle removal logic
                                if (newQuantity == 0) {
                                    // Add the old quantity back to origin bin (reversing the original transfer)
                                    boolean isOriginBinUpdated = transferOrderDAO.updateBinQuantity(conn, originBinID, productDetailID, oldQuantity);
                                    if (!isOriginBinUpdated) {
                                        conn.rollback();
                                        System.out.println("Transaction rolled back - Error restoring quantity to origin bin");
                                        request.setAttribute("errorMessage", "Error restoring quantity to origin bin.");
                                        request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                        return;
                                    }

                                    // Remove the quantity from final bin if it was already transferred
                                    if (transferOrder.getStatus().equals("Completed")) {
                                        boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(conn, finalBinID, productDetailID, -oldQuantity);
                                        if (!isFinalBinUpdated) {
                                            conn.rollback();
                                            System.out.println("Transaction rolled back - Error removing quantity from final bin");
                                            request.setAttribute("errorMessage", "Error removing quantity from final bin.");
                                            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                            return;
                                        }
                                    }
                                } else {
                                    // Calculate the difference in quantities
                                    int quantityDifference = newQuantity - oldQuantity;

                                    // Only check origin bin capacity if we're increasing the quantity
                                    if (quantityDifference > 0) {
                                        // Check available quantity in origin bin
                                        int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                                        if (availableQuantityInOriginBin < quantityDifference) {
                                            conn.rollback();
                                            System.out.println("Transaction rolled back - Not enough quantity in origin bin");
                                            request.setAttribute("errorQuantity", "Không đủ số lượng trong bin nguồn. Số lượng hiện có: "
                                                    + availableQuantityInOriginBin + ", Số lượng cần thêm: " + quantityDifference);
                                            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                            return;
                                        }
                                    }

                                    // Update bins based on the quantity difference
                                    if (quantityDifference != 0) {
                                        // Update origin bin (negative difference means add back, positive means take more)
                                        boolean isOriginBinUpdated = transferOrderDAO.updateBinQuantity(conn, originBinID, productDetailID, -quantityDifference);
                                        if (!isOriginBinUpdated) {
                                            conn.rollback();
                                            System.out.println("Transaction rolled back - Error updating origin bin quantity");
                                            request.setAttribute("errorMessage", "Error updating origin bin quantity.");
                                            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                            return;
                                        }

                                        // If the order is completed, update final bin quantity as well
                                        if (transferOrder.getStatus().equals("Completed")) {
                                            boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(conn, finalBinID, productDetailID, quantityDifference);
                                            if (!isFinalBinUpdated) {
                                                conn.rollback();
                                                System.out.println("Transaction rolled back - Error updating final bin quantity");
                                                request.setAttribute("errorMessage", "Error updating final bin quantity.");
                                                request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                                return;
                                            }
                                        }
                                    }
                                }

                                // Update the existing TODetail with the new quantity
                                existingToDetail.setQuantity(newQuantity);
                                boolean isTODetailUpdated = transferOrderDAO.updateTODetail(existingToDetail);
                                if (!isTODetailUpdated) {
                                    conn.rollback();
                                    System.out.println("Transaction rolled back - Error updating Transfer Order Detail");
                                    request.setAttribute("errorMessage", "Error updating Transfer Order Detail.");
                                    request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                    return;
                                }

                                // If quantity is 0, remove the TODetail record
                                if (newQuantity == 0) {
                                    boolean isDetailRemoved = transferOrderDAO.removeTODetail(existingToDetail.getToDetailID());
                                    if (!isDetailRemoved) {
                                        conn.rollback();
                                        System.out.println("Transaction rolled back - Error removing Transfer Order Detail");
                                        request.setAttribute("errorMessage", "Error removing Transfer Order Detail.");
                                        request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                        return;
                                    }
                                }
                            } else {
                                // If this is a new product being added to the TO
                                if (newQuantity > 0) {
                                    // Check available quantity in origin bin
                                    int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                                    if (availableQuantityInOriginBin < newQuantity) {
                                        conn.rollback();
                                        System.out.println("Transaction rolled back - Not enough quantity in origin bin for new product");
                                        request.setAttribute("errorQuantity", "Không đủ số lượng trong bin nguồn cho sản phẩm mới. Số lượng hiện có: "
                                                + availableQuantityInOriginBin + ", Số lượng cần: " + newQuantity);
                                        request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                        return;
                                    }

                                    // Create new TODetail
                                    TODetail newDetail = new TODetail();
                                    newDetail.setToDetailID(UUID.randomUUID().toString());
                                    newDetail.setProductDetailID(productDetailID);
                                    newDetail.setQuantity(newQuantity);
                                    newDetail.setToID(transferOrder.getToID());
                                    newDetail.setOriginBinID(originBinID);
                                    newDetail.setFinalBinID(finalBinID);

                                    // Update origin bin quantity
                                    boolean isOriginBinUpdated = transferOrderDAO.updateBinQuantity(conn, originBinID, productDetailID, -newQuantity);
                                    if (!isOriginBinUpdated) {
                                        conn.rollback();
                                        System.out.println("Transaction rolled back - Error updating origin bin for new product");
                                        request.setAttribute("errorMessage", "Error updating origin bin for new product.");
                                        request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                        return;
                                    }

                                    // Update final bin if order is completed
                                    if (transferOrder.getStatus().equals("Completed")) {
                                        boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(conn, finalBinID, productDetailID, newQuantity);
                                        if (!isFinalBinUpdated) {
                                            conn.rollback();
                                            System.out.println("Transaction rolled back - Error updating final bin for new product");
                                            request.setAttribute("errorMessage", "Error updating final bin for new product.");
                                            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                            return;
                                        }
                                    }

                                    // Add new TODetail
                                    boolean isDetailAdded = transferOrderDAO.addTODetail(newDetail);
                                    if (!isDetailAdded) {
                                        conn.rollback();
                                        System.out.println("Transaction rolled back - Error adding new Transfer Order Detail");
                                        request.setAttribute("errorMessage", "Error adding new Transfer Order Detail.");
                                        request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                                        return;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            conn.rollback();
                            System.out.println("Transaction rolled back - Invalid quantity format");
                            request.setAttribute("errorMessage", "Invalid quantity format.");
                            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                // Update bin status based on order status
                if (transferOrder.getStatus().equals("Completed")) {
                    // Set final bin status to 1 (available)
                    boolean isFinalBinStatusUpdated = transferOrderDAO.updateBinStatus(conn, finalBinID, 1);
                    if (!isFinalBinStatusUpdated) {
                        conn.rollback();
                        System.out.println("Transaction rolled back - Error updating final bin status");
                        request.setAttribute("errorBinStatus", "Error updating final bin status.");
                        request.getRequestDispatcher("/to-update.jsp").forward(request, response);
                        return;
                    }
                }

                // If we got here, all operations were successful
                conn.commit();
                System.out.println("Transaction committed - Transfer Order updated successfully");

                // Redirect to Transfer Order List page
                response.sendRedirect("/ClothingManagement_war/TOList");
            } else {
                conn.rollback();
                System.out.println("Transaction rolled back - Error updating transfer order");
                request.setAttribute("errorMessage", "Error updating transfer order.");
                request.getRequestDispatcher("/to-update.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/to-update.jsp").forward(request, response);
        }
    }
}

