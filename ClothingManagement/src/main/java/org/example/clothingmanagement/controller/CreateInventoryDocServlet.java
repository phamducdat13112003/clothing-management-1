package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.repository.InventoryDocDAO;
import org.example.clothingmanagement.repository.InventoryDocDetailDAO;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.InventoryDocService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


@WebServlet(name = "CreateInventoryDocServlet", value = "/CreateInventoryDocServlet")
public class CreateInventoryDocServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<SectionType> sectionTypes = InventoryDocDAO.getAllSectionTypes();
        request.setAttribute("sectionTypes", sectionTypes);

        // Lấy Section theo SectionTypeID nếu có
        String sectionTypeID = request.getParameter("sectionTypeID");
        if (sectionTypeID != null) {
            List<Section> sections = InventoryDocDAO.getSectionsByType(Integer.parseInt(sectionTypeID));
            request.setAttribute("sections", sections);
        }

        // Lấy Bin theo SectionID nếu có
        String sectionID = request.getParameter("sectionID");
        if (sectionID != null) {
            List<Bin> bins = InventoryDocDAO.getBinsBySection(sectionID);
            request.setAttribute("bins", bins);
        }

        request.getRequestDispatcher("createInventory.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String binID = request.getParameter("binId");
        Date createdDate = Date.valueOf(LocalDate.now()); // Lấy ngày hôm nay
        InventoryDocService dao = new InventoryDocService();
        HttpSession session = request.getSession();
        String accountId = (String) session.getAttribute("account_id");
        String employeeId = InventoryDocDAO.getEmployeeIDByAccountID(accountId);

// Nhận CounterID
        String counterID = request.getParameter("employee");

        try {
            // Tạo Inventory Document ID mới
            String inventoryId = dao.generateInventoryDocID();
            dao.createInventoryDoc(inventoryId, employeeId, createdDate, binID, "Pending");

            // Lấy danh sách BinDetail từ binID
            List<BinDetail> binDetails = dao.getBinDetailByBinID(binID);

            // Duyệt danh sách BinDetail để tạo InventoryDocDetail
            for (BinDetail binDetail : binDetails) {
                String productDetailID = binDetail.getProductDetailId();
                int originQuantity = binDetail.getQuantity();
                int recountQuantity = -1; // Ban đầu có thể là -1, sau sẽ cập nhật

                // Tạo InventoryDocDetailID mới
                String inventoryDocDetailID = InventoryDocDetailDAO.generateInventoryDocDetailID();

                // Thêm InventoryDocDetail vào database
                InventoryDocDetailDAO.createInventoryDocDetail(inventoryDocDetailID, productDetailID, counterID, null, originQuantity, recountQuantity, createdDate, inventoryId);


            }
            InventoryDocDAO.changeBinStatus(binID,0);
            response.sendRedirect("ViewInventoryDocList");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}