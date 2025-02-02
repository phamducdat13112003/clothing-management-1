package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.validator.ValidateEmployee;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.service.AccountDAO;
import org.example.clothingmanagement.service.EmployeeDAO;
import org.example.clothingmanagement.service.RoleDAO;
import org.example.clothingmanagement.service.WarehouseDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "EmployeeServlet", value = "/employee")
public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "view";
        }

        switch (action) {
            case "view":
                viewEmployee(request, response);
                break;
            default:
                response.getWriter().write("Invalid action.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("update".equals(action)) {
            updateEmployee(request, response);
        } else {
            response.getWriter().write("Invalid action.");
        }
    }

    private void viewEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeID = Integer.parseInt(request.getParameter("id"));
        EmployeeDAO employeeDAO = new EmployeeDAO();
        // Retrieve all employees
        List<Employee> employees = employeeDAO.getAllEmployee();
        Employee employee = null;

        // Search for the employee with the given ID
        for (Employee emp : employees) {
            if (emp.getEmployeeID() == employeeID) {
                employee = emp;
                break;
            }
        }

        if (employee != null) {
            // Retrieve warehouse name
           WarehouseDAO wareHouseDAO = new WarehouseDAO();
            String warehouseName = null;
            try {
                warehouseName = WarehouseDAO.getWarehouseNameById(employee.getWarehouseID());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            int accountId = employee.getAccountID(); // Assuming Employee table has AccountID

            // Fetch RoleID using AccountID from AccountDAO
            int roleId = AccountDAO.getRoleIdByAccountId(accountId);

            // Fetch RoleName using RoleID from RoleDAO
            String roleName = RoleDAO.getRoleNameById(roleId);

            // Set attributes for the JSP
            request.setAttribute("employee", employee);
            request.setAttribute("warehouseName", warehouseName);
            request.setAttribute("roleName", roleName);
            // Forward to the JSP page
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
        } else {
            response.getWriter().write("Employee not found.");
        }
    }


    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int employeeID = Integer.parseInt(request.getParameter("employeeID"));
        String employeeName = request.getParameter("employeeName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dateOfBirth = request.getParameter("dateOfBirth");
        String status = request.getParameter("status");
        int warehouseID = Integer.parseInt(request.getParameter("warehouseID"));
        String image = request.getParameter("image");


        // Biến lỗi cho từng trường
        String employeeNameError = null;
        String phoneError = null;
        String addressError = null;


        // Validation
        if (!ValidateEmployee.isValidFullName(employeeName)) {
            employeeNameError = "Tên không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt hoặc khoảng trắng liên tục.";
        }
        if (!ValidateEmployee.isValidPhone(phone)) {
            phoneError = "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại hợp lệ.";
        }
        if (!ValidateEmployee.isValidAddress(address)) {
            addressError = "Địa chỉ không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt ngoài , . - /.";
        }

        // Nếu có lỗi, gửi lại toàn bộ dữ liệu và lỗi về JSP
        if (employeeNameError != null || phoneError != null || addressError != null) {
            request.setAttribute("employeeNameError", employeeNameError);
            request.setAttribute("phoneError", phoneError);
            request.setAttribute("addressError", addressError);

            // Fetch lại thông tin role và warehouse để hiển thị đúng
            String warehouseName = null;
            String roleName = null;
            try {
                warehouseName = WarehouseDAO.getWarehouseNameById(warehouseID);
                int accountId = EmployeeDAO.getAccountIdByEmployeeId(employeeID);
                int roleId = AccountDAO.getRoleIdByAccountId(accountId);
                roleName = RoleDAO.getRoleNameById(roleId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Chuẩn bị lại thông tin employee để hiển thị
            Employee employee = new Employee();
            employee.setEmployeeID(employeeID);
            employee.setEmployeeName(employeeName);
            employee.setPhone(phone);
            employee.setAddress(address);
            employee.setEmail(email);
            employee.setGender(gender);
            employee.setDateOfBirth(LocalDate.parse(dateOfBirth));
            employee.setStatus(status);
            employee.setWarehouseID(warehouseID);
            employee.setImage(image);

            // Gửi lại thông tin về JSP
            request.setAttribute("employee", employee);
            request.setAttribute("warehouseName", warehouseName);
            request.setAttribute("roleName", roleName);

            // Forward lại về profile-info.jsp
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Nếu không có lỗi, thực hiện cập nhật
        Employee employee = new Employee();
        employee.setEmployeeID(employeeID);
        employee.setEmployeeName(employeeName);
        employee.setPhone(phone);
        employee.setAddress(address);
        employee.setEmail(email);
        employee.setGender(gender);
        employee.setDateOfBirth(LocalDate.parse(dateOfBirth));
        employee.setStatus(status);
        employee.setWarehouseID(warehouseID);
        employee.setImage(image);

        boolean isUpdated = EmployeeDAO.updateEmployee(employee);

        if (isUpdated) {
            // Nếu cập nhật thành công, cập nhật cả tên gốc và chuyển hướng
            request.getSession().setAttribute("originalName", employeeName); // Tên mới khi cập nhật thành công
            String successMessage = "Updated Successfully!";
            request.getSession().setAttribute("successMessage", successMessage);
            response.sendRedirect("employee?action=view&id=" + employeeID);
        } else {
            response.getWriter().write("Failed to update employee information.");
        }
    }






}