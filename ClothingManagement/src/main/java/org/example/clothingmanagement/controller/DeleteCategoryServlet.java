package org.example.clothingmanagement.controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.CategoryDAO;
import org.example.clothingmanagement.service.CategoryService;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "DeleteCategoryServlet", value = "/DeleteCategoryServlet")
public class DeleteCategoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       int id = Integer.parseInt(request.getParameter("categoryID"));
        CategoryService dao = new CategoryService();
        try {
            if(!dao.checkProductCategory(id)) {
                dao.deleteCategory(id);
                response.sendRedirect("CategoryServlet");
            }else{
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script type='text/javascript'>");
                out.println("alert('Không thể xóa vì danh mục này vẫn có sản phẩm!');");
                out.println("window.history.back();"); // Quay lại trang trước
                out.println("</script>");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}