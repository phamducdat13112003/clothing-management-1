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
            // Lấy danh sách employee và bin để duy trì form state
            List<String> employeeIds = transferOrderDAO.getAllEmployeeIds();
            request.setAttribute("employeeIds", employeeIds);

            List<String> binIds = transferOrderDAO.getAllBinIds();
            request.setAttribute("binIds", binIds);

            // Gọi phương thức validation trước khi xử lý
            if (!validateTransferOrderCreation(request)) {
                // Nếu validation thất bại, chuyển về form với các thông báo lỗi
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Validate Transfer Order ID
            String toID = request.getParameter("toID");
            if (toID == null || toID.trim().isEmpty()) {
                request.setAttribute("errorToID", "Transfer Order ID cannot be empty.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Kiểm tra Transfer Order ID đã tồn tại
            if (transferOrderDAO.isTransferOrderIDExist(toID)) {
                request.setAttribute("errorToID", "Transfer Order ID already exists.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin người tạo và trạng thái
            String createdBy = request.getParameter("createdBy");
            String status = "Processing";

            // Validate ngày tạo
            LocalDate createdDate;
            try {
                createdDate = LocalDate.parse(request.getParameter("createdDate"));
            } catch (DateTimeParseException e) {
                request.setAttribute("errorDate", "Invalid date format.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Validate bin
            String originBinID = request.getParameter("originBinID");
            String finalBinID = request.getParameter("finalBinID");

            // Kiểm tra origin và final bin không được trùng nhau
            if (originBinID.equals(finalBinID)) {
                request.setAttribute("errorBinSame", "Origin Bin ID and Final Bin ID cannot be the same.");
                request.getRequestDispatcher("/test1.jsp").forward(request, response);
                return;
            }

            // Tạo Transfer Order
            TransferOrder transferOrder = new TransferOrder(toID, createdDate, createdBy, status);

            // Thêm Transfer Order vào database
            boolean isOrderCreated = transferOrderDAO.createTransferOrder(transferOrder);

            if (isOrderCreated) {
                // Lấy thông tin chi tiết sản phẩm
                String[] productDetailIDs = request.getParameterValues("productDetailID[]");
                String[] quantities = request.getParameterValues("quantity[]");

                double totalWeight = 0.0;
                System.out.println("Total weight before processing: " + totalWeight); // Log initial total weight

                // Validate và xử lý từng sản phẩm
                for (int i = 0; i < productDetailIDs.length; i++) {
                    try {
                        String productDetailID = productDetailIDs[i];
                        int quantity = Integer.parseInt(quantities[i]);

                        // Validate số lượng
                        if (quantity <= 0) {
                            request.setAttribute("errorQuantity1", "Quantity must be greater than 0.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Kiểm tra số lượng trong bin nguồn
                        int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                        if (availableQuantityInOriginBin < quantity) {
                            request.setAttribute("errorQuantity2", "Not enough quantity in the origin bin.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Tính trọng lượng sản phẩm
                        double productWeight = transferOrderDAO.getProductWeight(productDetailID);
                        System.out.println("Product weight for " + productDetailID + ": " + productWeight); // Log product weight

                        totalWeight += productWeight * quantity;
                        System.out.println("Updated total weight: " + totalWeight); // Log updated total weight

                        // Tạo TODetail
                        TODetail toDetail = new TODetail();
                        toDetail.setToDetailID(UUID.randomUUID().toString());
                        toDetail.setProductDetailID(productDetailID);
                        toDetail.setQuantity(quantity);
                        toDetail.setToID(toID);
                        toDetail.setOriginBinID(originBinID);
                        toDetail.setFinalBinID(finalBinID);

                        // Thêm TODetail vào database
                        boolean isTODetailCreated = transferOrderDAO.addTODetail(toDetail);
                        if (!isTODetailCreated) {
                            request.setAttribute("errorDetail", "Error creating Transfer Order Detail.");
                            request.getRequestDispatcher("/test1.jsp").forward(request, response);
                            return;
                        }

                        // Cập nhật số lượng bin
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

                // Kiểm tra trọng lượng bin cuối
                double binMaxCapacity = transferOrderDAO.getBinMaxCapacity(finalBinID);
                double currentBinCapacity = transferOrderDAO.getBinCurrentCapacity(finalBinID);
                double availableCapacity = binMaxCapacity - currentBinCapacity;

                // Validate trọng lượng
                System.out.println("Bin max capacity: " + binMaxCapacity); // Log max bin capacity
                System.out.println("Current bin capacity: " + currentBinCapacity); // Log current bin capacity
                System.out.println("Available bin capacity: " + availableCapacity); // Log available bin capacity

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

            // Chuyển hướng sau khi tạo thành công
            request.setAttribute("successMessage", "Transfer Order Created Successfully.");
            response.sendRedirect("/ClothingManagement_war_exploded/transfer-order/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorGeneral", "An unexpected error occurred.");
            request.getRequestDispatcher("/test1.jsp").forward(request, response);
        }
    }


    private boolean validateTransferOrderCreation(HttpServletRequest request) {
        // Kiểm tra Transfer Order ID
        String toID = request.getParameter("toID");
        if (toID == null || toID.trim().isEmpty()) {
            request.setAttribute("errorToID", "Transfer Order ID cannot be empty.");
            return false;
        }

        if (transferOrderDAO.isTransferOrderIDExist(toID)) {
            request.setAttribute("errorToID", "Transfer Order ID already exists.");
            return false;
        }

        // Validate ngày tạo
        try {
            LocalDate createdDate = LocalDate.parse(request.getParameter("createdDate"));
            // Có thể thêm logic kiểm tra ngày không được là ngày tương lai
        } catch (DateTimeParseException e) {
            request.setAttribute("errorDate", "Invalid date format.");
            return false;
        }

        // Validate Bin
        String originBinID = request.getParameter("originBinID");
        String finalBinID = request.getParameter("finalBinID");

        if (originBinID.equals(finalBinID)) {
            request.setAttribute("errorBinSame", "Origin Bin ID and Final Bin ID cannot be the same.");
            return false;
        }

        // Validate sản phẩm và số lượng
        String[] productDetailIDs = request.getParameterValues("productDetailID[]");
        String[] quantities = request.getParameterValues("quantity[]");

        if (productDetailIDs == null || productDetailIDs.length == 0) {
            request.setAttribute("errorProduct", "No products selected for transfer.");
            return false;
        }

        double totalWeight = 0.0;
        for (int i = 0; i < productDetailIDs.length; i++) {
            try {
                String productDetailID = productDetailIDs[i];
                int quantity = Integer.parseInt(quantities[i]);

                // Validate số lượng
                if (quantity <= 0) {
                    request.setAttribute("errorQuantity1", "Quantity must be greater than 0.");
                    return false;
                }

                // Kiểm tra số lượng trong bin nguồn
                int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                if (availableQuantityInOriginBin < quantity) {
                    request.setAttribute("errorQuantity2", "Not enough quantity in the origin bin.");
                    return false;
                }

                // Tính tổng trọng lượng
                double productWeight = transferOrderDAO.getProductWeight(productDetailID);
                totalWeight += productWeight * quantity;
            } catch (NumberFormatException e) {
                request.setAttribute("errorQuantity3", "Invalid quantity format.");
                return false;
            }
        }

        // Kiểm tra trọng lượng bin cuối
        double binMaxCapacity = transferOrderDAO.getBinMaxCapacity(finalBinID);
        double currentBinCapacity = transferOrderDAO.getBinCurrentCapacity(finalBinID);
        double availableCapacity = binMaxCapacity - currentBinCapacity;

        if (totalWeight > availableCapacity) {
            request.setAttribute("errorWeight", "Total weight exceeds the available capacity of the bin.");
            return false;
        }

        return true;
    }







}
