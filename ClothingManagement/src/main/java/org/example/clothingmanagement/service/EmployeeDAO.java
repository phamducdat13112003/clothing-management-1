package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.repository.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public boolean createEmployee(Employee employee) {
        String sql = "INSERT INTO Employee (EmployeeName, Email, Phone, Address, Gender, DateOfBirth, Status, AccountID, WarehouseID, EmployeeImage) " +
                "VALUES (?, ?, ?, ?, ?, ?, 1, ?, ?,?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeName());
            pt.setString(2, employee.getEmail());
            pt.setString(3, employee.getPhone());
            pt.setString(4, employee.getAddress());
            pt.setInt(5, employee.getGender());
            pt.setDate(6, Date.valueOf(employee.getDateOfBirth()));
            pt.setInt(7, employee.getAccountID());
            pt.setInt(8, employee.getWarehouseID());
            pt.setString(9, employee.getImage());
            int rowsAffected = pt.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmployeeExisted(int employeeId ,String email, String phone) {
        boolean exists = false;
        String sql = "SELECT * FROM Employee WHERE (Email = ? OR Phone = ?) AND employeeId != ? AND status = 1";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, email);
            pt.setString(2, phone);
            pt.setInt(3, employeeId);
            ResultSet rs = pt.executeQuery();
            if(rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean isEmployeeExistedWhenAdd(String email, String phone) {
        boolean exists = false;
        String sql = "SELECT * FROM Employee WHERE (Email = ? OR Phone = ?)  AND status = 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, email);
            pt.setString(2, phone);
            ResultSet rs = pt.executeQuery();
            if(rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }



    public boolean updatePassword(int accountID, String newPassword) {
        String sql = "UPDATE Account SET Password = ? WHERE AccountID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, newPassword);
            pt.setInt(2, accountID);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAccountIdExist(int accountId) {
        String sql = "SELECT COUNT(accountId) FROM employee WHERE accountId = ? AND status = 1 ";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, accountId);
            ResultSet rs = pt.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                return count > 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update employee information
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employee SET EmployeeName = ?, Email = ?, Phone = ?, Address = ?, Gender = ?, DateOfBirth = ?, AccountID = ?, WarehouseID = ?, EmployeeImage = ? WHERE EmployeeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeName());
            pt.setString(2, employee.getEmail());
            pt.setString(3, employee.getPhone());
            pt.setString(4, employee.getAddress());
            pt.setInt(5, employee.getGender());
            pt.setDate(6, Date.valueOf(employee.getDateOfBirth()));
            pt.setInt(7, employee.getAccountID());
            pt.setInt(8, employee.getWarehouseID());
            pt.setString(9, employee.getImage());
            pt.setInt(10, employee.getEmployeeID());
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Nếu có lỗi, trả về false
        }
    }



    public List<Employee> getAllEmployee() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection conn = DBContext.getConnection();
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
                employee.setImage(rs.getString("EmployeeImage"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Employee getEmployeeByID(int employeeID) {
        Employee employee = null;
        String sql = "SELECT * FROM Employee WHERE EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)){
            pt.setInt(1, employeeID);
            try (ResultSet rs = pt.executeQuery()) {
                while (rs.next()) {
                    employee = new Employee();
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
                    employee.setImage(rs.getString("EmployeeImage"));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    public boolean deleteEmployee(int employeeID) {
        String sql = "UPDATE Employee SET Status = 0 WHERE EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, employeeID);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getEmployeeId(){
        String sql ="SELECT MAX(EmployeeID) FROM Employee";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement pt = conn.prepareStatement(sql)) {
            ResultSet rs = pt.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static void main(String[] args) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.getEmployeeByID(1);
        List<Employee> list= employeeDAO.getAllEmployee();
        for (Employee e : list) {
            System.out.println(e);
        }
    }

}
