package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.repository.SectionDAO;
import org.example.clothingmanagement.service.SectionTypeService;

import java.io.IOException;
import java.util.List;

@WebServlet(name="ViewListSectionType",urlPatterns = "/view-list-section-type")
public class ViewListSectionTypeController extends HttpServlet {
    private final SectionTypeService sts = new SectionTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<SectionType> sectionTypes = sts.getSectionTypesWithPagination(page,pageSize);
        int totalProduct = sts.getAllSectionTypes().size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);



        req.setAttribute("list", sectionTypes);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("list-section-type.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        List<SectionType> list = sts.searchSectionTypesWithPagination(nameSearch,page,pageSize);
        int totalProducts = sts.searchSectionTypes(nameSearch).size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        req.setAttribute("list", list);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("search", nameSearch);
        req.getRequestDispatcher("list-section-type.jsp").forward(req, resp);
    }
}
