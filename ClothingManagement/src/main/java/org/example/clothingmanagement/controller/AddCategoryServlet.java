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
            int accountId = (int) session.getAttribute("account_id");
// Lấy EmployeeID từ AccountID
            Integer employeeId = dao.getEmployeeIDByAccountID(accountId);
// Tạo đối tượng Category và tiếp tục xử lý
            Category category = new Category(1, name, new Date(), employeeId);
            List<String> errors = new ArrayList<>();

            // Kiểm tra độ dài không quá 20 ký tự
            if (name.length() > 15) {
                errors.add("Tên danh mục không được dài quá 15 ký tự.");
            }

            // Kiểm tra không chứa ký tự đặc biệt ngoại trừ "-"
            if (!name.matches("^[A-Z][a-zA-Z\\s-]*$")) {
                errors.add("Tên danh mục chỉ được chứa chữ cái, khoảng trắng, dấu '-' và chữ cái đầu phải viết hoa.");
            }

            try {
                // Kiểm tra xem tên danh mục đã tồn tại chưa
                if (dao.checkCategoryNameExist(name)) {
                    errors.add("Tên danh mục này đã tồn tại.");
                }

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



