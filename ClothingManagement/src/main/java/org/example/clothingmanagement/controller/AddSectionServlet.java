package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.SectionService;
import org.example.clothingmanagement.service.SectionTypeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AddSectionServlet", value = "/addsection")
public class AddSectionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddAcc</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddAcc at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SectionTypeService sectionTypeService = new SectionTypeService();
        List<SectionType> list= sectionTypeService.getAllSectionTypes();
        request.setAttribute("sectionTypes", list);
        request.getRequestDispatcher("./section-add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SectionService sectionService = new SectionService();
        SectionTypeService sectionTypeService = new SectionTypeService();
        BinService binService = new BinService();
        String sectionName = request.getParameter("sectionName").trim();
        int sectionTypeID = Integer.parseInt(request.getParameter("sectionTypeID"));
        int page = 1;
        int pageSize = 5;
        int totalSections = 0;
        String sectionTypeName = sectionTypeService.getSectionTypeNameById(sectionTypeID);

        if (!isValidSectionName(sectionName)) {
            request.setAttribute("message", "Section name cannot contain special characters");
            List<SectionType> list= sectionTypeService.getAllSectionTypes();
            request.setAttribute("sectionTypes", list);
            request.setAttribute("sectionName", sectionName);
            request.setAttribute("sectionTypeID", sectionTypeID);
            request.getRequestDispatcher("/section-add.jsp").forward(request, response);
            return;
        }

        String sectionId;
        if (sectionTypeName.equalsIgnoreCase("Receipt Storage")) {
            sectionId = "RS";
        } else if (sectionTypeName.equalsIgnoreCase("Others Storage")) {
            sectionId = "OT";
        } else {
            sectionId = sectionTypeName.substring(0, 2).toUpperCase();
        }
        // Lấy số thứ tự tiếp theo
        int nextNumber = sectionService.getNextSectionNumber(sectionId);
        sectionId = sectionId + String.format("%03d", nextNumber);

        if (sectionService.isSectionNameExists(sectionName)) {
            request.setAttribute("message", "Section name already exists. Please use a different name.");
            List<SectionType> list= sectionTypeService.getAllSectionTypes();
            request.setAttribute("sectionName", sectionName);
            request.setAttribute("sectionTypeID", sectionTypeID);
            request.setAttribute("sectionTypes", list);
            request.getRequestDispatcher("/section-add.jsp").forward(request, response);
            return;
        }

        Section section = new Section(sectionId, sectionName, sectionTypeID, 1);
        boolean success = sectionService.insertSection(section);

        if (success) {
            request.setAttribute("messageSuccess", "Section added successfully");
        } else {
            request.setAttribute("message", "Failed to add section");
        }
        List<Section> list = sectionService.getSectionsWithPagination(sectionTypeID, page, pageSize);
        totalSections = sectionService.getTotalSections();
        for (Section s : list) {
            int totalBins = binService.countBinsBySectionId(s.getSectionID());
            s.setNumberOfBins(totalBins);
        }
        SectionType sectionType = null;
        if(sectionTypeService.getSectionTypeById(sectionTypeID).isPresent()){
            sectionType = sectionTypeService.getSectionTypeById(sectionTypeID).get();
        }
        int totalPages = (int) Math.ceil((double) totalSections / pageSize);
        request.setAttribute("list", list);
        request.setAttribute("sectionName", sectionName);
        request.setAttribute("sectionType", sectionType);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./view-section.jsp").forward(request, response);
    }

    private boolean isValidSectionName(String sectionName) {
        return sectionName != null && sectionName.matches("^[a-zA-Z0-9 /]+$");
    }
}
