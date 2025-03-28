package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.repository.BinDAO;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.SectionService;

import java.io.IOException;
@WebServlet("/addBin")
public class AddBinServlet extends HttpServlet {
    private static final double MAX_SECTION_CAPACITY = 500.00; // 500 kg
    private static final double[] ALLOWED_BIN_CAPACITIES = {50.00, 100.00, 150.00};

    private BinService binService = new BinService();
    private SectionService sectionService = new SectionService();
    private BinDAO binDAO = new BinDAO(); // Add BinDAO instance

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();

        // Clear any existing messages in ServletContext
        context.removeAttribute("errorMessage");
        context.removeAttribute("successMessage");

        String sectionId = request.getParameter("sectionID");

        // Validate section exists
        Section section = sectionService.getSectionById(sectionId)
                .orElseThrow(() -> new ServletException("Invalid section ID"));

        // Generate next bin ID
        String nextBinID = binService.generateNextBinId(sectionId);

        // Set attributes for the view
        request.setAttribute("section", section);
        request.setAttribute("nextBinID", nextBinID);
        request.getRequestDispatcher("addBin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();

        // Retrieve parameters
        String sectionId = request.getParameter("sectionID");
        String binID = request.getParameter("binID");
        String binName = request.getParameter("binName").trim();
        boolean status = Boolean.parseBoolean(request.getParameter("status"));

        // Validate input parameters
        if (sectionId == null || binID == null || binName.isEmpty()) {
            context.setAttribute("errorMessage", "Invalid input parameters.");
            response.sendRedirect("addBin?sectionID=" + sectionId);
            return;
        }

        // Validate max capacity
        double maxCapacity;
        try {
            maxCapacity = Double.parseDouble(request.getParameter("maxCapacity"));

            // Check if capacity is valid
            boolean isValidCapacity = false;
            for (double allowedCapacity : ALLOWED_BIN_CAPACITIES) {
                if (maxCapacity == allowedCapacity) {
                    isValidCapacity = true;
                    break;
                }
            }

            if (!isValidCapacity) {
                throw new IllegalArgumentException("Invalid bin capacity");
            }
        } catch (IllegalArgumentException e) {
            context.setAttribute("errorMessage", "Please select a valid bin capacity (50, 100, or 150 kg).");
            response.sendRedirect("addBin?sectionID=" + sectionId);
            return;
        }

        // Validate section
        Section section = sectionService.getSectionById(sectionId)
                .orElseThrow(() -> new ServletException("Invalid section ID"));

        // Check for duplicate bin name using BinDAO method
        boolean isDuplicate = binDAO.isBinNameDuplicateInSection(binName, sectionId, null);
        if (isDuplicate) {
            context.setAttribute("errorMessage",
                    "A bin with the name '" + binName + "' already exists in this section.");
            response.sendRedirect("addBin?sectionID=" + sectionId);
            return;
        }

        // Calculate current section capacity
        double currentSectionCapacity = binService.getTotalBinCapacityForSection(sectionId);

        // Check section capacity limit
        if (currentSectionCapacity + maxCapacity > MAX_SECTION_CAPACITY) {
            context.setAttribute("errorMessage",
                    String.format("Cannot add bin. Total section capacity would exceed %.2f kg. Current capacity is %.2f kg.",
                            MAX_SECTION_CAPACITY, currentSectionCapacity));
            response.sendRedirect("addBin?sectionID=" + sectionId);
            return;
        }

        // Create and add new bin
        Bin newBin = new Bin();
        newBin.setBinID(binID);
        newBin.setSectionID(sectionId);
        newBin.setBinName(binName);
        newBin.setStatus(status);
        newBin.setMaxCapacity(maxCapacity);

        // Attempt to add bin
        boolean success = binService.addBin(newBin);

        if (success) {
            context.setAttribute("successMessage", "Bin added successfully!");
            response.sendRedirect("list-bin?id=" + sectionId);
        } else {
            context.setAttribute("errorMessage", "Failed to add bin. Please try again.");
            response.sendRedirect("addBin?sectionID=" + sectionId);
        }
    }
}
