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
        String sql = "SELECT p.POID, p.CreatedDate, s.SupplierName, e.EmployeeName, p.Status, p.TotalPrice " +
                "FROM po p " +
                "JOIN employee e ON p.CreatedBy = e.EmployeeID " +
                "JOIN supplier s ON p.SupplierID = s.SupplierID " +
                "ORDER BY FIELD(p.Status, 'Pending', 'Confirmed', 'Processing', 'Cancel', 'Done'), " +
                "p.CreatedDate DESC";

        List<PurchaseOrder> purchaseOrders = new ArrayList<>();

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PurchaseOrder purchaseOrder = new PurchaseOrder(
                        rs.getString("POID"),
                        rs.getDate("CreatedDate"),
                        rs.getString("SupplierName"),  // Lấy tên supplier thay vì ID
                        rs.getString("EmployeeName"),  // Lấy tên employee thay vì ID
                        rs.getString("Status"),
                        rs.getFloat("TotalPrice")
                );
                purchaseOrders.add(purchaseOrder);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving purchase orders", e);
        }

        return purchaseOrders;
    }


    public List<PurchaseOrder> getAllPurchaseOrderHaveStatusProcessingAndConfirmed() {
        String sql = "SELECT p.POID, p.CreatedDate, s.SupplierName, e.EmployeeName, p.Status, p.TotalPrice " +
                "FROM po p " +
                "JOIN employee e ON p.CreatedBy = e.EmployeeID " +
                "JOIN supplier s ON p.SupplierID = s.SupplierID " +
                "WHERE p.Status IN ('Confirmed', 'Processing', 'Done') " +
                "ORDER BY FIELD(p.Status, 'Confirmed', 'Processing', 'Done'), p.CreatedDate DESC";

        List<PurchaseOrder> purchaseOrders = new ArrayList<>();

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PurchaseOrder purchaseOrder = new PurchaseOrder(
                        rs.getString("POID"),
                        rs.getDate("CreatedDate"),
                        rs.getString("SupplierName"),  // Lấy tên supplier thay vì ID
                        rs.getString("EmployeeName"),  // Lấy tên employee thay vì ID
                        rs.getString("Status"),
                        rs.getFloat("TotalPrice")
                );
                purchaseOrders.add(purchaseOrder);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving purchase orders", e);
        }

        return purchaseOrders;
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
        String sql = "SELECT p.POID, p.CreatedDate, s.SupplierName, e.EmployeeName, p.Status, p.TotalPrice " +
                "FROM po p " +
                "JOIN employee e ON p.CreatedBy = e.EmployeeID " +
                "JOIN supplier s ON p.SupplierID = s.SupplierID " +
                "WHERE p.POID LIKE ? OR p.CreatedDate LIKE ? OR s.SupplierName LIKE ? " +
                "OR p.TotalPrice LIKE ? OR e.EmployeeName LIKE ? OR p.Status LIKE ? " +
                "ORDER BY FIELD(p.Status, 'Pending', 'Confirmed', 'Processing', 'Cancel', 'Done'), p.CreatedDate DESC";

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
                        rs.getString("SupplierName"),  // Lấy tên supplier thay vì ID
                        rs.getString("EmployeeName"),  // Lấy tên employee thay vì ID
                        rs.getString("Status"),
                        rs.getFloat("TotalPrice")
                );
                purchaseOrders.add(purchaseOrder);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching purchase orders", e);
        }

        return purchaseOrders;
    }

    public List<PurchaseOrder> searchDO(String searchQuery) {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        String sql = "SELECT p.POID, p.CreatedDate, s.SupplierName, e.EmployeeName, p.Status, p.TotalPrice " +
                "FROM po p " +
                "JOIN employee e ON p.CreatedBy = e.EmployeeID " +
                "JOIN supplier s ON p.SupplierID = s.SupplierID " +
                "WHERE (p.POID LIKE ? OR p.CreatedDate LIKE ? OR s.SupplierName LIKE ? " +
                "OR p.TotalPrice LIKE ? OR e.EmployeeName LIKE ? OR p.Status LIKE ?) " +
                "AND p.Status IN ('Confirmed', 'Processing', 'Done') " +  // Thêm điều kiện lọc Status
                "ORDER BY FIELD(p.Status, 'Confirmed', 'Processing', 'Done'), p.CreatedDate DESC";

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
                        rs.getString("SupplierName"),  // Lấy tên supplier thay vì ID
                        rs.getString("EmployeeName"),  // Lấy tên employee thay vì ID
                        rs.getString("Status"),
                        rs.getFloat("TotalPrice")
                );
                purchaseOrders.add(purchaseOrder);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching delivery orders", e);
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
