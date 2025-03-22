package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.InventoryDocDetail;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryDocDetailDAO {

    public static String generateInventoryDocDetailID() throws SQLException {
        String sql = "SELECT MAX(InventoryDocDetailID) FROM inventorydocdetail WHERE InventoryDocDetailID LIKE 'IDD%'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                String lastID = rs.getString(1); // Lấy ID lớn nhất hiện tại
                int num = Integer.parseInt(lastID.substring(3)); // Bỏ "IDD" và lấy số
                return String.format("IDD%05d", num + 1); // Tăng lên 1 và định dạng lại
            }
        }
        return "IDD00001"; // Trường hợp chưa có dữ liệu
    }

    public static void createInventoryDocDetail(String inventoryDocDetailID, String productDetailID,
                                                String counterID, String reCounterID, int originQuantity,
                                                int recountQuantity, Date updatedDate, String inventoryDocID) throws SQLException {
        String sql = "INSERT INTO inventorydocdetail (InventoryDocDetailID, ProductDetailID, CounterID, ReCounterID, OriginQuantity, RecountQuantity, UpdatedDate, InventoryDocID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inventoryDocDetailID);
            stmt.setString(2, productDetailID);
            stmt.setString(3, counterID);
            stmt.setString(4, reCounterID);
            stmt.setInt(5, originQuantity);
            stmt.setInt(6, recountQuantity);
            stmt.setDate(7, new java.sql.Date(updatedDate.getTime()));
            stmt.setString(8, inventoryDocID);

            stmt.executeUpdate();
        }
    }

    public static List<InventoryDocDetail> getInventoryDocDetailsByDocID(String inventoryDocID) {
        List<InventoryDocDetail> details = new ArrayList<>();
        String sql = "SELECT idd.InventoryDocDetailID, idd.ProductDetailID, idd.CounterID, " +
                "idd.ReCounterID, idd.OriginQuantity, idd.RecountQuantity, idd.UpdatedDate, " +
                "p.ProductName, pd.Weight, pd.Color, pd.Size " +
                "FROM inventorydocdetail idd " +
                "LEFT JOIN productdetail pd ON idd.ProductDetailID = pd.ProductDetailID " +
                "LEFT JOIN product p ON pd.ProductID = p.ProductID " +
                "WHERE idd.InventoryDocID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inventoryDocID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryDocDetail detail = new InventoryDocDetail();
                    detail.setInventoryDocId(rs.getString("InventoryDocDetailID"));
                    detail.setProductDetailId(rs.getString("ProductDetailID"));
                    detail.setCounterId(rs.getString("CounterID"));
                    detail.setRecounterId(rs.getString("ReCounterID"));
                    detail.setOriginalQuantity(rs.getInt("OriginQuantity"));
                    detail.setRecountQuantity(rs.getInt("RecountQuantity"));
                    detail.setUpdateDate(rs.getDate("UpdatedDate"));
                    detail.setProductName(rs.getString("ProductName"));
                    detail.setWeight(rs.getInt("Weight"));
                    detail.setColor(rs.getString("Color"));
                    detail.setSize(rs.getString("Size"));
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi
        }

        return details;
    }

    public static void firstCount(List<String> productDetailIds, List<Integer> recountQuantities, String inventoryDocId) {
        String sql = "UPDATE inventorydocdetail " +
                "SET RecountQuantity = ?, UpdatedDate = ? " +
                "WHERE ProductDetailID = ? AND InventoryDocID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            LocalDate currentDate = LocalDate.now(); // Lấy ngày hiện tại

            for (int i = 0; i < productDetailIds.size(); i++) {
                pstmt.setInt(1, recountQuantities.get(i)); // Cập nhật RecountQuantity
                pstmt.setDate(2, java.sql.Date.valueOf(currentDate)); // Cập nhật UpdatedDate
                pstmt.setString(3, productDetailIds.get(i)); // Điều kiện ProductDetailID
                pstmt.setString(4, inventoryDocId); // Điều kiện InventoryDocID
                pstmt.addBatch(); // Thêm vào batch để tối ưu cập nhật
            }

            pstmt.executeBatch(); // Thực thi batch update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void secondCount(List<String> productDetailIds,
                                                 List<Integer> recountQuantities,
                                                 String inventoryDocId,
                                                 String recounterId) {
        String sql = "UPDATE inventorydocdetail " +
                "SET RecountQuantity = ?, UpdatedDate = ?, ReCounterID = ? " +
                "WHERE ProductDetailID = ? AND InventoryDocID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            LocalDate currentDate = LocalDate.now(); // Lấy ngày hiện tại

            for (int i = 0; i < productDetailIds.size(); i++) {
                pstmt.setInt(1, recountQuantities.get(i)); // Cập nhật RecountQuantity
                pstmt.setDate(2, java.sql.Date.valueOf(currentDate)); // Cập nhật UpdatedDate
                pstmt.setString(3, recounterId); // Cập nhật ReCounterID
                pstmt.setString(4, productDetailIds.get(i)); // Điều kiện ProductDetailID
                pstmt.setString(5, inventoryDocId); // Điều kiện InventoryDocID

                pstmt.addBatch(); // Thêm vào batch để tối ưu cập nhật
            }

            pstmt.executeBatch(); // Thực thi batch update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
