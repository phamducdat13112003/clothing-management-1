package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.PurchaseOrder;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PurchaseOrderDAO {
    //CRUD
    public List<PurchaseOrder> getAllPurchaseOrder() {
        try (Connection conn = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM `po` \n" +
                    "ORDER BY FIELD(Status, 'Pending', 'Confirmed', 'Processing', 'Cancel', 'Done'),\n" +
                    "CreatedDate DESC;\n");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<PurchaseOrder> purchaseOrders = new ArrayList<>();
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
            return purchaseOrders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<PurchaseOrder> getAllPurchaseOrderHaveStatusProcessingAndConfirmed() {
        try (Connection conn = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * \n" +
                    "FROM `po` \n" +
                    "WHERE Status IN ('Confirmed', 'Processing', 'Done') \n" +
                    "ORDER BY \n" +
                    "    FIELD(Status, 'Confirmed', 'Processing', 'Done'),\n" +
                    "    CreatedDate DESC;\n");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<PurchaseOrder> purchaseOrders = new ArrayList<>();
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
        String sql = "SELECT * FROM po WHERE POID LIKE ? OR CreatedDate LIKE ? OR SupplierID LIKE ? OR TotalPrice LIKE ? OR CreatedBy LIKE ? OR Status LIKE ? ORDER BY FIELD(Status, 'Pending', 'Confirmed', 'Processing','Cancel','Done')";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thêm wildcard "%" vào trước và sau searchQuery để tìm kiếm mọi vị trí trong chuỗi
            String searchPattern = "%" + searchQuery + "%";
            for (int i = 1; i <= 6; i++) {
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
    public List<PurchaseOrder> searchDO(String searchQuery) {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        String sql = "SELECT * FROM po WHERE POID LIKE ? OR CreatedDate LIKE ? OR SupplierID LIKE ? OR TotalPrice LIKE ? OR CreatedBy LIKE ? OR Status LIKE ? ORDER BY FIELD(Status, 'Confirmed', 'Processing','Done')";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thêm wildcard "%" vào trước và sau searchQuery để tìm kiếm mọi vị trí trong chuỗi
            String searchPattern = "%" + searchQuery + "%";
            for (int i = 1; i <= 6; i++) {
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

    //generateUniquePoId
    private static final SecureRandom random = new SecureRandom();

    public static String generateUniquePoId(String date) {
        date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // Lấy ngày hiện tại theo định dạng yyyyMMdd
        String poid;
        do {
            int randomNumber = 10000 + random.nextInt(90000); // Sinh số từ 10000 đến 99999
            poid = "PO" + date + randomNumber;
        } while (isPoIdExists(poid)); // Nếu trùng thì random lại

        return poid;
    }

    private static boolean isPoIdExists(String poid) {
        String sql = "SELECT COUNT(*) FROM po WHERE POID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, poid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Trùng mã POID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Không trùng
    }

    //
    public boolean addPurchaseOrder(PurchaseOrder purchaseOrder) {
        String sql = "INSERT INTO `po`(`POID`, `CreatedDate`, `SupplierID`, `CreatedBy`, `Status`, `TotalPrice`) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, purchaseOrder.getPoID());
            ps.setDate(2, new java.sql.Date(purchaseOrder.getCreatedDate().getTime()));
            ps.setString(3, purchaseOrder.getSupplierID());
            ps.setString(4, purchaseOrder.getCreatedBy());
            ps.setString(5, purchaseOrder.getStatus());
            ps.setFloat(6, purchaseOrder.getTotalPrice());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm đơn hàng: " + e.getMessage(), e);
        }
    }

    //updateStatusPO
    public boolean updateStatusPO(String poID, String newStatus) {
        String sql = "UPDATE `po` SET `Status`= ? WHERE POID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, poID);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu có ít nhất một dòng được cập nhật

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật trạng thái PO: " + e.getMessage(), e);
        }
    }

    public boolean updatePO(String poID, String supplierID, float totalPrice) {
        String sql = "UPDATE `po` SET `SupplierID`= ?, `TotalPrice`= ? WHERE POID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplierID);
            ps.setFloat(2, totalPrice);
            ps.setString(3, poID);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu có ít nhất một dòng được cập nhật

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật PO: " + e.getMessage(), e);
        }
    }

}
