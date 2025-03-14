package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.entity.PurchaseOrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class PurchaseOrderDetailDAO {
    public List<PurchaseOrderDetail> getListPODetailbypoID(String poID) {
        List<PurchaseOrderDetail> poDetails = new ArrayList<>();
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT * FROM `podetail` WHERE POID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, poID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                poDetails.add(new PurchaseOrderDetail(
                        rs.getString("POdetailID"),
                        rs.getString("POID"),
                        rs.getString("ProductDetailID"),
                        rs.getInt("Quantity"),
                        rs.getFloat("Price"),
                        rs.getFloat("TotalPrice")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return poDetails;
    }

    public String getProductDetailID(String poID) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT ProductDetailID FROM `podetail` WHERE POID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, poID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("ProductDetailID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void addPurchaseOrderDetail(String poID, String[] productDetailIDs, String[] poDetailIDs, String[] prices, String[] quantities, String[] totalPrices) {
        String sql = "INSERT INTO `podetail`(`POdetailID`, `POID`, `ProductDetailID`, `Quantity`, `Price`, `TotalPrice`) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < productDetailIDs.length; i++) {
                ps.setString(1, poDetailIDs[i]);
                ps.setString(2, poID);
                ps.setString(3, productDetailIDs[i]);
                ps.setInt(4, Integer.parseInt(quantities[i]));
                ps.setFloat(5, Float.parseFloat(prices[i]));
                ps.setFloat(6, Float.parseFloat(totalPrices[i]));

                ps.addBatch(); // Thêm vào batch để thực thi nhiều lần
            }

            ps.executeBatch(); // Thực thi tất cả các câu lệnh trong batch

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm chi tiết đơn hàng: " + e.getMessage(), e);
        }
    }
    public void deletePoDetailByPoID(String poID) {
        String sql = "DELETE FROM `podetail` WHERE POID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, poID);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa chi tiết đơn hàng: " + e.getMessage(), e);
        }
    }

}