package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.service.CategoryService;

import java.io.IOException;

import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "SelectCategory", value = "/SelectCategory")
public class SelectCategory extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CategoryService dao = new CategoryService();
        List<Category> categories = new ArrayList<>();
        Map<Integer, String> createdByNames = null;
        try {
            // Lấy dữ liệu từ form
            String categoryName = request.getParameter("categoryName");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String createdByStr = request.getParameter("createBy");

            // Chuyển đổi dữ liệu
            Date startDate = (startDateStr != null && !startDateStr.isEmpty()) ? java.sql.Date.valueOf(startDateStr) : null;
            Date endDate = (endDateStr != null && !endDateStr.isEmpty()) ? java.sql.Date.valueOf(endDateStr) : null;

            Integer createdBy = null;
            if (createdByStr != null && !createdByStr.trim().isEmpty()) {
                createdBy = dao.getEmployeeIDByName(createdByStr);

            }
            // Lọc dữ liệu
            categories = dao.filterCategories(categoryName, startDate, endDate, createdBy);
            createdByNames = new HashMap<>();
            for (Category category : categories) {
                createdBy = category.getCreatedBy();
                if (!createdByNames.containsKey(createdBy)) {
                    createdByNames.put(createdBy, CategoryService.getEmployeeNameByCreatedBy(createdBy));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Gửi danh sách danh mục sang JSP
        request.setAttribute("categories", categories);
        request.setAttribute("createdByNames", createdByNames);
        request.getRequestDispatcher("Category.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}