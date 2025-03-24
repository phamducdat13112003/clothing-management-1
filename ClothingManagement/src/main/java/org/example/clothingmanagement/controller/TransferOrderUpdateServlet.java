package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.DBContext;
import org.example.clothingmanagement.repository.SectionDAO;
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
    private SectionDAO sectionDAO;

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
        sectionDAO = new SectionDAO();
    }

    @Override
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


}

