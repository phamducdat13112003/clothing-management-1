package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public static boolean isAccountExists(int accountId) {
        String sql = "SELECT AccountID FROM Account WHERE AccountID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, accountId);
            ResultSet rs = pt.executeQuery();
            return rs.next(); // Returns true if AccountID exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Account> getAccountsByPage(int page, int pageSize) throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, r.RoleName " +
                "FROM Account a " +
                "JOIN Role r ON a.RoleID = r.RoleID " +
                "WHERE a.status = 'Active' AND r.RoleName != 'Manager' " +
                "LIMIT ? OFFSET ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getString("accountId"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setStatus(rs.getString("status"));
                account.setRoleId(rs.getInt("roleID"));
                account.setRoleName(rs.getString("roleName"));
                account.setEmployeeId(rs.getString("employeeID"));
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getMaxAccountId() throws SQLException {
        int maxId = 0;
        String query = "SELECT MAX(CAST(SUBSTRING(AccountID, 6) AS UNSIGNED)) FROM Account";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
        }
        return maxId;
    }

    public int getTotalAccounts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Account WHERE status = 'Active'";
        try (Connection connection = DBContext.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Account> searchAccount(String keyword, int page, int pageSize) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, r.RoleName " +
                "FROM Account a " +
                "JOIN Role r ON a.RoleID = r.RoleID " +
                "WHERE (a.email LIKE ? OR a.employeeID LIKE ?) " +
                "AND r.RoleName != 'Manager' " +
                "LIMIT ? OFFSET ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setInt(3, pageSize);
            stmt.setInt(4, (page - 1) * pageSize);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getString("accountId"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setStatus(rs.getString("status"));
                account.setRoleId(rs.getInt("roleID"));
                account.setEmployeeId(rs.getString("employeeID"));
                account.setRoleName(rs.getString("RoleName")); 
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }


    public int getTotalAccounts(String keyword) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Account WHERE status = 'Active' AND (email LIKE ? OR employeeID LIKE ?)";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Account getAccountById(String accountId) throws SQLException {
        Account account = new Account();
        String sql = "SELECT * FROM account WHERE accountId = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account.setId(rs.getString("accountId"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setStatus(rs.getString("status"));
                account.setRoleId(rs.getInt("roleID"));
                account.setEmployeeId(rs.getString("employeeId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE account SET email = ?, password = ? ,LastUpdate=? , roleID = ?, status = ? WHERE accountId = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(account.getLastUpdate()));
            stmt.setInt(4, account.getRoleId());
            stmt.setString(5, account.getStatus());
            stmt.setString(6, account.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAccountExist(String email, String accountId) throws SQLException {
        String sql = "SELECT * FROM account WHERE email = ? AND accountId != ? ";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAccount(String accountId) throws SQLException {
        String sql = "UPDATE account SET status = 'Inactive' WHERE accountId = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAccountWhenDeleteEmployee(String employeeId) throws SQLException {
        String sql = "UPDATE Account SET status = 'Inactive' WHERE employeeId = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false if there is any exception
        }
    }

    public boolean updateStatusAccount(String status, String employeeId) throws SQLException {
        String sql = "UPDATE Account SET status = ? WHERE employeeId = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, employeeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account (AccountID,Email, Password, LastUpdate, RoleID, Status, EmployeeID) VALUES (?,?, ?, ?,?,'Active',?)";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getId());
            stmt.setString(2, account.getEmail());
            stmt.setString(3, account.getPassword());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(account.getLastUpdate()));
            stmt.setInt(5, account.getRoleId());
            stmt.setString(6, account.getEmployeeId());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Role> getAllRoles() throws SQLException {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM role where RoleName != 'Manager'";
        try (Connection connection = DBContext.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("roleID"));
                role.setRoleName(rs.getString("roleName"));
                list.add(role);
            }
        }
        return list;
    }

    public static int getRoleIdByAccountId(int accountId) {
        String sql = "SELECT RoleID FROM Account WHERE AccountID = ?";
        try (Connection connection = DBContext.getConnection(); // Use DBContext for connection
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RoleID"); // Return the RoleID if found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }
        return -1; // Return -1 if AccountID is not found or an error occurs
    }

    public static String getEmployeeNameById(String employeeId) {
        String sql = "SELECT EmployeeName FROM Employee WHERE EmployeeID = ?";
        try (Connection connection = DBContext.getConnection(); // Use DBContext for connection
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("EmployeeName"); // Return the EmployeeName if found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }
        return null; // Return null if EmployeeID is not found or an error occurs
    }


    public Account findAccount(String email, String password) {
        String sql = "SELECT * FROM account WHERE email='" + email + "' AND password='" + password + "'";
        Account account = null;
        try (Connection connection = DBContext.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                account = new Account();
                account.setId(rs.getString("accountId"));
                account.setEmail(rs.getString("email"));
                account.setStatus(rs.getString("status"));
                account.setPassword(rs.getString("password"));
                account.setEmployeeId(rs.getString("employeeId"));
                account.setRoleId(rs.getInt("roleID"));
                account.setEmployeeId(rs.getString("employeeId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean updatePassword(String accountId, String newPassword) throws SQLException {
        String sql = "UPDATE account SET password = ? WHERE accountId = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, accountId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPasswordByAccountId(String accountId) {
        String password = null; // To store the fetched password
        String sql = "SELECT password FROM account WHERE accountId = ?"; // SQL query to get the password

        try (Connection connection = DBContext.getConnection(); // Get database connection
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, accountId); // Set the accountId in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    password = rs.getString("password"); // Fetch the password from the result set
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }

        return password; // Return the fetched password, or null if not found
    }

    public String checkEmailExist(String email) {
        String sql = "SELECT * FROM Account WHERE email = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return email;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassByEmail(String password, String email) {
        String sql = "UPDATE Account SET Password = ? WHERE Email = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, password);
            st.setString(2, email);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        // AccountDAO dao = new AccountDAO();
        // List<Account > list= dao.getAllAccount();
        // for(Account account:list){
            // System.out.println(account.getId());
        //}
    }

}
