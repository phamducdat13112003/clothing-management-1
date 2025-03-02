package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.service.CategoryService;


import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "CategoryServlet", value = "/CategoryServlet")
public class CategoryServlet extends HttpServlet {
    private final CategoryService cs = new CategoryService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ServletException {
        List<Category> categories = cs.selectAll();
        Map<String, String> createdByNames = new HashMap<>();

        for (Category category : categories) {
            String createdBy = category.getCreatedBy();
            if (!createdByNames.containsKey(createdBy)) {
                createdByNames.put(createdBy, CategoryService.getEmployeeNameByCreatedBy(createdBy));
            }
        }

        request.setAttribute("categories", categories);
        request.setAttribute("createdByNames", createdByNames);
        request.getRequestDispatcher("Category.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
           
    }


}