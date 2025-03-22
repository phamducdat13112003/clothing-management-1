package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.repository.InventoryDocDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/form")
public class FormServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SectionType> sectionTypes = InventoryDocDAO.getAllSectionTypes();
        request.setAttribute("sectionTypes", sectionTypes);

        String sectionTypeID = request.getParameter("sectionTypeID");
        if (sectionTypeID != null) {
            List<Section> sections = InventoryDocDAO.getSectionsByType(Integer.parseInt(sectionTypeID));
            request.setAttribute("sections", sections);
        }

        String sectionID = request.getParameter("sectionID");
        if (sectionID != null) {
            List<Employee> employeeList = InventoryDocDAO.getAllEmployee();
            request.setAttribute("employeeList", employeeList);
            // Lấy danh sách Bin có Status = 1
            List<Bin> binsStatusOne = InventoryDocDAO.getBinsBySectionWithStatusOne(sectionID);
            request.setAttribute("binsStatusOne", binsStatusOne);
        }

        request.getRequestDispatcher("createInventory.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inventoryDocId = request.getParameter("inventoryDocId");
        String binId = request.getParameter("binId");
        InventoryDocDAO.updateInventoryDocStatus(inventoryDocId,"Cancel");
        InventoryDocDAO.changeBinStatus(binId,1);

        response.sendRedirect("ViewInventoryDocList");


    }
}
