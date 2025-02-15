package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.clothingmanagement.validator.ValidateEmployee;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.repository.RoleDAO;
import org.example.clothingmanagement.repository.WarehouseDAO;
import org.example.clothingmanagement.repository.EmployeeDAO;
import org.example.clothingmanagement.repository.AccountDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ViewProfileServlet", value = "/employee")
public class EmployeeProfileServlet extends HttpServlet {

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
        List<Employee> employees = employeeDAO.getAllEmployee();
        Employee employee = null;

        // Tìm nhân viên với ID đã cho
        for (Employee emp : employees) {
            if (emp.getEmployeeID() == employeeID) {
                employee = emp;
                break;
            }
        }

        if (employee != null) {
            WarehouseDAO wareHouseDAO = new WarehouseDAO();
            String warehouseName = null;
            try {
                warehouseName = WarehouseDAO.getWarehouseNameById(employee.getWarehouseID());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            int accountId = employee.getAccountID();

            // Lấy RoleID sử dụng AccountID từ AccountDAO
            int roleId = AccountDAO.getRoleIdByAccountId(accountId);

            // Lấy RoleName sử dụng RoleID từ RoleDAO
            String roleName = RoleDAO.getRoleNameById(roleId);

            //Luu rolename vao session
            request.getSession().setAttribute("employee", employee);
            request.getSession().setAttribute("roleName", roleName);
            request.getSession().setAttribute("warehouseName", warehouseName);

            request.setAttribute("employee", employee);
            request.setAttribute("warehouseName", warehouseName);
            request.setAttribute("roleName", roleName);
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
        } else {
            response.getWriter().write("Employee not found.");
        }
    }


    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Get accountID from session
        HttpSession session = request.getSession();
        Integer accountID = (Integer) session.getAttribute("account_id");

        // If accountID is not found in the session, handle the error
        if (accountID == null) {
            // If accountID is not in session, show error and return
            response.getWriter().write("Lỗi: Không tìm thấy AccountID trong phiên làm việc.");
            return;
        }

        // Retrieve the roleName from the session
        String roleName = (String) session.getAttribute("roleName");
        String warehouseName = (String) session.getAttribute("warehouseName");


        // Get other employee details from request
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

        // Check if accountID is valid
        if (accountID == -1) {
            System.out.println("Lỗi: Không tìm thấy AccountID cho EmployeeID " + employeeID);
            response.getWriter().write("Lỗi: Không tìm thấy AccountID.");
            return;
        }

        // Check for input validation errors
        String employeeNameError = null;
        String phoneError = null;
        String addressError = null;
        String dobError = null;

        if (!ValidateEmployee.isValidFullName(employeeName)) {
            employeeNameError = "Tên không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt hoặc khoảng trắng liên tục.";
        }
        if (!ValidateEmployee.isValidPhone(phone)) {
            phoneError = "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại hợp lệ.";
        }
        if (!ValidateEmployee.isValidAddress(address)) {
            addressError = "Địa chỉ không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt ngoài , . - /.";
        }

        if(!ValidateEmployee.isAdult(dateOfBirth)){
            dobError = "Người dùng phải trên 18 tuổi.";
        }

        // If there are validation errors, forward data back to JSP
        if (employeeNameError != null || phoneError != null || addressError != null || dobError != null) {
            request.setAttribute("employeeNameError", employeeNameError);
            request.setAttribute("phoneError", phoneError);
            request.setAttribute("addressError", addressError);
            request.setAttribute("dobError", dobError);

            // Prepare employee data for display in JSP
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
            employee.setAccountID(accountID);

            // Lưu lại roleName và warehouseName vào session
            if (roleName != null && !roleName.isEmpty()) {
                session.setAttribute("roleName", roleName); // Đảm bảo roleName không null và không rỗng
            }
            if (warehouseName != null && !warehouseName.isEmpty()) {
                session.setAttribute("warehouseName", warehouseName); // Đảm bảo warehouseName không null và không rỗng
            }

            // Send employee data and other info to the JSP
            request.setAttribute("employee", employee);
            request.setAttribute("warehouseName", warehouseName);
            request.setAttribute("roleName", roleName);
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Create an Employee object with the validated data
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
        employee.setAccountID(accountID);

        // Update the employee in the database
        boolean isUpdated = EmployeeDAO.updateEmployee(employee);

        // Check if the update was successful
        if (isUpdated) {
            request.getSession().setAttribute("originalName", employeeName);
            String successMessage = "Cập nhật thành công!";
            request.getSession().setAttribute("successMessage", successMessage);
            response.sendRedirect("employee?action=view&id=" + employeeID);
        } else {
            response.getWriter().write("Cập nhật thông tin nhân viên thất bại.");
        }
    }


}