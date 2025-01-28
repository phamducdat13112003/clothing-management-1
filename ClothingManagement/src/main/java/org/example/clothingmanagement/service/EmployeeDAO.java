package org.example.clothingmanagement.service;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Employee;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final DataSource dataSource = createDataSource();

    private static DataSource createDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/warehousemanagement");
        ds.setUser("root");
        ds.setPassword("");
        return ds;
    }

    // Create a new employee with account
    public static boolean insertEmployeeWithAccount(Employee employee, Account account) {
        String sqlAccount = "INSERT INTO Account (Email, Password, RoleID) VALUES (?, ?, ?)";
        String sqlEmployee = "INSERT INTO Employee (EmployeeName, Email, Phone, Address, Gender, DateOfBirth, Status, AccountID, WarehouseID, Image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            // Insert into Account
            try (PreparedStatement ptAccount = conn.prepareStatement(sqlAccount, Statement.RETURN_GENERATED_KEYS)) {
                ptAccount.setString(1, account.getEmail());
                ptAccount.setString(2, account.getPassword());
                ptAccount.setInt(3, account.getRoleId());
                ptAccount.executeUpdate();

                // Get generated AccountID
                try (ResultSet rs = ptAccount.getGeneratedKeys()) {
                    if (rs.next()) {
                        int accountID = rs.getInt(1);
                        employee.setAccountID(accountID); // Set AccountID for Employee
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Insert into Employee
            try (PreparedStatement ptEmployee = conn.prepareStatement(sqlEmployee)) {
                ptEmployee.setString(1, employee.getEmployeeName());
                ptEmployee.setString(2, employee.getEmail());
                ptEmployee.setString(3, employee.getPhone());
                ptEmployee.setString(4, employee.getAddress());
                ptEmployee.setString(5, employee.getGender());
                ptEmployee.setDate(6, Date.valueOf(employee.getDateOfBirth()));
                ptEmployee.setString(7, employee.getStatus());
                ptEmployee.setInt(8, employee.getAccountID());
                ptEmployee.setInt(9, employee.getWarehouseID());
                ptEmployee.setString(10, employee.getImage());
                ptEmployee.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update employee information
    public static boolean updateEmployee(Employee employee) {
        String sql = "UPDATE Employee SET EmployeeName = ?, Email = ?, Phone = ?, Address = ?, Gender = ?, DateOfBirth = ?, Status = ?, WarehouseID = ?, Image = ? " +
                "WHERE EmployeeID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeName());
            pt.setString(2, employee.getEmail());
            pt.setString(3, employee.getPhone());
            pt.setString(4, employee.getAddress());
            pt.setString(5, employee.getGender());
            pt.setDate(6, Date.valueOf(employee.getDateOfBirth()));
            pt.setString(7, employee.getStatus());
            pt.setInt(8, employee.getWarehouseID());
            pt.setString(9, employee.getImage());
            pt.setInt(10, employee.getEmployeeID());
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read all employees
    public static List<Employee> selectAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql);
             ResultSet rs = pt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("EmployeeID"));
                employee.setEmployeeName(rs.getString("EmployeeName"));
                employee.setEmail(rs.getString("Email"));
                employee.setPhone(rs.getString("Phone"));
                employee.setAddress(rs.getString("Address"));
                employee.setGender(rs.getString("Gender"));
                employee.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
                employee.setStatus(rs.getString("Status"));
                employee.setAccountID(rs.getInt("AccountID"));
                employee.setWarehouseID(rs.getInt("WarehouseID"));
                employee.setImage(rs.getString("Image"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // Delete employee by ID
    public static boolean deleteEmployee(int employeeID) {
        String sql = "DELETE FROM Employee WHERE EmployeeID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, employeeID);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getAccountIdByEmployeeId(int employeeID) {
        String sql = "SELECT AccountID FROM Employee WHERE EmployeeID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setInt(1, employeeID);

            try (ResultSet resultSet = pt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("AccountID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no account is found or an error occurs
    }

    public static String getEmployeeNameByEmployeeId(int employeeID) {
        String sql = "SELECT EmployeeName FROM Employee WHERE EmployeeID = ?";
        try (Connection conn = dataSource.getConnection(); // Sử dụng DataSource để kết nối
             PreparedStatement pt = conn.prepareStatement(sql)) {

            // Thiết lập giá trị cho tham số trong SQL
            pt.setInt(1, employeeID);

            // Thực thi truy vấn và xử lý kết quả
            try (ResultSet resultSet = pt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("EmployeeName"); // Lấy giá trị EmployeeName từ kết quả
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log lỗi nếu có vấn đề xảy ra với truy vấn SQL
        }
        return null; // Trả về null nếu không tìm thấy hoặc có lỗi
    }





    // Main method to test SelectAll
    public static void main(String[] args) {
        // Create an Employee object with updated information
        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeID(1); // Replace with the actual EmployeeID you want to update
        updatedEmployee.setEmployeeName("John Doe Updated");
        updatedEmployee.setEmail("johndoe@example.com"); // Ensure this matches the email in your database
        updatedEmployee.setPhone("9876543210");
        updatedEmployee.setAddress("123 Updated Address");
        updatedEmployee.setGender("Male"); // Ensure the value is valid
        updatedEmployee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        updatedEmployee.setStatus("Active"); // Ensure the value is valid
        updatedEmployee.setWarehouseID(1); // Ensure this matches a valid WarehouseID in your database
        updatedEmployee.setImage("path/to/updated-profile.jpg"); // Replace with the actual image path

        // Call the update method and print the result
        boolean isUpdated = EmployeeDAO.updateEmployee(updatedEmployee);

        if (isUpdated) {
            System.out.println("Employee information updated successfully.");
        } else {
            System.out.println("Failed to update employee information.");
        }
    }



}
