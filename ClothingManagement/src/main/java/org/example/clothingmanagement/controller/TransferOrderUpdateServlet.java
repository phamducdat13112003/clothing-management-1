package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.TransferOrderDAO;

import java.io.IOException;
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
        TransferOrder transferOrder = transferOrderDAO.getTransferOrderById(toID);

        // Fetch all the TODetails for this Transfer Order
        List<TODetail> toDetails = transferOrderDAO.getTODetailsByTransferOrderId(toID);

        // Get all employee IDs (optional: for a list of employees)
        List<String> employeeIds = transferOrderDAO.getAllEmployeeIds();
        request.setAttribute("employeeIds", employeeIds);

        if (transferOrder != null) {
            request.setAttribute("transferOrder", transferOrder);
            request.setAttribute("toDetails", toDetails);  // Set the list of TODetails as a separate attribute
            request.getRequestDispatcher("/test2.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Transfer Order not found.");
            request.getRequestDispatcher("/test.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String toID = request.getParameter("toID");
            String createdBy = request.getParameter("createdBy");
            String status = request.getParameter("status");

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

            // Update the Transfer Order in the database
            boolean result = transferOrderDAO.updateTransferOrder(transferOrder);

            if (result) {
                // Retrieve and process the list of product details and quantities to update
                String[] productDetailIDs = request.getParameterValues("productDetailID[]");
                String[] quantities = request.getParameterValues("quantity[]");
                String originBinID = request.getParameter("originBinID");
                String finalBinID = request.getParameter("finalBinID");

                // Update each product in TODetail
                if (productDetailIDs != null && quantities != null && productDetailIDs.length == quantities.length) {
                    for (int i = 0; i < productDetailIDs.length; i++) {
                        try {
                            String productDetailID = productDetailIDs[i];
                            int newQuantity = Integer.parseInt(quantities[i]);

                            // Fetch existing TODetail for the transfer order and product detail
                            TODetail existingToDetail = transferOrderDAO.getTODetailByProductDetailID(toID, productDetailID);

                            if (existingToDetail != null) {
                                int oldQuantity = existingToDetail.getQuantity(); // Existing quantity in the transfer order

                                // If new quantity is 0, subtract the old quantity from bins
                                if (newQuantity == 0) {
                                    // Subtract the old quantity from both origin and final bins
                                    boolean isOriginBinUpdatedOld = transferOrderDAO.updateBinQuantity(originBinID, productDetailID, -oldQuantity);
                                    if (!isOriginBinUpdatedOld) {
                                        request.setAttribute("errorMessage", "Error subtracting old quantity from origin bin.");
                                        request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                        return;
                                    }

                                    boolean isFinalBinUpdatedOld = transferOrderDAO.updateBinQuantity(finalBinID, productDetailID, -oldQuantity);
                                    if (!isFinalBinUpdatedOld) {
                                        request.setAttribute("errorMessage", "Error subtracting old quantity from final bin.");
                                        request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                        return;
                                    }
                                } else {
                                    // Calculate the difference in quantities
                                    int quantityDifference = newQuantity - oldQuantity;

                                    int originBinQuantity = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                                    if (originBinQuantity < quantityDifference) {
                                        request.setAttribute("errorMessage", "Not enough quantity in origin bin.");
                                        request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                        return;
                                    }


                                    // Update the origin bin (subtract the old quantity and add the new quantity)
                                    boolean isOriginBinUpdatedOld = transferOrderDAO.updateBinQuantity(originBinID, productDetailID, -oldQuantity);
                                    if (!isOriginBinUpdatedOld) {
                                        request.setAttribute("errorMessage", "Error subtracting old quantity from origin bin.");
                                        request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                        return;
                                    }

                                    boolean isOriginBinUpdatedNew = transferOrderDAO.updateBinQuantity(originBinID, productDetailID, quantityDifference);
                                    if (!isOriginBinUpdatedNew) {
                                        request.setAttribute("errorMessage", "Error adding new quantity to origin bin.");
                                        request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                        return;
                                    }

                                    // Update the final bin (subtract the old quantity and add the new quantity)
                                    boolean isFinalBinUpdatedOld = transferOrderDAO.updateBinQuantity(finalBinID, productDetailID, -oldQuantity);
                                    if (!isFinalBinUpdatedOld) {
                                        request.setAttribute("errorMessage", "Error subtracting old quantity from final bin.");
                                        request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                        return;
                                    }

                                    boolean isFinalBinUpdatedNew = transferOrderDAO.updateBinQuantity(finalBinID, productDetailID, quantityDifference);
                                    if (!isFinalBinUpdatedNew) {
                                        request.setAttribute("errorMessage", "Error adding new quantity to final bin.");
                                        request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                        return;
                                    }
                                }

                                // Update the existing TODetail with the new quantity
                                existingToDetail.setQuantity(newQuantity);
                                boolean isTODetailUpdated = transferOrderDAO.updateTODetail(existingToDetail);
                                if (!isTODetailUpdated) {
                                    request.setAttribute("errorMessage", "Error updating Transfer Order Detail.");
                                    request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                    return;
                                }
                            } else {
                                request.setAttribute("errorMessage", "Transfer Order Detail not found.");
                                request.getRequestDispatcher("/test2.jsp").forward(request, response);
                                return;
                            }
                        } catch (NumberFormatException e) {
                            request.setAttribute("errorMessage", "Invalid quantity format.");
                            request.getRequestDispatcher("/test2.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                // Redirect to Transfer Order List page
                response.sendRedirect("/ClothingManagement_war_exploded/transfer-order/list");
            } else {
                request.setAttribute("errorMessage", "Error updating transfer order.");
                request.getRequestDispatcher("/test2.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            request.getRequestDispatcher("/test2.jsp").forward(request, response);
        }
    }



}

