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

@WebServlet(name = "DeleteSectionServlet", value = "/deletesection")
public class DeleteSectionServlet extends HttpServlet {
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
        BinService binService = new BinService();
        SectionService sectionService = new SectionService();
        SectionTypeService sectionTypeService = new SectionTypeService();

        int page = 1;
        int pageSize = 5;
        List<Section> list = null;
        String sectionId = request.getParameter("sectionId");
        int sectionTypeId = sectionService.getSectionTypeIdBySectionId(sectionId);
        int numberofBins = binService.countBinsBySectionId(sectionId);
        boolean deleted = false;
        if (numberofBins == 0) {
            deleted = sectionService.deleteSections(sectionId);
        }
        if (deleted) {
            list = sectionService.getSectionsWithBinCount(page, pageSize, sectionTypeId);
            request.setAttribute("messageSuccess", "Section deleted successfully");
        } else {
            list = sectionService.getSectionsWithBinCount(page, pageSize, sectionTypeId);
            request.setAttribute("message", "Section deletion failed because bin still active");
        }

        SectionType sectionType = sectionTypeService.getSectionTypeBySectionId(sectionId);
        int totalSections = sectionService.getTotalSections();
        int totalPages = (int) Math.ceil((double) totalSections / pageSize);
        request.setAttribute("list", list);
        request.setAttribute("sectionType", sectionType);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./view-section.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
