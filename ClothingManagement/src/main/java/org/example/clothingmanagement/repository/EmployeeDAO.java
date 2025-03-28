package org.example.clothingmanagement.repository;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.clothingmanagement.entity.Employee;

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
        String sql = "INSERT INTO Employee (EmployeeID,EmployeeName, Email, Phone, Address, Gender, Dob, Status, WarehouseID, Image) " +
                "VALUES (?,?, ?, ?, ?, ?, ?, 'Active', ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeID());
            pt.setString(2, employee.getEmployeeName());
            pt.setString(3, employee.getEmail());
            pt.setString(4, employee.getPhone());
            pt.setString(5, employee.getAddress());
            pt.setBoolean(6, employee.isGender());
            pt.setDate(7, Date.valueOf(employee.getDob()));
            pt.setString(8, employee.getWarehouseID());
            pt.setString(9, employee.getImage());
            int rowsAffected = pt.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmployeeExisted(String employeeId ,String email, String phone) {
        boolean exists = false;
        String sql = "SELECT * FROM Employee WHERE (Email = ? OR Phone = ?) AND employeeId != ? ";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, email);
            pt.setString(2, phone);
            pt.setString(3, employeeId);
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
        String sql = "SELECT * FROM Employee WHERE (Email = ? OR Phone = ?)";
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
        String sql = "UPDATE Account SET Password = ? WHERE AccountID = ? AND status = 'Active'";
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

    public String getRoleNameByEmployeeID(String employeeID) {
        String sql = "SELECT r.RoleName FROM role r " +
                "JOIN account a ON r.RoleID = a.RoleID " +
                "WHERE a.EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employeeID);
            try (ResultSet rs = pt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("RoleName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTotalEmployeeCount() {
        String sql = "SELECT COUNT(*) AS total FROM Employee WHERE Status = 'Active'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql);
             ResultSet rs = pt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employee SET EmployeeName = ?, Email = ?, Phone = ?, Address = ?, Gender = ?, Dob = ?, Status = ?, WarehouseID = ?, Image = ? WHERE EmployeeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employee.getEmployeeName());
            pt.setString(2, employee.getEmail());
            pt.setString(3, employee.getPhone());
            pt.setString(4, employee.getAddress());
            pt.setBoolean(5, employee.isGender());
            pt.setDate(6, Date.valueOf(employee.getDob()));
            pt.setString(7, employee.getStatus());
            pt.setString(8, employee.getWarehouseID());
            pt.setString(9, employee.getImage());
            pt.setString(10, employee.getEmployeeID());
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getMaxEmployeeId() throws SQLException {
        int maxId = 0;
        String query = "SELECT MAX(CAST(SUBSTRING(employeeID, 5) AS UNSIGNED)) FROM employee";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
        }
        return maxId;
    }

    public static boolean updateAccountEmail(String employeeId, String newEmail) {
        String sql = "UPDATE account SET email = ? WHERE employeeId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, newEmail);
            pt.setString(2, employeeId);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasAccount(String employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM account WHERE EmployeeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu có tài khoản, trả về true
                }
            }
        }
        return false; // Không có tài khoản
    }



    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, w.WarehouseName " +
                "FROM Employee e " +
                "JOIN Warehouse w ON e.WarehouseID = w.WarehouseID " +
                "WHERE e.Status = 'Active'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql);
             ResultSet rs = pt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getString("EmployeeID"));
                employee.setEmployeeName(rs.getString("EmployeeName"));
                employee.setEmail(rs.getString("Email"));
                employee.setPhone(rs.getString("Phone"));
                employee.setAddress(rs.getString("Address"));
                employee.setGender(rs.getBoolean("Gender"));
                employee.setDob(rs.getDate("Dob").toLocalDate());
                employee.setStatus(rs.getString("Status"));
                employee.setWarehouseID(rs.getString("WarehouseID"));
                employee.setWarehouseName(rs.getString("WarehouseName")); // Lấy tên kho
                employee.setImage(rs.getString("Image"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> getAllEmployee(){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT employeeId, employeeName, email, phone, address, gender,dob,status,image,warehouseId FROM employee ");
            sql.append(" WHERE warehouseId = 'W001' ");
            sql.append(" ORDER BY employeeId");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                Employee employee = Employee.builder()
                        .employeeID(rs.getString("EmployeeID"))
                        .employeeName(rs.getString("EmployeeName"))
                        .email(rs.getString("Email"))
                        .phone(rs.getString("Phone"))
                        .address(rs.getString("Address"))
                        .gender(rs.getBoolean("Gender"))
                        .dob(LocalDate.parse(rs.getString("Dob")))
                        .status(rs.getString("Status"))
                        .warehouseID(rs.getString("WarehouseID"))
                        .image(rs.getString("Image"))
                        .build();
                employees.add(employee);
            }
            return employees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Employee> searchEmployee(String keyword, int page, int pageSize) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, w.WarehouseName " +
                "FROM Employee e " +
                "JOIN Warehouse w ON e.WarehouseID = w.WarehouseID " +
                "WHERE (e.EmployeeName LIKE ? OR e.Email LIKE ? OR e.Phone LIKE ? OR e.EmployeeID LIKE ?) " +
                "LIMIT ? OFFSET ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            stmt.setString(4, searchKeyword);
            stmt.setInt(5, pageSize);
            stmt.setInt(6, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getString("EmployeeID"));
                employee.setEmployeeName(rs.getString("EmployeeName"));
                employee.setEmail(rs.getString("Email"));
                employee.setPhone(rs.getString("Phone"));
                employee.setAddress(rs.getString("Address"));
                employee.setGender(rs.getBoolean("Gender"));
                employee.setDob(rs.getDate("Dob").toLocalDate());
                employee.setStatus(rs.getString("Status"));
                employee.setWarehouseID(rs.getString("WarehouseID"));
                employee.setWarehouseName(rs.getString("WarehouseName"));
                employee.setImage(rs.getString("Image"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public int getTotalEmployeeCount(String keyword) {
        String sql = "SELECT COUNT(*) AS total FROM Employee " +
                "WHERE (EmployeeName LIKE ? OR Email LIKE ? OR Phone LIKE ? OR EmployeeID LIKE ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            stmt.setString(4, searchKeyword);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Employee> getEmployeesWithPagination(int page, int pageSize) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, w.WarehouseName " +
                "FROM Employee e " +
                "JOIN Warehouse w ON e.WarehouseID = w.WarehouseID " +
                "WHERE e.Status = 'Active' " +
                "LIMIT ? OFFSET ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, pageSize);
            pt.setInt(2, (page-1) * pageSize);
            ResultSet rs = pt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getString("EmployeeID"));
                employee.setEmployeeName(rs.getString("EmployeeName"));
                employee.setEmail(rs.getString("Email"));
                employee.setPhone(rs.getString("Phone"));
                employee.setAddress(rs.getString("Address"));
                employee.setGender(rs.getBoolean("Gender"));
                employee.setDob(rs.getDate("Dob").toLocalDate());
                employee.setStatus(rs.getString("Status"));
                employee.setWarehouseID(rs.getString("WarehouseID"));
                employee.setWarehouseName(rs.getString("WarehouseName"));
                employee.setImage(rs.getString("Image"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Employee getEmployeeByID(String employeeID) {
        Employee employee = null;
        String sql = "SELECT e.*, w.WarehouseName " +
                "FROM Employee e " +
                "JOIN Warehouse w ON e.WarehouseID = w.WarehouseID " +
                "WHERE e.EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)){
            pt.setString(1, employeeID);
            try (ResultSet rs = pt.executeQuery()) {
                while (rs.next()) {
                    employee = new Employee();
                    employee.setEmployeeID(rs.getString("EmployeeID"));
                    employee.setEmployeeName(rs.getString("EmployeeName"));
                    employee.setEmail(rs.getString("Email"));
                    employee.setPhone(rs.getString("Phone"));
                    employee.setAddress(rs.getString("Address"));
                    employee.setGender(rs.getBoolean("Gender"));
                    employee.setDob(rs.getDate("Dob").toLocalDate());
                    employee.setStatus(rs.getString("Status"));
                    employee.setWarehouseID(rs.getString("WarehouseID"));
                    employee.setWarehouseName(rs.getString("WarehouseName"));
                    employee.setImage(rs.getString("Image"));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    public boolean deleteEmployee(String employeeID) {
        String sql = "UPDATE Employee SET Status = 'Inactive' WHERE EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employeeID);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getEmployeeId(){
        String sql ="SELECT EmployeeID FROM Employee WHERE status = 'Active'";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement pt = conn.prepareStatement(sql)) {
            ResultSet rs = pt.executeQuery();
            if(rs.next()) {
                return rs.getString("EmployeeID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Employee> getEmployeesWithoutAccount() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.EmployeeID, e.EmployeeName, e.Email, e.Phone, e.Address, e.Gender, " +
                "e.Dob, e.Status, e.WarehouseID, e.Image, w.WarehouseName " +
                "FROM Employee e " +
                "LEFT JOIN Account a ON e.EmployeeID = a.EmployeeID " +
                "LEFT JOIN Warehouse w ON e.WarehouseID = w.WarehouseID " +
                "WHERE a.EmployeeID IS NULL ";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql);
             ResultSet rs = pt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getString("EmployeeID"));
                employee.setEmployeeName(rs.getString("EmployeeName"));
                employee.setEmail(rs.getString("Email"));
                employee.setPhone(rs.getString("Phone"));
                employee.setAddress(rs.getString("Address"));
                employee.setGender(rs.getBoolean("Gender"));
                employee.setDob(rs.getDate("Dob").toLocalDate());
                employee.setStatus(rs.getString("Status"));
                employee.setWarehouseID(rs.getString("WarehouseID"));
                employee.setImage(rs.getString("Image"));
                employee.setWarehouseName(rs.getString("WarehouseName"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }


    public boolean isEmailExistForEmployee(String email, String employeeId) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Email = ? AND EmployeeID = ? AND Status = 'Active'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int getRoleIdByEmployeeId(String employeeId) throws SQLException {
        String sql = "SELECT RoleID FROM Employee WHERE EmployeeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameter
            stmt.setString(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Return RoleID of the employee
                    return rs.getInt("RoleID");
                } else {
                    throw new SQLException("Employee not found with EmployeeID: " + employeeId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Rethrow exception after logging
        }
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

    public static Employee getEmployeeByAccountId(String accountId) {
        Employee employee = null;
        String sql = "SELECT e.*, w.WarehouseName " +
                "FROM Employee e " +
                "JOIN Warehouse w ON e.WarehouseID = w.WarehouseID " +
                "JOIN Account a ON e.EmployeeID = a.EmployeeID " +
                "WHERE a.AccountID = ? AND e.Status = 'Active'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, accountId);
            try (ResultSet rs = pt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee();
                    employee.setEmployeeID(rs.getString("EmployeeID"));
                    employee.setEmployeeName(rs.getString("EmployeeName"));
                    employee.setEmail(rs.getString("Email"));
                    employee.setPhone(rs.getString("Phone"));
                    employee.setAddress(rs.getString("Address"));
                    employee.setGender(rs.getBoolean("Gender"));
                    employee.setDob(rs.getDate("Dob").toLocalDate());
                    employee.setStatus(rs.getString("Status"));
                    employee.setWarehouseID(rs.getString("WarehouseID"));
                    employee.setWarehouseName(rs.getString("WarehouseName"));
                    employee.setImage(rs.getString("Image"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    public String getEmployeeIdByAccountId(String accountId) {
        String sql = "SELECT EmployeeID FROM Account WHERE AccountID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setString(1, accountId); // Set AccountID as a String

            try (ResultSet rs = pt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("EmployeeID"); // Return EmployeeID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no matching EmployeeID is found
    }
    public String getEmployeeIdByAccountId(int accountId) {
        String sql = "SELECT EmployeeID FROM Account WHERE AccountID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("EmployeeID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy EmployeeID
    }

    public Employee getEmployeeByEmployeeId(String employeeId) {
        Employee employee = null;
        String sql = "SELECT * FROM employee WHERE EmployeeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, employeeId);
            try (ResultSet rs = pt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee();
                    employee.setEmployeeID(rs.getString("EmployeeID"));
                    employee.setEmployeeName(rs.getString("EmployeeName"));
                    employee.setEmail(rs.getString("Email"));
                    employee.setPhone(rs.getString("Phone"));
                    employee.setAddress(rs.getString("Address"));
                    employee.setGender(rs.getBoolean("Gender"));
                    employee.setDob(rs.getDate("Dob").toLocalDate());
                    employee.setStatus(rs.getString("Status"));
                    employee.setWarehouseID(rs.getString("WarehouseID"));
                    employee.setImage(rs.getString("Image"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }


    public void updateEmployeeImage(Employee employee) throws SQLException {
        String query = "UPDATE Employee SET image = ? WHERE employeeID = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, employee.getImage());
            preparedStatement.setString(2, employee.getEmployeeID());

            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {

    }

    public List<String> getAllEmployeeIds() throws SQLException {
        List<String> employeeIds = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT employeeID FROM employee ORDER BY employeeID");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employeeIds.add(rs.getString("employeeID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeIds;
    }
}
