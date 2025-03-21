package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.repository.BinDAO;
import org.example.clothingmanagement.repository.SectionDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addBin")
public class AddBinServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get all sections for dropdown selection
        SectionDAO sectionDAO = new SectionDAO();
        List<String> sectionIds = sectionDAO.getAllSectionIds();
        request.setAttribute("sections", sectionIds);

        request.getRequestDispatcher("addBin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters from form
        String binId = request.getParameter("binId");
        String binName = request.getParameter("binName");
        String maxCapacityStr = request.getParameter("maxCapacity");
        String sectionId = request.getParameter("sectionId");
        String statusStr = request.getParameter("status");

        // Validate inputs
        if (binId == null || binId.trim().isEmpty() ||
                binName == null || binName.trim().isEmpty() ||
                maxCapacityStr == null || maxCapacityStr.trim().isEmpty() ||
                sectionId == null || sectionId.trim().isEmpty() ||
                statusStr == null) {

            request.setAttribute("errorMessage", "All fields are required");
            doGet(request, response);
            return;
        }

        try {
            // Parse values
            double maxCapacity = Double.parseDouble(maxCapacityStr);
            boolean status = "1".equals(statusStr);

            // Create Bin object using regular constructor
            Bin bin = new Bin();
            bin.setBinID(binId);
            bin.setBinName(binName);
            bin.setMaxCapacity(maxCapacity);
            bin.setStatus(status);
            bin.setCurrentCapacity(0.0);
            bin.setSectionID(sectionId);

            // Save to database
            BinDAO binDAO = new BinDAO();
            boolean success = binDAO.addBin(bin);

            if (success) {
                request.setAttribute("successMessage", "Bin added successfully");
                // Redirect to bin list
                response.sendRedirect(request.getContextPath() + "/bins?sectionId=" + sectionId);
            } else {
                request.setAttribute("errorMessage", "Failed to add bin. Please try again.");
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid capacity format. Please enter a valid number.");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            doGet(request, response);
        }
    }
}
