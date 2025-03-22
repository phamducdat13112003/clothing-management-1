package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.SupplierService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "AddSupplierServlet", value = "/addsupplier")
public class AddSupplierServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddAcc</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddAcc at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("./addSupplier.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SupplierService supplierService = new SupplierService();
        StringBuilder message = new StringBuilder();
        int page = 1;
        int pageSize = 5;
        int totalSuppliers = 0;
        String supplierId = null;
        try {
            supplierId = generateNewSupplierId();
        } catch (SQLException e) {
            request.setAttribute("message", "Can't create new supplier");
        }
        String name = request.getParameter("name").trim();
        if (!isValidName(name)) {
            request.setAttribute("errorName", "Invalid name");
        }
        name = capitalizeName(name);
        String address = request.getParameter("address").trim();
        address = capitalizeName(address);
        String phone = request.getParameter("phone").trim();
        String email = request.getParameter("email").trim();

        if (!isValidEmail(email)) {
            request.setAttribute("errorEmail", "Invalid email");
        }

        if (!isValidPhone(phone)) {
            request.setAttribute("errorPhone", "Invalid phone number. The telephone number have 10 digits");
        }
        try {
            if (supplierService.isSupplierExistedWhenAdd(email, phone)) {
                message.append("Supplier already existed.\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (supplierId == null || !message.isEmpty() || !isValidName(name) || !isValidEmail(email) || !isValidPhone(phone)) {
            request.setAttribute("message", message.toString());
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.getRequestDispatcher("./addSupplier.jsp").forward(request, response);
        } else {
            Supplier supplier = new Supplier(supplierId, name, address, email, phone, true);
            boolean success = false;
            try {
                success = supplierService.createSupplier(supplier);
            } catch (Exception e) {
                request.setAttribute("message", "Can't add employee.");
            }
            if (success) {
                request.setAttribute("messageSuccess", "Supplier added successfully");
            } else {
                request.setAttribute("message", "Failed to add supplier");
            }
            List<Supplier> list = null;
            try {
                list = supplierService.getSuppliersWithPagination(page, pageSize);
                totalSuppliers = supplierService.getTotalSupplierCount();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int totalPages = (int) Math.ceil((double) totalSuppliers / pageSize);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("list", list);
            request.getRequestDispatcher("./manageSupplier.jsp").forward(request, response);
        }
    }

    private String capitalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return name;
        }
        String[] words = name.trim().split("\\s+");

        StringBuilder capitalizedName = new StringBuilder();

        for (String word : words) {
            capitalizedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase()) // Phần còn lại viết thường
                    .append(" "); // Thêm dấu cách giữa các từ
        }
        return capitalizedName.toString().trim();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{10}$"; // Chỉ cho phép 10 chữ số
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9\\sàáạảãâấầẩẫậăắằẳẵặèéẹẻẽêếềểễệìíịỉĩòóọỏõôốồổỗộơớờởỡợùúụủũưứừửữựýỳỵỷỹđĐ&\\-_.(),/+%#:]+$");
    }

    private String generateNewSupplierId() throws SQLException {
        SupplierService supplierService = new SupplierService();
        String prefix = "SP00"; // Luôn có "EP00"
        int maxId = supplierService.getMaxSupplierId(); // Lấy số lớn nhất từ database
        return prefix + (maxId + 1); // Tăng lên 1 và ghép vào
    }
}
