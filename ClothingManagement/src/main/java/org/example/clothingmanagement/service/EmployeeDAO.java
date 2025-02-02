package org.example.clothingmanagement.service;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.repository.DBContext;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/warehousemanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final DataSource dataSource = createDataSource();

    private static DataSource createDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/warehousemanagement");
        ds.setUser("root");
        ds.setPassword("");
        return ds;
    }

    public boolean createEmployee(Employee employee) {
        String sql = "INSERT INTO Employee (EmployeeName, Email, Phone, Address, Gender, DateOfBirth, Status, AccountID, WarehouseID, Image) " +
                "VALUES (?, ?, ?, ?, ?, ?, 1, ?, ?,?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeName());
            pt.setString(2, employee.getEmail());
            pt.setString(3, employee.getPhone());
            pt.setString(4, employee.getAddress());
            pt.setString(5, employee.getGender());
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
    public static boolean updateEmployee(Employee employee) {
        System.out.println("Updating EmployeeID: " + employee.getEmployeeID());
        System.out.println("Using AccountID: " + employee.getAccountID());

        // Check if AccountID exists before updating
        if (!AccountDAO.isAccountExists(employee.getAccountID())) {
            System.out.println("Error: AccountID " + employee.getAccountID() + " does not exist.");
            return false;
        }


        String sql = "UPDATE Employee SET EmployeeName = ?, Email = ?, Phone = ?, Address = ?, Gender = ?, DateOfBirth = ?, Status = ?, AccountID = ?, WarehouseID = ?, Image = ? WHERE EmployeeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeName());
            pt.setString(2, employee.getEmail());
            pt.setString(3, employee.getPhone());
            pt.setString(4, employee.getAddress());
            pt.setString(5, employee.getGender());
            pt.setDate(6, Date.valueOf(employee.getDateOfBirth()));
            pt.setString(7, employee.getStatus());
            pt.setInt(8, employee.getAccountID());
            pt.setInt(9, employee.getWarehouseID());
            pt.setString(10, employee.getImage());
            pt.setInt(11, employee.getEmployeeID());

            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead
            return false; // Return false if an error occurs
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

    public Employee getEmployeeByID(int employeeID) {
        Employee employee = null;
        String sql = "SELECT * FROM Employee WHERE EmployeeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setInt(1, employeeID);
            try (ResultSet rs = pt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee();
                    employee.setEmployeeID(rs.getInt("EmployeeID"));
                    employee.setEmployeeName(rs.getString("EmployeeName"));
                    employee.setEmail(rs.getString("Email"));
                    employee.setPhone(rs.getString("Phone"));
                    employee.setAddress(rs.getString("Address"));
                    employee.setGender(rs.getString("Gender"));
                    employee.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
                    employee.setStatus(rs.getString("Status"));
                    employee.setAccountID(rs.getInt("AccountID")); // ✅ Fix
                    employee.setWarehouseID(rs.getInt("WarehouseID"));
                    employee.setImage(rs.getString("Image"));

                    // ✅ Debugging output
                    System.out.println("Found Employee: " + employee.getEmployeeID() + " | AccountID: " + employee.getAccountID());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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


    public static void main(String[] args) {
        EmployeeDAO employeeDAO = new EmployeeDAO();

        // Creating a test employee object with updated information
        Employee employee = new Employee();
        employee.setEmployeeID(1); // Make sure this ID exists in the database
        employee.setEmployeeName("John Updated");
        employee.setEmail("johnupdated@example.com");
        employee.setPhone("0387031262");
        employee.setAddress("456 Updated Street");
        employee.setGender("Male");
        employee.setDateOfBirth(LocalDate.of(1992, 10, 5));
        employee.setStatus("Active");
        employee.setAccountID(1);
        employee.setWarehouseID(2);
        employee.setImage("updated_profile.jpg");

        // Calling the updateEmployee method
        boolean isUpdated = employeeDAO.updateEmployee(employee);

        // Printing the result
        if (isUpdated) {
            System.out.println("Employee update successful!");
        } else {
            System.out.println("Employee update failed!");
        }
    }


}
