package org.example.clothingmanagement.service;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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




