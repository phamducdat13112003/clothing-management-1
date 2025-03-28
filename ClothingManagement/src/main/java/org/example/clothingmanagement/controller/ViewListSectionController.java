package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.SectionService;
import org.example.clothingmanagement.service.SectionTypeService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name="ViewListSection",urlPatterns = "/view-list-section")
public class ViewListSectionController extends HttpServlet {
    private final SectionService ss = new SectionService();
    private final BinService bs = new BinService();
    private final SectionTypeService sts = new SectionTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // pagination
        int page = 1;
        int pageSize = 5;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        String sectionId = req.getParameter("sectionId");
        if (sectionId != null) {
            Optional<Section> section = ss.getSectionById(sectionId);
            section.ifPresent(s -> req.setAttribute("section", s));
        }

        int sectionTypeId = Integer.parseInt(req.getParameter("stid"));
        List<Section> list = ss.getSectionsWithPagination(sectionTypeId, page, pageSize);

        for(Section section : list){
            section.setNumberOfBins(bs.getBinsBySectionIdWithoutPagination(section.getSectionID()).size());
        }

        SectionType sectionType = null;
        if(sts.getSectionTypeById(sectionTypeId).isPresent()){
            sectionType = sts.getSectionTypeById(sectionTypeId).get();
        }
        int totalProduct = ss.getSectionsBySectionTypeId(sectionTypeId).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("sectionType", sectionType);
        req.setAttribute("list", list);
        req.getRequestDispatcher("view-section.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sectionTypeId = Integer.parseInt(req.getParameter("stid"));

        String nameSearch = req.getParameter("search") != null ? req.getParameter("search").trim() : "";
        String pageParam = req.getParameter("page");

        int page = 1;
        int pageSize = 5;
        if(!nameSearch.isEmpty()){
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
        }

        List<Section> list = ss.SearchSectionWithPagination(sectionTypeId,nameSearch, page, pageSize);

        for(Section section : list){
            section.setNumberOfBins(bs.getBinsBySectionIdWithoutPagination(section.getSectionID()).size());
        }

        SectionType sectionType = null;
        if(sts.getSectionTypeById(sectionTypeId).isPresent()){
            sectionType = sts.getSectionTypeById(sectionTypeId).get();
        }
        int totalProduct = ss.searchSectionWithoutPagination(sectionTypeId,nameSearch).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("sectionType", sectionType);
        req.setAttribute("list", list);
        req.getRequestDispatcher("view-section.jsp").forward(req, resp);
    }
}
