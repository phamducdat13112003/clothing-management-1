package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.SectionDAO;
import org.example.clothingmanagement.repository.TransferOrderDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;


@WebServlet(name = "TransferOrderUpdateServlet", value = "/TOUpdate")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class TransferOrderUpdateServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TransferOrderUpdateServlet.class.getName());


    private TransferOrderDAO transferOrderDAO;
    private SectionDAO sectionDAO;

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
        sectionDAO = new SectionDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        System.out.println("sections: " + sections);
        request.setAttribute("transferOrder", transferOrder);
        request.setAttribute("toDetails", toDetails);
        request.setAttribute("employeeName", employeeName);
        request.getRequestDispatcher("to-update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    private boolean validateTransferOrderLocationUpdate(String toID, String finalBinID) throws SQLException {
        // Fetch all transfer order details
        List<TODetail> toDetails = transferOrderDAO.getTODetailsByTransferOrderId(toID);

        // Calculate total transfer weight
        double totalTransferWeight = 0.0;
        for (TODetail detail : toDetails) {
            double productWeight = transferOrderDAO.getProductWeight(detail.getProductDetailID());
            totalTransferWeight += productWeight * detail.getQuantity();
        }

        // Check bin capacity
        double binMaxCapacity = transferOrderDAO.getBinMaxCapacity(finalBinID);
        double currentBinWeight = transferOrderDAO.getCurrentBinWeight(finalBinID);
        double pendingTransferWeight = transferOrderDAO.getProcessingTransferTotalWeight(finalBinID);
        double totalWeightAfterTransfer = currentBinWeight + pendingTransferWeight + totalTransferWeight;

        // Log capacity details for debugging
        System.out.println("Final Bin Validation Details:");
        System.out.println("Final Bin Max Capacity: " + binMaxCapacity);
        System.out.println("Current Bin Weight: " + currentBinWeight);
        System.out.println("Pending Transfer Weight: " + pendingTransferWeight);
        System.out.println("Total Transfer Order Weight: " + totalTransferWeight);
        System.out.println("Total Weight After Transfer: " + totalWeightAfterTransfer);

        // Return true if the total weight after transfer does not exceed bin capacity
        return totalWeightAfterTransfer <= binMaxCapacity;
    }

    private String convertToJson(Map<String, Object> map) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            // Fallback to manual JSON creation if Jackson fails
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"JSON conversion error\"}";
        }
    }


}

