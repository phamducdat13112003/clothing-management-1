//package org.example.clothingmanagement.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.MultipartConfig;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import org.example.clothingmanagement.validator.ValidateEmployee;
//import org.example.clothingmanagement.entity.Employee;
//import org.example.clothingmanagement.repository.RoleDAO;
//import org.example.clothingmanagement.repository.WarehouseDAO;
//import org.example.clothingmanagement.repository.EmployeeDAO;
//import org.example.clothingmanagement.repository.AccountDAO;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//
//@MultipartConfig
//@WebServlet(name = "ViewProfileServlet", value = "/employee")
//public class EmployeeProfileServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        if (action == null || action.isEmpty()) {
//            action = "view";
//        }
//
//        switch (action) {
//            case "view":
//                try {
//                    viewEmployee(request, response);
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//                break;
//            default:
//                response.getWriter().write("Invalid action1.");
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//
//        if ("update".equals(action)) {
//            updateEmployee(request, response);
//        } else {
//            response.getWriter().write("Invalid action2.");
//        }
//    }
//
//    private void viewEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
//        HttpSession session = request.getSession();
//        String employeeID = (String) session.getAttribute("employeeId");
//        System.out.println("employeeID: " + employeeID);
//        String warehouseID = (String) session.getAttribute("warehouseId");
//        System.out.println("warehouse id received: " + warehouseID);
//
//
//        EmployeeDAO employeeDAO = new EmployeeDAO();
//        List<Employee> employees = employeeDAO.getAllEmployees();
//
//
//        Employee employee = null;
//        // Tìm nhân viên với ID đã cho
//        for (Employee emp : employees) {
//            if (emp.getEmployeeID().equals(employeeID)) {
//                employee = emp;
//                break;
//            }
//        }
//
//
//        if (employee != null) {
//            String roleName = null;
//            roleName = employeeDAO.getRoleNameByEmployeeID(employeeID);
//            String warehouseName = null;
//            try {
//
//                warehouseName = WarehouseDAO.getWarehouseNameById(warehouseID);
//                System.out.println("Warehouse name retrieved: " + warehouseName);
//            } catch (Exception e) {
//                System.out.println("Error retrieving warehouse name: " + e.getMessage());
//                e.printStackTrace();
//            }
//
//
//
//            //Luu rolename vao session
//            request.getSession().setAttribute("employee", employee);
//            request.getSession().setAttribute("warehouseName", warehouseName);
//            request.getSession().setAttribute("roleName", roleName);
//
//            request.setAttribute("employee", employee);
//            request.setAttribute("warehouseName", warehouseName);
//            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
//        } else {
//            response.getWriter().write("Employee not found.");
//        }
//    }
//
//
//    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        HttpSession session = request.getSession();
//        String accountID = (String) session.getAttribute("account_id");
//
//        // If accountID is null, return error
//        if (accountID == null || accountID.equals("null")) {
//            response.getWriter().write("Lỗi: Không tìm thấy AccountID trong phiên làm việc.");
//            return;
//        }
//
//        // Retrieve roleName and warehouseName from session
//        String roleName = (String) session.getAttribute("roleName");
//        String warehouseName = (String) session.getAttribute("warehouseName");
//
//        // Get other employee details from request
//        String employeeID = request.getParameter("employeeID");
//        System.out.println("Employee ID: " + employeeID);
//
//        String employeeName = request.getParameter("employeeName");
//        String phone = request.getParameter("phone");
//        String address = request.getParameter("address");
//        String email = request.getParameter("email");
//        String gender = request.getParameter("gender");
//        String dateOfBirth = request.getParameter("dateOfBirth");
//        String status = request.getParameter("status");
//        String image = request.getParameter("image");
//
//
//
//
//        int warehouseID = -1; // Default invalid value
//        String warehouseIdStr = request.getParameter("warehouseID");
//        if (warehouseIdStr != null && !warehouseIdStr.trim().isEmpty()) {
//            try {
//                warehouseID = Integer.parseInt(warehouseIdStr);
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid warehouseID: " + warehouseIdStr);
//                response.getWriter().write("Error: Invalid warehouseID.");
//                return;
//            }
//        }
//
//        int roleID = -1; // Default invalid value
//        String roleIdStr = request.getParameter("roleID");
//        if (roleIdStr != null && !roleIdStr.trim().isEmpty()) {
//            try {
//                roleID = Integer.parseInt(roleIdStr);
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid roleID: " + roleIdStr);
//                response.getWriter().write("Error: Invalid roleID.");
//                return;
//            }
//        }
//
//        // Validate roleID and warehouseID before proceeding
//        if (roleID == -1 || warehouseID == -1) {
//            response.getWriter().write("Error: RoleID or WarehouseID is missing or invalid.");
//            return;
//        }
//
//
//        String employeeNameError = null;
//        String phoneError = null;
//        String addressError = null;
//        String dobError = null;
//
//        if (!ValidateEmployee.isValidFullName(employeeName)) {
//            employeeNameError = "Tên không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt hoặc khoảng trắng liên tục.";
//        }
//        if (!ValidateEmployee.isValidPhone(phone)) {
//            phoneError = "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại hợp lệ.";
//        }
//        if (!ValidateEmployee.isValidAddress(address)) {
//            addressError = "Địa chỉ không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt ngoài , . - /.";
//        }
//        if (!ValidateEmployee.isAdult(dateOfBirth)) {
//            dobError = "Người dùng phải trên 18 tuổi.";
//        }
//
//        // If validation errors, send back to JSP
//        if (employeeNameError != null || phoneError != null || addressError != null || dobError != null) {
//            request.setAttribute("employeeNameError", employeeNameError);
//            request.setAttribute("phoneError", phoneError);
//            request.setAttribute("addressError", addressError);
//            request.setAttribute("dobError", dobError);
//
//            // Prepare employee object
//            Employee employee = new Employee();
//            employee.setEmployeeID(employeeID);
//            employee.setEmployeeName(employeeName);
//            employee.setPhone(phone);
//            employee.setAddress(address);
//            employee.setEmail(email);
//            //employee.setGender(gender);
//            employee.setDob(LocalDate.parse(dateOfBirth));
//            employee.setStatus(status);
//            //employee.setWarehouseID(warehouseID);
//            employee.setImage(image);
//            //employee.setRoleId(roleID);
//
//            // Store values in session
//            session.setAttribute("roleName", roleName);
//            session.setAttribute("warehouseName", warehouseName);
//
//            request.setAttribute("employee", employee);
//            request.setAttribute("warehouseName", warehouseName);
//            request.setAttribute("roleName", roleName);
//            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
//            return;
//        }
//
//        // Create Employee Object for Update
//        Employee employee = new Employee();
//        employee.setEmployeeID(employeeID);
//        employee.setEmployeeName(employeeName);
//        employee.setPhone(phone);
//        employee.setAddress(address);
//        employee.setEmail(email);
//        //employee.setGender(gender);
//        employee.setDob(LocalDate.parse(dateOfBirth));
//        employee.setStatus(status);
//        //employee.setWarehouseID(warehouseID);
//        employee.setImage(image);
//        //employee.setRoleId(roleID);
//
//        System.out.println("Updating employee: " + employee.toString());
//
//        // Update the employee in the database
//        boolean isUpdated = EmployeeDAO.updateEmployee(employee);
//
//        // Check if the update was successful
//        if (isUpdated) {
//            request.getSession().setAttribute("originalName", employeeName);
//            request.getSession().setAttribute("successMessage", "Cập nhật thành công!");
//            response.sendRedirect("employee?action=view&employeeID=" + employeeID);
//        } else {
//            response.getWriter().write("Cập nhật thông tin nhân viên thất bại.");
//        }
//    }
//
//
//
//}