package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.entity.Warehouse;
import org.example.clothingmanagement.service.EmployeeService;
import org.example.clothingmanagement.repository.WarehouseDAO;
import org.example.clothingmanagement.service.RoleService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 2 * 1024 * 1024,   // 2MB
        maxRequestSize = 4 * 1024 * 1024 // 4MB
)
@WebServlet(name = "AddEmployeeServlet", value = "/addemployee")
public class AddEmployeeServlet extends HttpServlet {

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
        WarehouseDAO wareHouseDAO = new WarehouseDAO();
        List<Warehouse> listWarehouse = null;
        listWarehouse = wareHouseDAO.getAllWareHouse();
        request.setAttribute("listWarehouse", listWarehouse);
        request.getRequestDispatcher("./addEmployee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        EmployeeService employeeService = new EmployeeService();
        RoleService roleService = new RoleService();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        StringBuilder message = new StringBuilder();
        List<Role> list =null;
        List<Warehouse> listWarehouse = null;
        int page = 1;
        int pageSize = 5;
        int totalEmployees = 0;
        try {
            list = roleService.getAllRoles();
            listWarehouse = warehouseDAO.getAllWareHouse();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String employeeId = null;
        try {
            employeeId = generateNewEmployeeId();
        } catch (SQLException e) {
            request.setAttribute("message", "Cannot create employeeID.");
        }
        String name = request.getParameter("name").trim();
        if (!isValidName(name)) {
            request.setAttribute("errorName", "Name must contain only letters.");
        }
        name = capitalizeName(name);
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        address = capitalizeName(address);
        String genderParam = request.getParameter("gender");
        boolean gender = "1".equals(genderParam);
        LocalDate dateOfBirth = LocalDate.parse(request.getParameter("dob"));
        // Kiểm tra lỗi cho từng trường
        if (!isValidEmail(email)) {
            request.setAttribute("errorEmail", "Invalid email");
        }

        if (!isValidPhone(phone)) {
            request.setAttribute("errorPhone", "Invalid phone number. The telephone number have 10 digits");
        }

        if (!isAdult(String.valueOf(dateOfBirth))) {
            request.setAttribute("errorDateofBirth", "Invalid date of birth, employee must equal or greater than 18 age");
        }
        try {
            if (employeeService.isEmployeeExistedWhenAdd(email, phone)) {
                message.append("Employee already existed.\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(employeeId == null || !message.isEmpty() || !isValidName(name) || !isValidEmail(email) || !isValidPhone(phone) || !isAdult(String.valueOf(dateOfBirth))) {
            request.setAttribute("message", message.toString());
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.setAttribute("dateOfBirth", dateOfBirth);
            request.setAttribute("list", list);
            request.setAttribute("listWarehouse", listWarehouse);
            request.getRequestDispatcher("./addEmployee.jsp").forward(request, response);
        }else{
            Employee employee = new Employee(employeeId ,name, email, phone, address, gender, dateOfBirth, "W001","");
            Part part = request.getPart("img");
            if (part != null && part.getSize() > 0) {
                String contentType = part.getContentType();
                long fileSize = part.getSize();
                if (!isImageFile(contentType)) {
                    request.setAttribute("message", "Only image files (JPG, PNG, GIF) are allowed.");
                } else if (fileSize > 2 * 1024 * 1024) {
                    request.setAttribute("message", "Image size must not exceed 2MB.");
                } else {
                    String realPath = request.getServletContext().getRealPath("img/Employee");
                    String source = Path.of(part.getSubmittedFileName()).getFileName().toString();
                    if (!source.isEmpty()) {
                        String filename = null;
                        try {
                            filename = employeeService.getEmployeeId() + ".png";
                        } catch (SQLException e) {
                            request.setAttribute("message", "Can't create filename.");
                        }
                        if (!Files.exists(Path.of(realPath))) {
                            Files.createDirectories(Path.of(realPath));
                        }
                        part.write(realPath + "/" + filename);
                        employee.setImage("img/Employee/" + filename);
                    }
                }
            }
            boolean success = false;
            try {
                success = employeeService.createEmployee(employee);
            } catch (SQLException e) {
                request.setAttribute("message", "Can't add employee.");
            }
            if (success) {
                request.setAttribute("messageSuccess", "Employee added successfully");
            } else {
                request.setAttribute("message", "Failed to add employee");
            }
            List<Employee> listEmployee = null;
            try {
                listEmployee = employeeService.getEmployeesWithPagination(page, pageSize);
                totalEmployees = employeeService.getTotalEmployeeCount();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("list", listEmployee);
            request.getRequestDispatcher("./manageEmployee.jsp").forward(request, response);
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

    private boolean isAdult(String dob) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = sdf.parse(dob);

            Calendar today = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(dateOfBirth);

            int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                    (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
                age--; // Chưa đến sinh nhật trong năm nay
            }
            return age >= 18;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    private boolean isImageFile(String contentType) {
        String[] validImageTypes = {"image/jpeg", "image/png", "image/gif"};
        for (String validType : validImageTypes) {
            if (validType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\sàáạảãâấầẩẫậăắằẳẵặèéẹẻẽêếềểễệìíịỉĩòóọỏõôốồổỗộơớờởỡợùúụủũưứừửữựýỳỵỷỹđĐ]+$");
    }

    public String generateNewEmployeeId() throws SQLException {
        EmployeeService employeeService = new EmployeeService();
        String prefix = "EP00"; // Luôn có "EP00"
        int maxId = employeeService.getMaxEmployeeId(); // Lấy số lớn nhất từ database
        return prefix + (maxId + 1); // Tăng lên 1 và ghép vào
    }



}
