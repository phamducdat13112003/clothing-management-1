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

        // Fetch all bins (you can use your bin fetching method here)
        List<String> binIds = transferOrderDAO.getAllBinIds();  // Example method to fetch bin IDs
        request.setAttribute("binIds", binIds);
        request.getRequestDispatcher("/test1.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<String> employeeIds = transferOrderDAO.getAllEmployeeIds();
            request.setAttribute("employeeIds", employeeIds);

            // Fetch all bins
            List<String> binIds = transferOrderDAO.getAllBinIds();
            request.setAttribute("binIds", binIds);

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

            // Check if originBinID and finalBinID are the same
            if (originBinID.equals(finalBinID)) {
                request.setAttribute("errorBinSame", "Origin Bin ID and Final Bin ID cannot be the same.");
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

                double totalWeight = 0.0;

                // Validate productDetailIDs and quantities
                for (int i = 0; i < productDetailIDs.length; i++) {
                    try {
                        String productDetailID = productDetailIDs[i];
                        int quantity = Integer.parseInt(quantities[i]);

                        // Retrieve the weight of the product
                        double productWeight = transferOrderDAO.getProductWeight(productDetailID);

                        // Calculate the total weight for this product detail (weight * quantity)
                        totalWeight += productWeight * quantity;

                        // Comprehensive validation block
                        if (quantity <= 0) {
                            request.setAttribute("errorQuantity1", "Quantity must be greater than 0.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Check if the origin bin has enough quantity for the transfer
                        int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                        if (availableQuantityInOriginBin < quantity) {
                            request.setAttribute("errorQuantity2", "Not enough quantity in the origin bin.");
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

                        // Attempt to update bin quantities
                        boolean isOriginBinUpdated = transferOrderDAO.updateBinQuantity(originBinID, productDetailID, -quantity);
                        boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(finalBinID, productDetailID, quantity);

                        if (!isOriginBinUpdated || !isFinalBinUpdated) {
                            request.setAttribute("errorBin", "Error updating bin quantities.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                    } catch (NumberFormatException e) {
                        request.setAttribute("errorQuantity3", "Invalid quantity format.");
                        request.getRequestDispatcher("/test1.jsp").forward(request, response);
                        return;
                    }
                }

                // Retrieve the max capacity and current capacity of the final bin
                double binMaxCapacity = transferOrderDAO.getBinMaxCapacity(finalBinID);
                double currentBinCapacity = transferOrderDAO.getBinCurrentCapacity(finalBinID);

                // Calculate the available capacity of the bin
                double availableCapacity = binMaxCapacity - currentBinCapacity;

                // Compare total weight with available capacity
                if (totalWeight > availableCapacity) {
                    request.setAttribute("errorWeight", "Total weight exceeds the available capacity of the bin.");
                    request.getRequestDispatcher("/test1.jsp").forward(request, response);
                    return;
                }


            } else {
                request.setAttribute("errorOrder", "Error creating transfer order.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Set success message and redirect
            request.setAttribute("successMessage", "Transfer Order Created Successfully.");
            response.sendRedirect("/ClothingManagement_war_exploded/transfer-order/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorGeneral", "An unexpected error occurred.");
            request.getRequestDispatcher("/test1.jsp").forward(request, response);
        }
    }







}
