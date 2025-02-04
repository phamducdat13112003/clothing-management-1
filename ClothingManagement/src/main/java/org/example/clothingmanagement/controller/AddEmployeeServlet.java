package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Warehouse;
import org.example.clothingmanagement.service.AccountDAO;
import org.example.clothingmanagement.service.EmployeeDAO;
import org.example.clothingmanagement.service.WarehouseDAO;

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

@MultipartConfig
@WebServlet(name = "AddEmployeeServlet", value = "/addemployee")
public class AddEmployeeServlet extends HttpServlet {

    private AccountDAO accountDAO;
    private WarehouseDAO warehouseDAO;

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
        AccountDAO accountDAO = new AccountDAO();
        WarehouseDAO wareHouseDAO = new WarehouseDAO();
        List<Account> list = null;
        List<Warehouse> listWarehouse = null;
        try {
            list = accountDAO.getAllAccountAvaiable();
            listWarehouse = wareHouseDAO.getAllWareHouse();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("list", list);
        request.setAttribute("listWarehouse", listWarehouse);
        request.getRequestDispatcher("./addEmployee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        AccountDAO accountDAO = new AccountDAO();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        StringBuilder message = new StringBuilder();

        boolean isAccountExisted= false;
        boolean isEmployeeExisted= false;
        Map<String, String> errorMessages = new HashMap<>();
        List<Account> list =null;
        List<Warehouse> listWarehouse = null;
        try {
            list = accountDAO.getAllAccountAvaiable();
            listWarehouse = warehouseDAO.getAllWareHouse();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String name = request.getParameter("name").trim();
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            errorMessages.put("name", "Invalid name");
        }
        name = capitalizeName(name);
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        address = capitalizeName(address);
        String accountId = request.getParameter("account");
        String gender = request.getParameter("gender");
        LocalDate dateOfBirth = LocalDate.parse(request.getParameter("dob"));
        String warehouseID = request.getParameter("warehouse");

        // Kiểm tra lỗi cho từng trường
        if (!isValidEmail(email)) {
            errorMessages.put("email", "Invalid Email.");
        }

        if (!isValidPhone(phone)) {
            errorMessages.put("phone", "Invalid phone number! Must be 10 digits.");
        }

        if (!isAdult(String.valueOf(dateOfBirth))) {
            errorMessages.put("dob", "Employees must be 18 years of age or older.");
        }

//        if (employeeDAO.isAccountIdExistAdd(Integer.parseInt(accountId))) {
//            message.append("This account is used by another employee.\n");
//            isAccountExisted =true;
//        }

        if (employeeDAO.isEmployeeExistedWhenAdd(email, phone)) {
            message.append("Employee already existed.\n");
            isEmployeeExisted =true;
        }
        if(!errorMessages.isEmpty() || isAccountExisted || isEmployeeExisted) {
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.setAttribute("gender", gender);
            request.setAttribute("dateOfBirth", dateOfBirth);
            request.setAttribute("list", list);
            request.setAttribute("listWarehouse", listWarehouse);
            request.setAttribute("errorMessages", errorMessages);
            request.setAttribute("message", message.toString());
            request.getRequestDispatcher("./addEmployee.jsp").forward(request, response);
        }else{
            Employee employee = new Employee(name, email, phone, address, gender, dateOfBirth, Integer.parseInt(accountId), Integer.parseInt(warehouseID),"");
            Part part = request.getPart("img");
            String contentType = part.getContentType();
            if (!isImageFile(contentType)) {
                request.setAttribute("message", "Only image files (JPG, PNG, GIF) are allowed.");
            }
            String realPath = request.getServletContext().getRealPath("/img/Employee"); //where the photo is saved
            String source = Path.of(part.getSubmittedFileName()).getFileName().toString(); //get the original filename of the file then
            // convert it to a string, get just the filename without including the full path.
            if (!source.isEmpty()) {
                String filename = employeeDAO.getEmployeeId() + ".png";
                if (!Files.exists(Path.of(realPath))) { // check folder /images/Equipment is existed
                    Files.createDirectories(Path.of(realPath));
                }
                part.write(realPath + "/" + filename); //Save the uploaded file to the destination folder with a new filename.
                employee.setImage("/img/Employee/" + filename); //Set the path to the image file
            }
            boolean success = employeeDAO.createEmployee(employee);
                if (success) {
                    request.setAttribute("message", "Employee added successfully");
                } else {
                    request.setAttribute("message", "Failed to add employee");
                }
            List<Employee> listEmployee = employeeDAO.getAllEmployee();
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

}
