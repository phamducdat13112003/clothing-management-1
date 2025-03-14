package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.service.SectionTypeService;

import java.io.IOException;
import java.util.List;

@WebServlet(name="ViewListSectionType",urlPatterns = "/view-list-section-type")
public class ViewListSectionTypeController extends HttpServlet {
    private final SectionTypeService sts = new SectionTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SectionType> list = sts.getAllSectionTypes();
        req.setAttribute("list", list);
        req.getRequestDispatcher("list-section-type.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
