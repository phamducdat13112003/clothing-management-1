package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class ViewProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "view";
        }

        switch (action) {
            case "view":
                //viewEmployee(request, response);
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

//    private void viewEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int employeeID = Integer.parseInt(request.getParameter("id"));
//        EmployeeDAO employeeDAO = new EmployeeDAO();
//        // Lấy tất cả nhân viên
//        List<Employee> employees = employeeDAO.getAllEmployee();
//        Employee employee = null;
//
//        // Tìm nhân viên với ID đã cho
//        for (Employee emp : employees) {
//            if (emp.getEmployeeID() == employeeID) {
//                employee = emp;
//                break;
//            }
//        }
//
//        if (employee != null) {
//            WarehouseDAO wareHouseDAO = new WarehouseDAO();
//            String warehouseName = null;
//            try {
//                warehouseName = WarehouseDAO.getWarehouseNameById(employee.getWarehouseID());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            //int accountId = employee.getAccountID();
//
//            // Lấy RoleID sử dụng AccountID từ AccountDAO
//            //int roleId = AccountDAO.getRoleIdByAccountId(accountId);
//
//            // Lấy RoleName sử dụng RoleID từ RoleDAO
//            //String roleName = RoleDAO.getRoleNameById(roleId);
//
//
//            request.setAttribute("employee", employee);
//            request.setAttribute("warehouseName", warehouseName);
//            //request.setAttribute("roleName", roleName);
//            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
//        } else {
//            response.getWriter().write("Employee not found.");
//        }
//    }


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

        // Lấy AccountID từ cơ sở dữ liệu để tránh lỗi NULL
        int accountID = EmployeeDAO.getAccountIdByEmployeeId(employeeID);
        if (accountID == -1) {
            System.out.println("Lỗi: Không tìm thấy AccountID cho EmployeeID " + employeeID);
            response.getWriter().write("Lỗi: Không tìm thấy AccountID.");
            return;
        }

        // Kiểm tra lỗi dữ liệu nhập
        String employeeNameError = null;
        String phoneError = null;
        String addressError = null;

        if (!ValidateEmployee.isValidFullName(employeeName)) {
            employeeNameError = "Tên không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt hoặc khoảng trắng liên tục.";
        }
        if (!ValidateEmployee.isValidPhone(phone)) {
            phoneError = "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại hợp lệ.";
        }
        if (!ValidateEmployee.isValidAddress(address)) {
            addressError = "Địa chỉ không hợp lệ. Vui lòng không sử dụng ký tự đặc biệt ngoài , . - /.";
        }

        // Nếu có lỗi, chuyển dữ liệu về JSP để hiển thị lỗi
        if (employeeNameError != null || phoneError != null || addressError != null) {
            request.setAttribute("employeeNameError", employeeNameError);
            request.setAttribute("phoneError", phoneError);
            request.setAttribute("addressError", addressError);


            String warehouseName = null;
            String roleName = null;
            try {
                warehouseName = WarehouseDAO.getWarehouseNameById(warehouseID);
                int roleId = AccountDAO.getRoleIdByAccountId(accountID);
                roleName = RoleDAO.getRoleNameById(roleId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // dữ liệu nhân viên để hiển thị lại
            Employee employee = new Employee();
            //employee.setEmployeeID(employeeID);
            employee.setEmployeeName(employeeName);
            employee.setPhone(phone);
            employee.setAddress(address);
            employee.setEmail(email);
            employee.setGender(gender);
            employee.setDateOfBirth(LocalDate.parse(dateOfBirth));
            employee.setStatus(status);
            employee.setWarehouseID(warehouseID);
            employee.setImage(image);
            //employee.setAccountID(accountID);

            // Gửi dữ liệu về trang JSP
            request.setAttribute("employee", employee);
            request.setAttribute("warehouseName", warehouseName);
            request.setAttribute("roleName", roleName);
            request.getRequestDispatcher("profile-info.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng Employee với dữ liệu đã kiểm tra
        Employee employee = new Employee();
        //employee.setEmployeeID(employeeID);
        employee.setEmployeeName(employeeName);
        employee.setPhone(phone);
        employee.setAddress(address);
        employee.setEmail(email);
        employee.setGender(gender);
        employee.setDateOfBirth(LocalDate.parse(dateOfBirth));
        employee.setStatus(status);
        employee.setWarehouseID(warehouseID);
        employee.setImage(image);
        //employee.setAccountID(accountID);

        boolean isUpdated = EmployeeDAO.updateEmployee(employee);

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