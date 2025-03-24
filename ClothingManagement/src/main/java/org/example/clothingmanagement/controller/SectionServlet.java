package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.repository.BinDAO;
import org.example.clothingmanagement.repository.SectionDAO;
import org.example.clothingmanagement.repository.SectionTypeDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/section")
public class SectionServlet extends HttpServlet {
    private SectionDAO sectionDAO;
    private SectionTypeDAO sectionTypeDAO;
    private BinDAO binDAO;

    // Maximum number of sections allowed in the warehouse
    private static final int MAX_SECTIONS = 20;

    @Override
    public void init() {
        sectionDAO = new SectionDAO();
        sectionTypeDAO = new SectionTypeDAO();
        binDAO = new BinDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listSections(request, response);
                break;
            case "view":
                viewSection(request, response);
                break;
            case "showAdd":
                checkAndShowAddForm(request, response);
                break;
            case "showEdit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteSection(request, response);
                break;
            default:
                listSections(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add":
                checkAndAddSection(request, response);
                break;
            case "view":
                viewSection(request, response);
                break;
            case "edit":
                updateSection(request, response);
                break;
            default:
                listSections(request, response);
        }
    }


    private boolean canAddMoreSections() {
        int currentSectionCount = sectionDAO.getSectionCount();
        return currentSectionCount < MAX_SECTIONS;
    }

    /**
     * Check if more sections can be added before showing the add form
     */
    private void checkAndShowAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!canAddMoreSections()) {
            request.setAttribute("errorMessage", "Cannot add more sections. Maximum limit of " + MAX_SECTIONS + " sections has been reached.");
            listSections(request, response);
            return;
        }

        showAddForm(request, response);
    }

    /**
     * Check if more sections can be added before actually adding the section
     */
    private void checkAndAddSection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!canAddMoreSections()) {
            request.setAttribute("errorMessage", "Cannot add more sections. Maximum limit of " + MAX_SECTIONS + " sections has been reached.");
            listSections(request, response);
            return;
        }

        addSection(request, response);
    }

    private void viewSection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sectionID = request.getParameter("id");
        Section section = sectionDAO.getSectionByID(sectionID);

        // Get the section type details for this section
        SectionType sectionType = sectionTypeDAO.getSectionTypeById(section.getSectionTypeId());

        List<Bin> bins = binDAO.getBinsBySection(sectionID);

        request.setAttribute("sectionType", sectionType);
        request.setAttribute("section", section);
        request.setAttribute("bins", bins);
        request.getRequestDispatcher("/section-detail.jsp").forward(request, response);
    }

    private void listSections(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Section> sections = sectionDAO.getAllSections();
        for (Section section : sections) {
            int totalBins = binDAO.countBinsBySectionId(section.getSectionID());
            section.setTotalBins(totalBins);
        }

        // Add information about section limits
        int currentSectionCount = sections.size();
        boolean canAddMore = currentSectionCount < MAX_SECTIONS;

        request.setAttribute("sections", sections);
        request.setAttribute("currentSectionCount", currentSectionCount);
        request.setAttribute("maxSections", MAX_SECTIONS);
        request.setAttribute("canAddMoreSections", canAddMore);

        request.getRequestDispatcher("/section-list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newSectionID = sectionDAO.generateSectionID();
        System.out.println("newSection:" + newSectionID);
        List<SectionType> sectionTypes = sectionTypeDAO.getAllSectionTypes();

        request.setAttribute("sectionID", newSectionID);
        request.setAttribute("sectionTypes", sectionTypes);
        request.getRequestDispatcher("/section-add.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sectionID = request.getParameter("id");
        Section section = sectionDAO.getSectionByID(sectionID);

        if (section == null) {
            request.setAttribute("errorMessage", "Section not found with ID: " + sectionID);
            listSections(request, response);
            return;
        }

        List<SectionType> sectionTypes = sectionTypeDAO.getAllSectionTypes();

        request.setAttribute("section", section);
        request.setAttribute("sectionTypes", sectionTypes);
        request.getRequestDispatcher("/section-edit.jsp").forward(request, response);
    }

    private void addSection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sectionID = request.getParameter("sectionID");
        String sectionName = request.getParameter("sectionName");
        int sectionTypeID = Integer.parseInt(request.getParameter("sectionTypeID"));

        // Validation
        if (sectionName == null || sectionName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Section name cannot be empty");
            showAddForm(request, response);
            return;
        }

        // Check for duplicate section name
        if (sectionDAO.isSectionNameExists(sectionName.trim())) {
            request.setAttribute("errorMessage", "Section name already exists. Please use a different name.");

            // Recreate form with entered values
            List<SectionType> sectionTypes = sectionTypeDAO.getAllSectionTypes();
            request.setAttribute("sectionID", sectionID);
            request.setAttribute("sectionName", sectionName);
            request.setAttribute("sectionTypeID", sectionTypeID);
            request.setAttribute("sectionTypes", sectionTypes);

            request.getRequestDispatcher("/section-add.jsp").forward(request, response);
            return;
        }

        Section section = new Section(sectionID, sectionName, sectionTypeID);
        boolean success = sectionDAO.insertSection(section);

        if (success) {
            request.setAttribute("successMessage", "Section added successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to add section");
        }

        listSections(request, response);
    }

    private void updateSection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sectionID = request.getParameter("sectionID");
        String sectionName = request.getParameter("sectionName");
        int sectionTypeID = Integer.parseInt(request.getParameter("sectionTypeID"));

        // Validation
        if (sectionName == null || sectionName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Section name cannot be empty");

            // Recreate the section object to pass back to the form
            Section section = new Section(sectionID, sectionName, sectionTypeID);
            request.setAttribute("section", section);

            List<SectionType> sectionTypes = sectionTypeDAO.getAllSectionTypes();
            request.setAttribute("sectionTypes", sectionTypes);

            request.getRequestDispatcher("/section-edit.jsp").forward(request, response);
            return;
        }

        // Check for duplicate section name (excluding the current section)
        if (sectionDAO.isSectionNameExistsExcludingCurrent(sectionName.trim(), sectionID)) {
            request.setAttribute("errorMessage", "Section name already exists. Please use a different name.");

            // Recreate the section object to pass back to the form
            Section section = new Section(sectionID, sectionName, sectionTypeID);
            request.setAttribute("section", section);

            List<SectionType> sectionTypes = sectionTypeDAO.getAllSectionTypes();
            request.setAttribute("sectionTypes", sectionTypes);

            request.getRequestDispatcher("/section-edit.jsp").forward(request, response);
            return;
        }

        Section section = new Section(sectionID, sectionName, sectionTypeID);
        boolean success = sectionDAO.updateSection(section);

        if (success) {
            request.setAttribute("successMessage", "Section updated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to update section");
        }

        listSections(request, response);
    }

    private void deleteSection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sectionID = request.getParameter("id");
        boolean success = sectionDAO.deleteSection(sectionID);

        if (success) {
            request.setAttribute("successMessage", "Section deleted successfully");
        } else {
            request.setAttribute("errorMessage", "Cannot delete section. It may have associated bins.");
        }

        listSections(request, response);
    }
}
