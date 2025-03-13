package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.DeliveryOrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<DeliveryOrderDetail> getDODetailsByDOID(String doid) {
            List<DeliveryOrderDetail> details = new ArrayList<>();
            String sql = "SELECT * FROM DODetail WHERE DOID = ?";

            try (Connection conn = DBContext.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, doid);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    DeliveryOrderDetail detail = new DeliveryOrderDetail();
                    detail.setDoDetailID(rs.getString("doDetailID"));
                    detail.setProductDetailID(rs.getString("ProductDetailID"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setDoID(rs.getString("DOID"));

                    details.add(detail);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return details;
        }

    public boolean updateDODetailForPS(Map<String, Integer> quantityUpdates) {
        String sql = "UPDATE DODetail SET Quantity = ? WHERE DODetailID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map.Entry<String, Integer> entry : quantityUpdates.entrySet()) {
                ps.setInt(1, entry.getValue());  // Quantity mới
                ps.setString(2, entry.getKey()); // DODetailID
                ps.addBatch();
            }
            int[] updatedRows = ps.executeBatch();
            return updatedRows.length > 0; // Trả về true nếu có cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
