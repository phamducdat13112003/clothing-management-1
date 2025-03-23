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

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String toID = request.getParameter("toID");

        if ("cancel".equals(action)) {
            if (toID != null && !toID.trim().isEmpty()) {
                boolean isCanceled = transferOrderDAO.cancelTransferOrder(toID);
                if (isCanceled) {
                    // Redirect to the list page after successful cancellation
                    response.sendRedirect("/ClothingManagement_war/TOList");
                } else {
                    // If cancellation failed, set an error message
                    request.setAttribute("errorMessage", "Error canceling the transfer order.");
                    request.getRequestDispatcher("to-list.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Invalid Transfer Order ID.");
                request.getRequestDispatcher("to-list.jsp").forward(request, response);
            }
        } else if ("done".equals(action)) {
            if (toID != null && !toID.trim().isEmpty()) {
                completeTransferOrder(request, response, toID);
            } else {
                request.setAttribute("errorMessage", "Invalid Transfer Order ID.");
                request.getRequestDispatcher("to-list.jsp").forward(request, response);
            }
        } else {
            // Default: Show all transfer orders
            List<TransferOrder> transferOrders = transferOrderDAO.getAllTransferOrders();
            request.setAttribute("transferOrders", transferOrders);
            request.getRequestDispatcher("to-list.jsp").forward(request, response);
        }
    }

    private void completeTransferOrder(HttpServletRequest request, HttpServletResponse response, String toID)
            throws ServletException, IOException {

        System.out.println("Starting completion process for Transfer Order: " + toID);

        try (Connection conn = DBContext.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            System.out.println("Transaction started");

            // Check if the transfer order exists and has valid status
            TransferOrder transferOrder = transferOrderDAO.getTransferOrderByID(toID);
            if (transferOrder == null) {
                request.setAttribute("errorMessage", "Transfer Order not found.");
                request.getRequestDispatcher("to-list.jsp").forward(request, response);
                return;
            }

            // Allow completion from either Pending or Processing status
            if (!"Processing".equals(transferOrder.getStatus())) {
                request.setAttribute("errorMessage", "Only orders in Processing status can be completed.");
                request.getRequestDispatcher("to-list.jsp").forward(request, response);
                return;
            }

            System.out.println("Transfer Order status is: " + transferOrder.getStatus());

            // Get all details of this transfer order
            List<TODetail> details = transferOrderDAO.getTODetailsByTOID(toID);
            if (details == null || details.isEmpty()) {
                request.setAttribute("errorMessage", "No details found for this transfer order.");
                request.getRequestDispatcher("to-list.jsp").forward(request, response);
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

                        request.setAttribute("successMessage", "Transfer Order completed successfully.");
                        response.sendRedirect("/ClothingManagement_war/TOList");
                    } else {
                        conn.rollback();
                        System.out.println("Transaction rolled back - Failed to update transfer order status");

                        request.setAttribute("errorMessage", "Error updating transfer order status.");
                        request.getRequestDispatcher("to-list.jsp").forward(request, response);
                    }
                }
            } else {
                conn.rollback();
                System.out.println("Transaction rolled back - Failed to update bin quantities");

                request.setAttribute("errorMessage", "Error updating bin quantities.");
                request.getRequestDispatcher("to-list.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during transfer completion: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("to-list.jsp").forward(request, response);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // This might be needed if you want to handle form submissions for completing transfer orders
        doGet(request, response);
    }
}
