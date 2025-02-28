package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.PurchaseOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAO {
//CRUD
public List<PurchaseOrder> getAllPurchaseOrder() {
    try(Connection conn = DBContext.getConnection()){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM `po`");
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ResultSet rs = ps.executeQuery();
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        while(rs.next()){
            PurchaseOrder purchaseOrder = new PurchaseOrder(
            rs.getString("POID"),
            rs.getDate("CreatedDate"),
            rs.getString("SupplierID"),
            rs.getString("CreatedBy"),
            rs.getString("Status"),
            rs.getFloat("TotalPrice")
            );
            purchaseOrders.add(purchaseOrder);
        }
        return purchaseOrders;

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}
    public String getSupplierIDByPoID(String poID) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT SupplierID FROM `po` WHERE POID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, poID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("SupplierID"); // Trả về SupplierID nếu tìm thấy
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Trả về null nếu không tìm thấy
    }
    public List<PurchaseOrder> searchPO(String searchQuery) {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        String sql = "SELECT * FROM po WHERE POID LIKE ? OR CreatedDate LIKE ? OR SupplierID LIKE ? OR TotalPrice LIKE ? OR CreatedBy LIKE ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thêm wildcard "%" vào trước và sau searchQuery để tìm kiếm mọi vị trí trong chuỗi
            String searchPattern = "%" + searchQuery + "%";
            for (int i = 1; i <= 5; i++) {
                ps.setString(i, searchPattern);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PurchaseOrder purchaseOrder = new PurchaseOrder(
                        rs.getString("POID"),
                        rs.getDate("CreatedDate"),
                        rs.getString("SupplierID"),
                        rs.getString("CreatedBy"),
                        rs.getString("Status"),
                        rs.getFloat("TotalPrice")
                );
                purchaseOrders.add(purchaseOrder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return purchaseOrders;
    }
    public PurchaseOrder getPObyPoID(String poID) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT * FROM `po` WHERE POID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, poID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new PurchaseOrder(
                        rs.getString("POID"),
                        rs.getDate("CreatedDate"),
                        rs.getString("SupplierID"),
                        rs.getString("CreatedBy"),
                        rs.getString("Status"),
                        rs.getFloat("TotalPrice")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
