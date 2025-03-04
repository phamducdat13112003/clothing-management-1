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

@WebServlet(name = "EditSupplierServlet", value = "/editsupplier")
public class EditSupplierServlet extends HttpServlet {

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
            out.println("<h1>Servlet AddAcc at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SupplierService supplierService = new SupplierService();
        String supplierId = request.getParameter("supplierId");
        Supplier supplier = null;
        try {
            supplier = supplierService.getSupplierById(supplierId);
        } catch (SQLException e) {
            request.setAttribute("message", "Can't find supplier with ID " + supplierId);
        }
        request.setAttribute("supplier", supplier);
        request.getRequestDispatcher("./editSupplier.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SupplierService supplierService = new SupplierService();
        String supplierId = request.getParameter("supplierId");
        StringBuilder message = new StringBuilder();
        Supplier supplier = null;
        int page = 1;
        int pageSize = 5;
        int totalSuppliers =0;
        String name = request.getParameter("name").trim();
        if (!isValidName(name)) {
            request.setAttribute("errorName", "Invalid name");
        }
        name = capitalizeName(name);
        String address = request.getParameter("address").trim();
        address = capitalizeName(address);
        String phone = request.getParameter("phone").trim();
        String email = request.getParameter("email").trim();
        String statusParam = request.getParameter("status");
        boolean status = "1".equals(statusParam);
        if (!isValidEmail(email)) {
            request.setAttribute("errorEmail", "Invalid email");
        }

        if (!isValidPhone(phone)) {
            request.setAttribute("errorPhone", "Invalid phone number. The telephone number have 10 digits");
        }
        try {
            if(supplierService.isSupplierExisted(supplierId, email, phone)){
                message.append("Supplier already existed.\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            supplier = supplierService.getSupplierById(supplierId);
        } catch (SQLException e) {
            request.setAttribute("message", "Can't find supplier with ID " + supplierId);
        }
        if(supplierId == null || !message.isEmpty() || !isValidName(name) || !isValidEmail(email) || !isValidPhone(phone)){
            request.setAttribute("message", message.toString());
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.getRequestDispatcher("./addSupplier.jsp").forward(request, response);
        }else{
            Supplier editSupplier = new Supplier(supplierId, name, address, email, phone, status);
            boolean success = false;
            try {
                success = supplierService.updateSupplier(editSupplier);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("message", "Cannot update supplier due to a error.");
            }
            if (success) {
                request.setAttribute("messageSuccess", "Supplier updated successfully");
            } else {
                request.setAttribute("message", "Failed to update supplier");
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

}
