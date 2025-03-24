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
    private static final int MAX_BINS_PER_SECTION = 20;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sectionId = request.getParameter("sectionID");

        // Check if bin limit has been reached
        BinDAO binDAO = new BinDAO();
        int currentBinCount = binDAO.getBinCountForSection(sectionId);

        if (currentBinCount >= MAX_BINS_PER_SECTION) {
            request.setAttribute("error", "Maximum limit of " + MAX_BINS_PER_SECTION + " bins reached for this section.");
            request.getRequestDispatcher("section?action=view&id=" + sectionId).forward(request, response);
            return;
        }

        String nextBinID = BinDAO.getNextBinId(sectionId);
        request.setAttribute("nextBinID", nextBinID);
        System.out.println("nextBinID: " + nextBinID);
        request.getRequestDispatcher("addBin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String sectionID = request.getParameter("sectionID");

            // Verify bin limit hasn't been reached
            BinDAO binDAO = new BinDAO();
            int currentBinCount = binDAO.getBinCountForSection(sectionID);

            if (currentBinCount >= MAX_BINS_PER_SECTION) {
                request.setAttribute("error", "Maximum limit of " + MAX_BINS_PER_SECTION + " bins reached for this section.");
                response.sendRedirect("section?action=view&id=" + sectionID);
                return;
            }

            String binID = request.getParameter("nextBinID");
            String binName = request.getParameter("binName");
            double maxCapacity = 100.00; // Fixed capacity value of 100.00 kg

            // Get status from form (default to true/unlock if not provided)
            boolean status = true; // Default to unlock
            String statusParam = request.getParameter("status");
            if (statusParam != null && !statusParam.isEmpty()) {
                status = Boolean.parseBoolean(statusParam); // "true" = unlock, "false" = lock
            }

            // Create and save the bin using your DAO or repository
            Bin newBin = new Bin();
            newBin.setBinID(binID);
            newBin.setSectionID(sectionID);
            newBin.setBinName(binName);
            newBin.setMaxCapacity(maxCapacity);
            newBin.setStatus(status);

            boolean success = binDAO.addBin(newBin);
            if (success) {
                // Redirect to section view or bin list
                response.sendRedirect("section?action=view&id=" + sectionID);
            } else {
                request.setAttribute("error", "Failed to add bin. Please try again.");
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
