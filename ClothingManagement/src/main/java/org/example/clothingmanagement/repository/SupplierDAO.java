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
            sql.append(" SELECT SupplierID, SupplierName, Address, ContactEmail, Phone, Status FROM Supplier ");
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

    public boolean isSupplierExistedWhenAdd(String email, String phone) {
        boolean exists = false;
        String sql = "SELECT * FROM Supplier WHERE (ContactEmail = ? OR Phone = ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, email);
            pt.setString(2, phone);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra console để debug
        }
        return exists;
    }

    public boolean isSupplierExisted(String supplierId, String email, String phone) {
        boolean exists = false;
        String sql = "SELECT * FROM Supplier WHERE (ContactEmail = ? OR Phone = ?) AND SupplierID != ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, email);
            pt.setString(2, phone);
            pt.setString(3, supplierId);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra console để debug
        }
        return exists;
    }

    public Supplier getSupplierById(String supplierId) {
        Supplier supplier = null;
        String sql = "SELECT * FROM Supplier WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, supplierId);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                supplier = new Supplier();
                supplier.setSupplierId(rs.getString("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setEmail(rs.getString("ContactEmail"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setStatus(rs.getBoolean("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }

    public boolean createSupplier(Supplier supplier) {
        String sql = "INSERT INTO Supplier (SupplierID, SupplierName, Address, ContactEmail, Phone, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, supplier.getSupplierId());
            pt.setString(2, supplier.getSupplierName());
            pt.setString(3, supplier.getAddress());
            pt.setString(4, supplier.getEmail());
            pt.setString(5, supplier.getPhone());
            pt.setBoolean(6, supplier.isStatus());
            int rowsAffected = pt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE Supplier SET SupplierName = ?, Address = ?, ContactEmail = ?, Phone = ?, Status = ? WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, supplier.getSupplierName());
            pt.setString(2, supplier.getAddress());
            pt.setString(3, supplier.getEmail());
            pt.setString(4, supplier.getPhone());
            pt.setBoolean(5, supplier.isStatus());
            pt.setString(6, supplier.getSupplierId());
            int rowsAffected = pt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSupplier(String supplierId) {
        String sql = "UPDATE Supplier SET Status = 0 WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setString(1, supplierId);
            int rowsUpdated = pt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Supplier> searchSupplier(String keyword, int page, int pageSize) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM Supplier " +
                "WHERE (SupplierName LIKE ? OR ContactEmail LIKE ? OR Phone LIKE ? OR SupplierID LIKE ?) " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            stmt.setString(4, searchKeyword);
            stmt.setInt(5, pageSize);
            stmt.setInt(6, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getString("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setEmail(rs.getString("ContactEmail"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setStatus(rs.getBoolean("Status"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public int getTotalSupplierCount(String keyword) {
        String sql = "SELECT COUNT(*) AS total FROM Supplier " +
                "WHERE (SupplierName LIKE ? OR ContactEmail LIKE ? OR Phone LIKE ? OR SupplierID LIKE ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            stmt.setString(4, searchKeyword);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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


    public int getMaxSupplierId() throws SQLException {
        int maxId = 0;
        String query = "SELECT MAX(CAST(SUBSTRING(supplierID, 5) AS UNSIGNED)) FROM Supplier";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
        }
        return maxId;
    }


    public static void main(String[] args) {
    }
}
