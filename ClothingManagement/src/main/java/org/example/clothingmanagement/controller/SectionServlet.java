package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.repository.SectionDAO;
import org.example.clothingmanagement.repository.SectionTypeDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/section")
public class SectionServlet extends HttpServlet {
    private SectionDAO sectionDAO;
    private SectionTypeDAO sectionTypeDAO;

    @Override
    public void init() {
        sectionDAO = new SectionDAO();
        sectionTypeDAO = new SectionTypeDAO();
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
            case "showAdd":
                showAddForm(request, response);
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
                addSection(request, response);
                break;
            case "edit":
                updateSection(request, response);
                break;
            default:
                listSections(request, response);
        }
    }

    private void listSections(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Section> sections = sectionDAO.getAllSections();
        request.setAttribute("sections", sections);
        request.getRequestDispatcher("/section-list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newSectionID = sectionDAO.generateSectionID();
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
            showEditForm(request, response);
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
