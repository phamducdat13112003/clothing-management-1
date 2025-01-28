package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.repository.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public List<Account> getAllAccount() throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM account";

        try (Connection connection = DBContext.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("accountId"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setRoleId(rs.getInt("roleID"));
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Account getAccountById(int accountId) throws SQLException {
        Account account = new Account();
        String sql = "SELECT * FROM account WHERE accountId = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account.setId(rs.getInt("accountId"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setRoleId(rs.getInt("roleID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE account SET email = ?, password = ? WHERE accountId = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAccountExist(String email) throws SQLException {
        String sql = "SELECT * FROM account WHERE email = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM account WHERE accountId = ?";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account(email, password, roleID) VALUES(?,?,?)";
        try (Connection connection = DBContext.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getRoleId());
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


    public static void main(String[] args) throws SQLException {
        AccountDAO dao = new AccountDAO();
        List<Role > list= dao.getAllRoles();
        for(Role account:list){
            System.out.println(account.getRoleName());
        }
    }

}
