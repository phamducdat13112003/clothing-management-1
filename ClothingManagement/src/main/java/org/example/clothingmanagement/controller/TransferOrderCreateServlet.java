package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.EmployeeDAO;
import org.example.clothingmanagement.repository.TransferOrderDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebServlet(name = "CreateTransferOrderServlet", value = "/transfer-order/create")
public class TransferOrderCreateServlet extends HttpServlet {
    private TransferOrderDAO transferOrderDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get all employee IDs (optional: for a list of employees)
        List<String> employeeIds = transferOrderDAO.getAllEmployeeIds();
        request.setAttribute("employeeIds", employeeIds);
        request.getRequestDispatcher("/test1.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get all employee IDs once
            List<String> employeeIds = transferOrderDAO.getAllEmployeeIds();
            request.setAttribute("employeeIds", employeeIds);

            // Get Transfer Order ID from user input
            String toID = request.getParameter("toID");
            if (toID == null || toID.trim().isEmpty()) {
                request.setAttribute("errorToID", "Transfer Order ID cannot be empty.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Check if Transfer Order ID already exists
            if (transferOrderDAO.isTransferOrderIDExist(toID)) {
                request.setAttribute("errorToID", "Transfer Order ID already exists.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            String createdBy = request.getParameter("createdBy");
            String status = "Processing"; // "Processing" status

            // Retrieve and validate createdDate
            LocalDate createdDate;
            try {
                createdDate = LocalDate.parse(request.getParameter("createdDate"));
            } catch (DateTimeParseException e) {
                request.setAttribute("errorDate", "Invalid date format.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Retrieve bin information
            String originBinID = request.getParameter("originBinID");
            String finalBinID = request.getParameter("finalBinID");

            // Validate bins
            if (!transferOrderDAO.isBinValid(originBinID)) {
                request.setAttribute("errorOriginBin", "Origin Bin ID is invalid.");
            }

            if (!transferOrderDAO.isBinValid(finalBinID)) {
                request.setAttribute("errorFinalBin", "Final Bin ID is invalid.");
            }

            // Check if originBinID and finalBinID are the same
            if (originBinID.equals(finalBinID)) {
                request.setAttribute("errorBinSame", "Origin Bin ID and Final Bin ID cannot be the same.");
            }

            // If there is an error with the bins, redirect back to the form with error messages
            if (request.getAttribute("errorOriginBin") != null || request.getAttribute("errorFinalBin") != null || request.getAttribute("errorBinSame") != null) {
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Create a Transfer Order object with the selected createdBy
            TransferOrder transferOrder = new TransferOrder(toID, createdDate, createdBy, status);

            // Insert Transfer Order into the database
            boolean isOrderCreated = transferOrderDAO.createTransferOrder(transferOrder);

            if (isOrderCreated) {
                // Retrieve ProductDetailID and Quantity from form
                String[] productDetailIDs = request.getParameterValues("productDetailID[]");
                String[] quantities = request.getParameterValues("quantity[]");

                // Check if productDetailIDs and quantities are not null and valid
                if (productDetailIDs == null || quantities == null || productDetailIDs.length == 0 || quantities.length == 0) {
                    request.setAttribute("errorProduct", "Transfer Order must contain at least one product.");
                    request.getRequestDispatcher("/test1.jsp").forward(request, response);
                    return;
                }

                if (productDetailIDs.length != quantities.length) {
                    request.setAttribute("errorProduct", "Product Detail IDs and Quantities do not match.");
                    request.getRequestDispatcher("/test1.jsp").forward(request, response);
                    return;
                }

                // Check for duplicate productDetailIDs
                Set<String> productDetailIDSet = new HashSet<>();
                for (String productDetailID : productDetailIDs) {
                    if (!productDetailIDSet.add(productDetailID)) {
                        request.setAttribute("errorProductDetail", "Duplicate Product Detail ID detected.");
                        request.getRequestDispatcher("/test1.jsp").forward(request, response);
                        return;
                    }
                }

                // Validate productDetailIDs and quantities
                for (int i = 0; i < productDetailIDs.length; i++) {
                    try {
                        String productDetailID = productDetailIDs[i];
                        int quantity = Integer.parseInt(quantities[i]);

                        // Ensure quantity is greater than 0
                        if (quantity <= 0) {
                            request.setAttribute("errorQuantity", "Quantity must be greater than 0.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Check if the productDetailID is valid
                        if (!transferOrderDAO.isProductDetailIDValid(productDetailID)) {
                            request.setAttribute("errorProductDetail", "Invalid Product Detail ID.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Check if the origin bin has enough quantity for the transfer
                        int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                        if (availableQuantityInOriginBin < quantity) {
                            request.setAttribute("errorQuantity", "Not enough quantity in the origin bin.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Create TODetail object
                        TODetail toDetail = new TODetail();
                        toDetail.setToDetailID(UUID.randomUUID().toString());
                        toDetail.setProductDetailID(productDetailID);
                        toDetail.setQuantity(quantity);
                        toDetail.setToID(toID);
                        toDetail.setOriginBinID(originBinID);
                        toDetail.setFinalBinID(finalBinID);

                        // Insert TODetail into the database
                        boolean isTODetailCreated = transferOrderDAO.addTODetail(toDetail);
                        if (!isTODetailCreated) {
                            request.setAttribute("errorDetail", "Error creating Transfer Order Detail.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Subtract quantity from origin bin (update BinDetail)
                        boolean isOriginBinUpdated = transferOrderDAO.updateBinQuantity(originBinID, productDetailID, -quantity);
                        if (!isOriginBinUpdated) {
                            request.setAttribute("errorBin", "Error updating origin bin quantity.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Add quantity to final bin (update BinDetail)
                        boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(finalBinID, productDetailID, quantity);
                        if (!isFinalBinUpdated) {
                            request.setAttribute("errorBin", "Error updating final bin quantity.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                    } catch (NumberFormatException e) {
                        request.setAttribute("errorQuantity", "Invalid quantity format.");
                        request.getRequestDispatcher("/test1.jsp").forward(request, response);
                        return;
                    }
                }
            } else {
                request.setAttribute("errorOrder", "Error creating transfer order.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
            }

            // Set success message and redirect
            request.setAttribute("successMessage", "Transfer Order Created Successfully.");
            response.sendRedirect("/ClothingManagement_war/transfer-order/list");  // Redirect to the list of transfer orders

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorGeneral", "An unexpected error occurred.");
            request.getRequestDispatcher("/test1.jsp").forward(request, response);
        }
    }



}
