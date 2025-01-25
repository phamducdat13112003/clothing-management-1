package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Account;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/warehousemanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Create a new employee with account
    public static boolean insertEmployeeWithAccount(Employee employee, Account account) {
        String sqlAccount = "INSERT INTO Account (Email, Password, RoleID) VALUES (?, ?, ?)";
        String sqlEmployee = "INSERT INTO Employee (EmployeeName, Email, Phone, Address, Gender, DateOfBirth, Status, AccountID, WarehouseID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false); // Begin transaction

            // Insert into Account
            try (PreparedStatement ptAccount = conn.prepareStatement(sqlAccount, Statement.RETURN_GENERATED_KEYS)) {
                ptAccount.setString(1, account.getEmail());
                ptAccount.setString(2, account.getPassword());
                ptAccount.setInt(3, account.getRoleID());
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
                ptEmployee.setInt(5, employee.getGender());
                ptEmployee.setDate(6, Date.valueOf(employee.getDateOfBirth()));
                ptEmployee.setInt(7, employee.getStatus());
                ptEmployee.setInt(8, employee.getAccountID());
                ptEmployee.setInt(9, employee.getWarehouseID());
                ptEmployee.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update account password
    public static boolean updatePassword(int accountID, String newPassword) {
        String sql = "UPDATE Account SET Password = ? WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, newPassword);
            pt.setInt(2, accountID);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update employee information
    public static boolean updateEmployee(Employee employee) {
        String sql = "UPDATE Employee SET EmployeeName = ?, Email = ?, Phone = ?, Address = ?, Gender = ?, DateOfBirth = ?, Status = ?, WarehouseID = ? " +
                "WHERE EmployeeID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeName());
            pt.setString(2, employee.getEmail());
            pt.setString(3, employee.getPhone());
            pt.setString(4, employee.getAddress());
            pt.setInt(5, employee.getGender());
            pt.setDate(6, Date.valueOf(employee.getDateOfBirth()));
            pt.setInt(7, employee.getStatus());
            pt.setInt(8, employee.getWarehouseID());
            pt.setInt(9, employee.getEmployeeID());
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
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pt = conn.prepareStatement(sql);
             ResultSet rs = pt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("EmployeeID"));
                employee.setEmployeeName(rs.getString("EmployeeName"));
                employee.setEmail(rs.getString("Email"));
                employee.setPhone(rs.getString("Phone"));
                employee.setAddress(rs.getString("Address"));
                employee.setGender(rs.getInt("Gender"));
                employee.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
                employee.setStatus(rs.getInt("Status"));
                employee.setAccountID(rs.getInt("AccountID"));
                employee.setWarehouseID(rs.getInt("WarehouseID"));
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
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, employeeID);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Main method to test Insert with Account
    public static void main(String[] args) {
        // Delete an employee by ID
        int employeeIDToDelete = 1; // Replace with the actual EmployeeID you want to delete
        boolean employeeDeleted = deleteEmployee(employeeIDToDelete);
        System.out.println("Employee with ID " + employeeIDToDelete + " deleted: " + employeeDeleted);

        // Display remaining employees
        List<Employee> employees = selectAll();
        System.out.println("Remaining employees:");
        for (Employee emp : employees) {
            System.out.println(emp);
        }
    }

}
