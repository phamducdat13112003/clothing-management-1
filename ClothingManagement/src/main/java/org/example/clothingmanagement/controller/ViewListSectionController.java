package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.SectionService;

import java.io.IOException;
import java.util.List;

@WebServlet(name="ViewListSection",urlPatterns = "/view-list-section")
public class ViewListSectionController extends HttpServlet {
    private final SectionService ss = new SectionService();
    private final BinService bs = new BinService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sectionTypeId = Integer.parseInt(req.getParameter("id"));
        List<Section> list = ss.getSectionsBySectionTypeId(sectionTypeId);
        for(Section section : list){
            section.setNumberOfBins(bs.getBinsBySectionId(section.getSectionID()).size());
        }
        req.setAttribute("list", list);
        req.getRequestDispatcher("view-section.jsp").forward(req, resp);
    }
}
