package org.example.clothingmanagement.controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.service.CategoryService;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name = "AddCategoryServlet", value = "/AddCategoryServlet")
public class AddCategoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


        public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String name = request.getParameter("categoryName");
            CategoryService dao = new CategoryService();
            HttpSession session = request.getSession();
            String accountId = (String) session.getAttribute("account_id");
// Lấy EmployeeID từ AccountID
            String employeeId = dao.getEmployeeIDByAccountID(accountId);
// Tạo đối tượng Category và tiếp tục xử lý
            Category category = new Category(1, name, new Date(), employeeId);

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
                    dao.addCategory(category);
                    response.sendRedirect("CategoryServlet");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



