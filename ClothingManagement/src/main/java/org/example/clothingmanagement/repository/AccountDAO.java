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


    public List<Account> getAllAccount() throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, r.RoleName\n" +
                "FROM Account a\n" +
                "JOIN Role r ON a.RoleID = r.RoleID\n" +
                "WHERE a.status = 'Active'";
        try (Connection connection = DBContext.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getString("accountId"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setStatus(rs.getString("status"));
                account.setRoleId(rs.getInt("roleID"));
                account.setRoleName(rs.getString("roleName"));
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
        String sql = "UPDATE account SET email = ?, password = ? , roleID = ?, status = ? WHERE accountId = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getRoleId());
            stmt.setString(4, account.getStatus());
            stmt.setString(5, account.getId());
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


    public void createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account (Email, Password, RoleID, Status, EmployeeID) VALUES (?, ?, ?, 'Active', ?)";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getRoleId());
            stmt.setString(4, account.getEmployeeId());
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
                account.setPassword(rs.getString("password"));
                account.setRoleId(rs.getInt("roleID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean updatePassword(int accountId, String newPassword) throws SQLException {
        String sql = "UPDATE account SET password = ? WHERE accountId = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, accountId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPasswordByAccountId(int accountId) {
        String password = null; // To store the fetched password
        String sql = "SELECT password FROM account WHERE accountId = ?"; // SQL query to get the password

        try (Connection connection = DBContext.getConnection(); // Get database connection
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, accountId); // Set the accountId in the query

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
        AccountDAO dao = new AccountDAO();
        List<Account > list= dao.getAllAccount();
        for(Account account:list){
            System.out.println(account.getId());
        }
    }

}
