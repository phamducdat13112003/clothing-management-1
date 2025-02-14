package org.example.clothingmanagement.repository;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Role;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private static final DataSource dataSource = createDataSource();

    private static DataSource createDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/warehousemanagement");
        ds.setUser("root");
        ds.setPassword("");
        return ds;
    }

    // Method to get role name by role ID
    public static String getRoleNameById(int roleId) {
        String sql = "SELECT RoleName FROM Role WHERE RoleID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, roleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("RoleName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if role ID not found or an error occurs
    }
    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role WHERE RoleName != 'Manager'";
        try (Connection connection = DBContext.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("RoleID"));
                role.setRoleName(rs.getString("RoleName"));
                list.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        int roleId = 1; // Replace with the role ID you want to fetch
        String roleName = RoleDAO.getRoleNameById(roleId);

        if (roleName != null) {
            System.out.println("Role Name: " + roleName);
        } else {
            System.out.println("Role not found for ID: " + roleId);
        }
    }

}




