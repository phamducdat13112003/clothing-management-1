package org.example.clothingmanagement.service;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class WarehouseDAO {

    private static final DataSource dataSource = createDataSource();

    private static DataSource createDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/warehousemanagement");
        ds.setUser("root");
        ds.setPassword("");
        return ds;
    }

    // Retrieve warehouse name by ID
    public static String getWarehouseNameById(int warehouseID) throws SQLException {
        String sql = "SELECT WarehouseName FROM Warehouse WHERE WarehouseID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, warehouseID);
            try (ResultSet resultSet = pt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("WarehouseName");
                }
            }
        }
        return null; // Return null if not found
    }
}
