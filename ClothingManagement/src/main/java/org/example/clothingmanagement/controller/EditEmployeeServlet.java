package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.entity.Warehouse;
import org.example.clothingmanagement.service.AccountService;
import org.example.clothingmanagement.service.EmployeeService;
import org.example.clothingmanagement.repository.WarehouseDAO;

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
@WebServlet(name = "EditEmployeeServlet", value = "/editemployee")
public class EditEmployeeServlet extends HttpServlet {

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
        String employeeId = request.getParameter("employeeId");
        EmployeeService employeeService = new EmployeeService();
        AccountService accountService = new AccountService();
        WarehouseDAO wareHouseDAO= new WarehouseDAO();
        Employee employee = null;
        try {
            employee = employeeService.getEmployeeByID(employeeId);
        } catch (SQLException e) {
            request.setAttribute("message", "Can't find employee with ID " + employeeId);
        }
        List<Role> list = null;
        List<Warehouse> listWarehouse = null;
        try {
             list = accountService.getAllRoles();
             listWarehouse= wareHouseDAO.getAllWareHouse();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("employee", employee);
        request.setAttribute("list", list);
        request.setAttribute("listWarehouse", listWarehouse);
        request.getRequestDispatcher("./editEmployee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmployeeService employeeService = new EmployeeService();
        AccountService accountService = new AccountService();
        StringBuilder message = new StringBuilder();
        List<Role> list = null;
        Employee employee = null;
        int page = 1;
        int pageSize = 5;
        int totalEmployees = 0;
        String employeeId= request.getParameter("employeeId");
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
        String status = request.getParameter("status");
        if (!isValidEmail(email)) {
            request.setAttribute("errorEmail", "Invalid email");
        }

        if (!isValidPhone(phone)) {
            request.setAttribute("errorPhone", "Invalid phone number");
        }

        if (!isAdult(String.valueOf(dateOfBirth))) {
            request.setAttribute("errorDateofBirth", "Invalid date of birth, employee must equal or greater than 18 age");
        }

        try {
            if (employeeService.isEmployeeExisted(employeeId,email, phone)) {
                message.append("Employee already existed.\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            employee = employeeService.getEmployeeByID(employeeId);
            list = accountService.getAllRoles();
        } catch (SQLException e) {
            request.setAttribute("message", "Can't find employee with ID " + employeeId);
        }
        if(!message.isEmpty() || !isValidName(name) || !isValidEmail(email) || !isValidPhone(phone) || !isAdult(String.valueOf(dateOfBirth))) {
            request.setAttribute("message", message.toString());
            request.setAttribute("employee", employee);
            request.setAttribute("list", list);
            request.getRequestDispatcher("./editEmployee.jsp").forward(request, response);
        }else{
            Employee editEmployee = new Employee( employeeId, name, email , phone, address, gender, dateOfBirth, status, "W001", "");
            Part part = request.getPart("img");
            long fileSize = part.getSize();
                if (part != null && part.getSize() > 0) { // Check if part is not null and has content
                    String contentType = part.getContentType();
                    if (!isImageFile(contentType)) {
                        request.setAttribute("message", "Only image files (JPG, PNG, GIF) are allowed.");
                        request.getRequestDispatcher("editemployee").include(request, response);
                        return;
                    }
                    if (fileSize > 2 * 1024 * 1024) { // Kiểm tra nếu ảnh lớn hơn 2MB
                        request.setAttribute("message", "Image size must not exceed 2MB.");
                        request.getRequestDispatcher("editemployee").include(request, response);
                        return;
                    }
                    String realPath = request.getServletContext().getRealPath("img/Employee"); //where the photo is saved
                    String source = Path.of(part.getSubmittedFileName()).getFileName().toString(); //get the original filename of the file then
                    // convert it to a string, get just the filename without including the full path

                    if (!source.isEmpty()) {
                        String filename = employeeId + ".png";
                        if (!Files.exists(Path.of(realPath))) { // check folder /img/ Employee is existed
                            Files.createDirectories(Path.of(realPath));
                        }
                    part.write(realPath + "/" + filename); //Save the uploaded file to the destination folder with a new filename.
                    editEmployee.setImage("img/Employee/" + filename+ "?" +System.currentTimeMillis()); //Set the path to the image file
                    }
                } else {
                    Employee existEmployee = null;
                        try {
                            existEmployee = employeeService.getEmployeeByID(employeeId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        editEmployee.setImage(existEmployee.getImage());
                }
            boolean success = false;
            boolean emailUpdated = true;
            boolean isUpdateStatusAccount =true;
            try {
                success = employeeService.updateEmployee(editEmployee);

                // Kiểm tra nếu có tài khoản, thì mới cập nhật email
                if (employeeService.hasAccount(employeeId)) {
                    emailUpdated = employeeService.updateAccountEmail(employeeId, email);
                    isUpdateStatusAccount = accountService.updateStatusAccount(status, employeeId);
                    if (!emailUpdated || ! isUpdateStatusAccount) {
                        request.setAttribute("message", "Employee updated, but account update failed.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("message", "Cannot update Employee due to a database error.");
            }

            if (success && emailUpdated && isUpdateStatusAccount) {
                request.setAttribute("messageSuccess", "Employee updated successfully");
            } else {
                request.setAttribute("message", "Failed to update employee");
            }
            List<Employee> listEmployee = null;
            try {
                listEmployee = employeeService.getEmployeesWithPagination(page, pageSize);
                totalEmployees = employeeService.getTotalEmployeeCount();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);
            request.setAttribute("list", listEmployee);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
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
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
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
}
