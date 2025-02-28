package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    public List<Supplier> getAllSuppliers() {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SupplierID, SupplierName, Address, ContactEmail, Phone FROM Supplier ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<Supplier> suppliers = new ArrayList<>();
            while(rs.next()){
                Supplier supplier = Supplier.builder()
                        .supplierId(rs.getString("SupplierID"))
                        .supplierName(rs.getString("SupplierName"))
                        .address(rs.getString("Address"))
                        .email(rs.getString("ContactEmail"))
                        .phone(rs.getString("Phone"))
                        .status(rs.getBoolean("Status"))
                        .build();
                suppliers.add(supplier);
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Supplier> getSuppliersWithPagination(int page, int pageSize) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM Supplier WHERE Status = 1 LIMIT ? OFFSET ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, pageSize);
            pt.setInt(2, (page - 1) * pageSize);
            ResultSet rs = pt.executeQuery();
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getString("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setEmail(rs.getString("ContactEmail"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setStatus(rs.getBoolean("Status"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    public int getTotalSupplierCount() {
        String sql = "SELECT COUNT(*) AS total FROM Supplier WHERE Status = 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql);
             ResultSet rs = pt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public Supplier getSupplierBySupplierID(String supplierID) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT * FROM `supplier` WHERE SupplierID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, supplierID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Supplier.builder()
                        .supplierId(rs.getString("SupplierID"))
                        .supplierName(rs.getString("SupplierName"))
                        .address(rs.getString("Address"))
                        .email(rs.getString("ContactEmail"))
                        .phone(rs.getString("Phone"))
                        .status(rs.getBoolean("Status"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static void main(String[] args) {
        SupplierDAO dao = new SupplierDAO();
        List<Supplier> list = dao.getSuppliersWithPagination(1, 5);
        for (Supplier supplier : list) {
            System.out.println(supplier);
        }
    }
}
