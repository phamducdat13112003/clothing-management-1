package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.ProductDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DashBoardDAO {

    public int getTotalOrders(){
        int totalOrders = 0;
        String sql ="SELECT COUNT(*) AS TotalPOs FROM PO";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                totalOrders = rs.getInt("TotalPOs");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalOrders;
    }

    public double getTotalOrderValue() {
        double totalValue = 0;
        String sql = "SELECT SUM(TotalPrice) AS TotalOrderValue FROM PO WHERE Status = 'Done'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalValue = rs.getDouble("TotalOrderValue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalValue;
    }

    public int getTotalSuppliers(){
        int TotalSuppliers = 0;
        String sql ="SELECT COUNT(*) as TotalSuppliers FROM Supplier WHERE status = 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                TotalSuppliers = rs.getInt("TotalSuppliers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return TotalSuppliers;
    }

    public int getTotalEmployees(){
        int TotalEmployees = 0;
        String sql ="SELECT COUNT(*) as TotalEmployees FROM Employee WHERE status = 'Active'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                TotalEmployees = rs.getInt("TotalEmployees");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return TotalEmployees;
    }

    public Map<String, Double> getTotalPOByMonth() {
        Map<String, Double> poByMonth = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(CreatedDate, '%Y-%m') AS Month, SUM(TotalPrice) AS TotalValue " +
                "FROM PO WHERE Status = 'Done' GROUP BY Month ORDER BY Month";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                poByMonth.put(rs.getString("Month"), rs.getDouble("TotalValue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poByMonth;
    }


    public Map<String, Double> getTotalPOByMonth(String startDate, String endDate) {
        Map<String, Double> poByMonth = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(CreatedDate, '%Y-%m') AS Month, SUM(TotalPrice) AS TotalValue " +
                "FROM PO WHERE Status = 'Done' And CreatedDate BETWEEN ? AND ? GROUP BY Month ORDER BY Month";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                poByMonth.put(rs.getString("Month"), rs.getDouble("TotalValue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poByMonth;
    }

    public List<ProductDetail> getProductStorageList(String productId) {
        List<ProductDetail> list = new ArrayList<>();
        String sql = """
                SELECT pd.ProductDetailID, p.ProductName, b.BinID, s.SectionID, pd.Weight, 
                    pd.Color, 
                    pd.Size, 
                    SUM(bd.quantity) AS TotalQuantity,
                    COALESCE(SUM(CASE WHEN b.Status = 1 THEN bd.quantity END), 0) AS AvailableQuantity,
                    COALESCE(SUM(CASE WHEN b.Status = 0 THEN bd.quantity END), 0) AS BlockedQuantity
                FROM Bin b
                JOIN BinDetail bd ON b.BinID = bd.BinID
                JOIN ProductDetail pd ON bd.ProductDetailID = pd.ProductDetailID
                JOIN Product p ON pd.ProductID = p.ProductID
                JOIN Section s ON b.SectionID = s.SectionID
                WHERE pd.ProductDetailID = ?
                GROUP BY pd.ProductDetailID, p.ProductName, b.BinID, s.SectionID;
                """;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, productId);
             ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductDetail product = new ProductDetail();
                product.setId(rs.getString("ProductDetailID"));
                product.setProductName(rs.getString("ProductName"));
                product.setBinId(rs.getString("BinID"));
                product.setSectionId(rs.getString("SectionID"));
                product.setWeight(rs.getDouble("Weight"));
                product.setColor(rs.getString("Color"));
                product.setSize(rs.getString("Size"));
                product.setTotalQuantity(rs.getInt("TotalQuantity"));
                product.setAvailableQuantity(rs.getInt("AvailableQuantity"));
                product.setBlockedQuantity(rs.getInt("BlockedQuantity"));
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BinDetail getTotalQuantityByProductDetailId(String productDetailId) {
        String sql = "SELECT SUM(quantity) AS totalQuantity FROM BinDetail WHERE ProductDetailID = ?";
        BinDetail binDetail = null;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productDetailId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int totalQuantity = rs.getInt("totalQuantity");
                binDetail = new BinDetail(productDetailId, totalQuantity);
                return binDetail;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không có dữ liệu
    }

    public List<BinDetail> getBinCapacityPercentage(int page, int pageSize) {
        String sql = "SELECT b.binId, SUM(b.quantity * p.Weight) AS totalWeightInBin, bin.MaxCapacity AS maxCapacity, " +
                "(SUM(b.quantity * p.Weight) / bin.MaxCapacity) * 100 AS binFullPercentage, " +
                "(1 - (SUM(b.quantity * p.Weight) / bin.MaxCapacity)) * 100 AS binRemainingPercentage " +
                "FROM BinDetail b " +
                "JOIN ProductDetail p ON b.ProductDetailID = p.ProductDetailID " +
                "JOIN Bin bin ON b.binId = bin.binId " +
                "GROUP BY b.binId, bin.MaxCapacity " +
                "LIMIT ? OFFSET ?";
        List<BinDetail> binCapacityList = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BinDetail binCapacity = new BinDetail();
                binCapacity.setBinId(rs.getString("binId"));
                binCapacity.setWeight(rs.getDouble("totalWeightInBin"));
                binCapacity.setMaxCapacity(rs.getDouble("maxCapacity"));
                binCapacity.setBinFullPercentage(rs.getDouble("binFullPercentage"));
                binCapacity.setBinRemainingPercentage(rs.getDouble("binRemainingPercentage"));
                binCapacityList.add(binCapacity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return binCapacityList;
    }

    public int getTotalPages() {
        String sql = "SELECT COUNT(DISTINCT binId) AS total FROM BinDetail";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int totalRecords = rs.getInt("total");
                return totalRecords;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



}
