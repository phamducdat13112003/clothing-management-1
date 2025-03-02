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
}