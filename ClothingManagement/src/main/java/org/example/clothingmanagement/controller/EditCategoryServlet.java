package org.example.clothingmanagement.controller;



import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.repository.CategoryDAO;
import org.example.clothingmanagement.service.CategoryService;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name = "EditCategoryServlet", value = "/EditCategoryServlet")
public class EditCategoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("categoryID"));
          CategoryDAO dao = new CategoryDAO();
        try {
            Category category = dao.getCategoryByID(id);
            request.setAttribute("category", category);
            request.getRequestDispatcher("editCategory.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("categoryId"));
        String name = request.getParameter("categoryName");
        CategoryService dao = new CategoryService();
        HttpSession session = request.getSession();
        String accountId = (String) session.getAttribute("account_id");
// Lấy EmployeeID từ AccountID
        String employeeId = dao.getEmployeeIDByAccountID(accountId);
// Tạo đối tượng Category và tiếp tục xử lý

        Category category = new Category(id, name, new Date(), employeeId);

        List<String> errors = null;
        try {
            errors = dao.validateCategoryName(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            if (!errors.isEmpty()) {
                // Nếu có lỗi, hiển thị tất cả bằng alert
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script type='text/javascript'>");
                out.println("alert('" + errors.stream().map(error -> error.replace("'", "\\'")).collect(Collectors.joining("\\n")) + "');");
                out.println("window.history.back();"); // Quay lại trang trước
                out.println("</script>");

            } else {
                // Nếu không có lỗi, tiến hành cập nhật danh mục
                dao.updateCategory(category);
                response.sendRedirect("CategoryServlet");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}