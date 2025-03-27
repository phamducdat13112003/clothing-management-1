package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.SectionDAO;
import org.example.clothingmanagement.repository.TransferOrderDAO;
import org.example.clothingmanagement.repository.VirtualBinDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/updateLocation")
public class UpdateLocationServlet extends HttpServlet {
    private TransferOrderDAO transferOrderDAO;
    private SectionDAO sectionDAO;
    private VirtualBinDAO virtualBinDAO;

    public void init() {
        transferOrderDAO = new TransferOrderDAO();
        sectionDAO = new SectionDAO();
        virtualBinDAO = new VirtualBinDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String toID = request.getParameter("toID");

        // Fetch the Transfer Order based on the provided toID
        TransferOrder transferOrder = transferOrderDAO.getTransferOrderById(toID);

        String employeeName = null;
        try {
            employeeName = transferOrderDAO.getEmployeeNameByID(transferOrder.getCreatedBy());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            System.out.println("original bin id:" + detail.getOriginBinID() + ". final bin id:" + detail.getFinalBinID());
        }

        // Fetch all sections for the dropdown
        List<Section> sections = sectionDAO.getAllSections();



        String originalSectionID = transferOrderDAO.getSectionByBinID(toDetails.get(0).getOriginBinID());
        String finalSectionID = transferOrderDAO.getSectionByBinID(toDetails.get(toDetails.size() - 1).getFinalBinID());
        System.out.println("original section id: " + originalSectionID + ", final section id: " + finalSectionID);

        request.setAttribute("originalSectionID", originalSectionID);
        request.setAttribute("finalSectionID", finalSectionID);

        // Pass the Transfer Order and its details to the JSP
        request.setAttribute("sections", sections);
        request.setAttribute("transferOrder", transferOrder);
        request.setAttribute("employeeName", employeeName);
        request.getRequestDispatcher("updateLocation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters from the form
        String toID = request.getParameter("toID");
        String finalSectionID = request.getParameter("finalSectionID");
        String finalBinID = request.getParameter("finalBinID");

        try {
            // Get the current transfer order status
            TransferOrder transferOrder = transferOrderDAO.getTransferOrderById(toID);
            String status = transferOrder.getStatus();

            // Validate the transfer order update
            if (!validateTransferOrderUpdate(toID, finalBinID)) {
                // If validation fails, return to the update page with error messages
                List<Section> sections = sectionDAO.getAllSections();
                List<TODetail> toDetails = transferOrderDAO.getTODetailsByTransferOrderId(toID);

                request.setAttribute("sections", sections);
                request.setAttribute("transferOrder", transferOrder);
                request.setAttribute("errorMessage", "Final bin do not have enough space! Please choose another bin.");
                request.getRequestDispatcher("updateLocation.jsp").forward(request, response);
                return;
            }

            if (!validateUnchangedBinID(toID, finalBinID)) {
                List<Section> sections = sectionDAO.getAllSections();
                request.setAttribute("sections", sections);
                request.setAttribute("transferOrder", transferOrder);
                // Handle the case where bin is not changed
                request.setAttribute("errorMessage", "Please select a bin different from the current final bin.");
                request.getRequestDispatcher("updateLocation.jsp").forward(request, response);
                return;
            }

            boolean updated;
            // Different update logic based on transfer order status
            if ("PENDING".equalsIgnoreCase(status)) {
                // For pending status, just update transfer order location
                updated = transferOrderDAO.updateTransferOrderLocation(toID, finalBinID);
            } else if ("PROCESSING".equalsIgnoreCase(status)) {
                // For processing status, update both transfer order and virtual bin locations
                updated = updateProcessingTransferOrder(toID, finalBinID);
            } else {
                // For other statuses, throw an exception or handle accordingly
                throw new IllegalStateException("Invalid transfer order status for location update");
            }

            if (updated) {
                HttpSession session = request.getSession();
                session.setAttribute("successMessage", "Transfer Order updated successfully!");
                response.sendRedirect(request.getContextPath() + "/TOUpdate?toID=" + toID);
            } else {
                // If update fails
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "Failed to update Transfer Order!");
                response.sendRedirect(request.getContextPath() + "/TOUpdate?toID=" + toID);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // If an exception occurs
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Failed to update Transfer Order Location!");
            response.sendRedirect(request.getContextPath() + "/TOUpdate?toID=" + toID);
        }
    }

    private boolean updateProcessingTransferOrder(String toID, String finalBinID) throws SQLException {
        // Retrieve virtual bin details for the transfer order
        List<TODetail> toDetails = virtualBinDAO.getToDetail(toID);

        // Update virtual bin entries for each transfer order detail
        for (TODetail detail : toDetails) {
            // Update the final bin ID in virtual bin for this specific transfer order detail
            boolean virtualBinUpdated = virtualBinDAO.updateVirtualBinLocation(
                    detail.getProductDetailID(),
                    toID,
                    finalBinID
            );

            if (!virtualBinUpdated) {
                // Log error or handle failed virtual bin update
                System.err.println("Failed to update virtual bin for product: " + detail.getProductDetailID());
                return false;
            }
        }

        // Update the transfer order location
        return transferOrderDAO.updateTransferOrderLocation(toID, finalBinID);
    }


    private boolean validateTransferOrderUpdate(String toID, String finalBinID) throws SQLException {
        // Retrieve transfer order details to calculate total transfer weight
        List<TODetail> toDetails = transferOrderDAO.getTODetailsByTransferOrderId(toID);


        // Calculate total transfer weight
        double totalTransferWeight = 0.0;
        for (TODetail detail : toDetails) {
            String productDetailID = detail.getProductDetailID();
            int quantity = detail.getQuantity();

            // Get product weight
            double productWeight = transferOrderDAO.getProductWeight(productDetailID);
            totalTransferWeight += productWeight * quantity;
        }

        // Check bin capacity
        double binMaxCapacity = transferOrderDAO.getBinMaxCapacity(finalBinID);
        double currentBinWeight = transferOrderDAO.getCurrentBinWeight(finalBinID);
        double pendingTransferWeight = transferOrderDAO.getProcessingTransferTotalWeight(finalBinID);
        double totalWeightAfterTransfer = currentBinWeight + pendingTransferWeight + totalTransferWeight;

        // Debug logging
        System.out.println("Validation for Transfer Order: " + toID);
        System.out.println("Final Bin ID: " + finalBinID);
        System.out.println("Final Bin Max Capacity: " + binMaxCapacity);
        System.out.println("Current Bin Weight: " + currentBinWeight);
        System.out.println("Pending Transfer Weight: " + pendingTransferWeight);
        System.out.println("Total Transfer Order Weight: " + totalTransferWeight);
        System.out.println("Total Weight After Transfer: " + totalWeightAfterTransfer);

        // Validate bin capacity
        if (totalWeightAfterTransfer > binMaxCapacity) {
            System.out.println("Bin capacity exceeded. Transfer cannot be completed.");
            return false;
        }

        return true;
    }

    private boolean validateUnchangedBinID(String toID, String finalBinID) throws SQLException {
        // Retrieve transfer order details
        List<TODetail> toDetails = transferOrderDAO.getTODetailsByTransferOrderId(toID);

        // Check if no details found
        if (toDetails == null || toDetails.isEmpty()) {
            // Log error or throw exception
            System.err.println("No transfer order details found for TO ID: " + toID);
            return false;
        }

        // Get the current bin ID from the first detail
        String currentBinID = toDetails.get(0).getFinalBinID();

        // Debug logging
        System.out.println("Validating Bin Change:");
        System.out.println("Current Bin ID: " + currentBinID);
        System.out.println("Selected Bin ID: " + finalBinID);

        // Return false if the selected bin is the same as the current bin
        if (finalBinID.equals(currentBinID)) {
            System.out.println("Bin selection is unchanged. Transfer cannot be completed.");
            return false;
        }

        return true;
    }
}
