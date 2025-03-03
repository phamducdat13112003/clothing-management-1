package org.example.clothingmanagement.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryOrderDetailDAO {
    public static boolean addDODetail(String doDetailID, String productDetailID, int quantity, String doID) {
        String query = "INSERT INTO DODetail (DODetailID, ProductDetailID, Quantity, DOID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doDetailID);
            stmt.setString(2, productDetailID);
            stmt.setInt(3, quantity);
            stmt.setString(4, doID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String generateDODetailID() throws SQLException {
        String query = "SELECT MAX(DODetailID) FROM DODetail";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String maxDODetailID = rs.getString(1);
                if (maxDODetailID != null) {
                    int nextID = Integer.parseInt(maxDODetailID.substring(3)) + 1;
                    return "DOD" + nextID;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DOD1"; // Nếu chưa có dữ liệu, bắt đầu từ DOD1001
    }

}
