package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.EmployeeDAO;
import org.example.clothingmanagement.repository.TransferOrderDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "CreateTransferOrderServlet", value = "/TOCreate")
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
        // Tự gen to id mới
        String nextToID = transferOrderDAO.getNextToID();
        request.setAttribute("nextToID", nextToID);

        // lấy all bin
        List<String> binIds = transferOrderDAO.getAllBinIds();
        request.setAttribute("binIds", binIds);
        request.getRequestDispatcher("to-create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<String> binIds = transferOrderDAO.getAllBinIds();
            request.setAttribute("binIds", binIds);

            // validate
            if (!validateTransferOrderCreation(request)) {
                request.getRequestDispatcher("to-create.jsp").forward(request, response);
                return;
            }

            // get to ID
            String toID = request.getParameter("toID");
            if (toID == null || toID.trim().isEmpty()) {
                request.setAttribute("errorToID", "Transfer Order ID không được để trống.");
                request.getRequestDispatcher("to-create.jsp").forward(request, response);
                return;
            }

            // check toID tồn tại không
            if (transferOrderDAO.isTransferOrderIDExist(toID)) {
                request.setAttribute("errorToID", "Transfer Order ID đã tồn tại.");
                request.getRequestDispatcher("to-create.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin người tạo và trạng thái
            String createdBy = request.getParameter("createdBy");
            String status = "Processing";

            // Xác thực ngày tạo
            LocalDate createdDate;
            try {
                createdDate = LocalDate.parse(request.getParameter("createdDate"));
            } catch (DateTimeParseException e) {
                request.setAttribute("errorDate", "Định dạng ngày không hợp lệ.");
                request.getRequestDispatcher("to-create.jsp").forward(request, response);
                return;
            }

            // Xác thực bin
            String originBinID = request.getParameter("originBinID");
            String finalBinID = request.getParameter("finalBinID");

            // Kiểm tra bin nguồn và bin đích không được trùng nhau
            if (originBinID.equals(finalBinID)) {
                request.setAttribute("errorBinSame", "Bin nguồn và Bin đích không được trùng nhau.");
                request.getRequestDispatcher("to-create.jsp").forward(request, response);
                return;
            }

            // Tạo Transfer Order
            TransferOrder transferOrder = new TransferOrder(toID, createdDate, createdBy, status);

            // Thêm Transfer Order vào cơ sở dữ liệu
            boolean isOrderCreated = transferOrderDAO.createTransferOrder(transferOrder);

            if (isOrderCreated) {
                // Lấy thông tin chi tiết sản phẩm
                String[] productDetailIDs = request.getParameterValues("productDetailID[]");
                String[] quantities = request.getParameterValues("quantity[]");

                double totalWeight = 0.0;
                System.out.println("Tổng trọng lượng trước khi xử lý: " + totalWeight);

                // Xác thực và xử lý từng sản phẩm
                for (int i = 0; i < productDetailIDs.length; i++) {
                    try {
                        String productDetailID = productDetailIDs[i];
                        int quantity = Integer.parseInt(quantities[i]);

                        // Xác thực số lượng
                        if (quantity <= 0) {
                            request.setAttribute("errorQuantity1", "Số lượng phải lớn hơn 0.");
                            request.getRequestDispatcher("to-create.jsp").forward(request, response);
                            return;
                        }

                        // Kiểm tra số lượng trong bin nguồn
                        int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                        if (availableQuantityInOriginBin < quantity) {
                            request.setAttribute("errorQuantity2", "Số lượng không đủ trong bin nguồn.");
                            request.getRequestDispatcher("to-create.jsp").forward(request, response);
                            return;
                        }

                        // Tính trọng lượng sản phẩm
                        double productWeight = transferOrderDAO.getProductWeight(productDetailID);
                        System.out.println("Trọng lượng sản phẩm cho " + productDetailID + ": " + productWeight); // Ghi log trọng lượng sản phẩm

                        totalWeight += productWeight * quantity;
                        System.out.println("Cập nhật tổng trọng lượng: " + totalWeight); // Ghi log trọng lượng đã cập nhật

                        // Tạo TODetail
                        TODetail toDetail = new TODetail();
                        toDetail.setToDetailID(UUID.randomUUID().toString());
                        toDetail.setProductDetailID(productDetailID);
                        toDetail.setQuantity(quantity);
                        toDetail.setToID(toID);
                        toDetail.setOriginBinID(originBinID);
                        toDetail.setFinalBinID(finalBinID);

                        // Thêm TODetail vào cơ sở dữ liệu
                        boolean isTODetailCreated = transferOrderDAO.addTODetail(toDetail);
                        if (!isTODetailCreated) {
                            request.setAttribute("errorDetail", "Lỗi khi tạo chi tiết Transfer Order.");
                            request.getRequestDispatcher("to-create.jsp").forward(request, response);
                            return;
                        }

                        // Cập nhật số lượng bin
                        boolean isOriginBinUpdated = transferOrderDAO.updateBinQuantity(originBinID, productDetailID, -quantity);
                        boolean isFinalBinUpdated = transferOrderDAO.updateBinQuantity(finalBinID, productDetailID, quantity);

                        if (!isOriginBinUpdated || !isFinalBinUpdated) {
                            request.setAttribute("errorBin", "Lỗi khi cập nhật số lượng bin.");
                            request.getRequestDispatcher("to-create.jsp").forward(request, response);
                            return;
                        }

                    } catch (NumberFormatException e) {
                        request.setAttribute("errorQuantity3", "Định dạng số lượng không hợp lệ.");
                        request.getRequestDispatcher("to-create.jsp").forward(request, response);
                        return;
                    }
                }

            } else {
                request.setAttribute("errorOrder", "Lỗi khi tạo Transfer Order.");
                request.getRequestDispatcher("to-create.jsp").forward(request, response);
                return;
            }

            // Chuyển hướng sau khi tạo thành công
            request.setAttribute("successMessage", "Transfer Order đã được tạo thành công.");
            response.sendRedirect("/ClothingManagement_war_exploded/TOList");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorGeneral", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("to-create.jsp").forward(request, response);
        }
    }


    private boolean validateTransferOrderCreation(HttpServletRequest request) {
        // Kiểm tra Transfer Order ID
        String toID = request.getParameter("toID");
        if (toID == null || toID.trim().isEmpty()) {
            request.setAttribute("errorToID", "Transfer Order ID không được để trống.");
            return false;
        }

        if (transferOrderDAO.isTransferOrderIDExist(toID)) {
            request.setAttribute("errorToID", "Transfer Order ID đã tồn tại.");
            return false;
        }

        // Xác thực ngày tạo
        try {
            LocalDate createdDate = LocalDate.parse(request.getParameter("createdDate"));
            // Có thể thêm logic kiểm tra ngày không được là ngày tương lai
        } catch (DateTimeParseException e) {
            request.setAttribute("errorDate", "Định dạng ngày không hợp lệ.");
            return false;
        }

        // Xác thực Bin
        String originBinID = request.getParameter("originBinID");
        String finalBinID = request.getParameter("finalBinID");

        if (originBinID.equals(finalBinID)) {
            request.setAttribute("errorBinSame", "Bin nguồn và Bin đích không được trùng nhau.");
            return false;
        }

        // Xác thực sản phẩm và số lượng
        String[] productDetailIDs = request.getParameterValues("productDetailID[]");
        String[] quantities = request.getParameterValues("quantity[]");

        if (productDetailIDs == null || productDetailIDs.length == 0) {
            request.setAttribute("errorProduct", "Không có sản phẩm nào được chọn cho chuyển kho.");
            return false;
        }

        double totalWeight = 0.0;
        for (int i = 0; i < productDetailIDs.length; i++) {
            try {
                String productDetailID = productDetailIDs[i];
                int quantity = Integer.parseInt(quantities[i]);

                // Xác thực số lượng
                if (quantity <= 0) {
                    request.setAttribute("errorQuantity1", "Số lượng phải lớn hơn 0.");
                    return false;
                }

                // Kiểm tra số lượng trong bin nguồn
                int availableQuantityInOriginBin = transferOrderDAO.getBinQuantity(originBinID, productDetailID);
                if (availableQuantityInOriginBin < quantity) {
                    request.setAttribute("errorQuantity2", "Số lượng không đủ trong bin nguồn.");
                    return false;
                }

                // Tính tổng trọng lượng
                double productWeight = transferOrderDAO.getProductWeight(productDetailID);
                totalWeight += productWeight * quantity;
            } catch (NumberFormatException e) {
                request.setAttribute("errorQuantity3", "Định dạng số lượng không hợp lệ.");
                return false;
            }
        }


        return true;
    }
}
