package org.example.clothingmanagement.controller;

import jakarta.servlet.RequestDispatcher;
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
import org.example.clothingmanagement.repository.SectionDAO;

import java.io.IOException;

@WebServlet("/updateBin")
public class UpdateBinServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BinDAO binDAO;
    private SectionDAO sectionDAO;

    @Override
    public void init() {
        binDAO = new BinDAO();
        sectionDAO = new SectionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();

        // Clear any existing messages in ServletContext
        context.removeAttribute("successMessage");
        context.removeAttribute("errorMessage");

        String binID = request.getParameter("binID");

        try {
            // Get bin by ID using DAO
            Bin bin = binDAO.getBinDetailByBinId(binID);

            if (bin != null) {
                // Get section details
                Section section = sectionDAO.getSectionsById(bin.getSectionID());

                // Set attributes
                request.setAttribute("bin", bin);
                request.setAttribute("section", section);

                // Forward to update page
                RequestDispatcher dispatcher = request.getRequestDispatcher("updateBin.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("section?action=list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();

        String binID = request.getParameter("binID");
        String binName = request.getParameter("binName");
        String statusStr = request.getParameter("status");
        String sectionID = request.getParameter("sectionID");

        boolean status = Boolean.parseBoolean(statusStr);

        try {
            // Get the current bin for reference
            Bin currentBin = binDAO.getBinDetailByBinId(binID);

            // Check for duplicate name - only if name has changed
            if (!binName.equals(currentBin.getBinName())) {
                boolean isDuplicate = binDAO.isBinNameDuplicateInSection(binName, sectionID, binID);
                System.out.println("Duplicate check result: " + isDuplicate);

                if (isDuplicate) {
                    // Store error message in ServletContext
                    context.setAttribute("errorMessage", "A bin with this name already exists in this section.");
                    // Redirect back to update page
                    response.sendRedirect("updateBin?binID=" + binID);
                    return;
                }
            }

            // Update bin properties
            currentBin.setBinName(binName);
            currentBin.setStatus(status);

            // Update bin using DAO
            boolean updated = binDAO.updateBin(currentBin);

            if (updated) {
                // Set success message in ServletContext
                context.setAttribute("successMessage", "Bin updated successfully!");
            } else {
                // Set error message in ServletContext
                context.setAttribute("errorMessage", "Failed to update bin.");
            }

            // Redirect to list-bin page for the specific section
            response.sendRedirect("list-bin?id=" + sectionID);

        } catch (Exception e) {
            e.printStackTrace();
            context.setAttribute("errorMessage", "Error: " + e.getMessage());
            response.sendRedirect("list-bin?id=" + sectionID);
        }
    }
}
