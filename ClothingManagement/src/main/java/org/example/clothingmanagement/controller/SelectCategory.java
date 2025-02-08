package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.repository.CategoryDAO;
import org.example.clothingmanagement.service.CategoryService;

import java.io.IOException;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


@WebServlet(name = "SelectCategory", value = "/SelectCategory")
public class SelectCategory extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CategoryService dao = new CategoryService();
        List<Category> categories = new ArrayList<>();

        try {
            // Nhận dữ liệu từ form
            String categoryIdStr = request.getParameter("categoryId");
            String categoryName = request.getParameter("categoryName");
            String createDateStr = request.getParameter("createDate");
            String createdByStr = request.getParameter("createBy");

            // Chuyển đổi dữ liệu đầu vào
            Integer categoryId = (categoryIdStr != null && !categoryIdStr.isEmpty()) ? Integer.parseInt(categoryIdStr) : null;
            Date createDate = (createDateStr != null && !createDateStr.isEmpty()) ? java.sql.Date.valueOf(createDateStr) : null;
            Integer createdBy = (createdByStr != null && !createdByStr.isEmpty()) ? Integer.parseInt(createdByStr) : null;

            // Lọc dữ liệu
            categories = dao.filterCategories(categoryId, categoryName, createDate, createdBy);
        } catch (Exception e) {

        }

        // Gửi danh sách danh mục sang JSP
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("Category.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}