package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.DeliveryOrder;
import org.example.clothingmanagement.entity.DeliveryOrderDetail;
import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.entity.PurchaseOrderDetail;


import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class DeliveryOrderDAO {
//1
    public static List<PurchaseOrder> getPurchaseOrderOpen() {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        String sql = "SELECT * FROM `po` WHERE Status = 'open';";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                purchaseOrders.add(new PurchaseOrder(
                        rs.getString("POID"),          // Đúng tên cột SQL
                        rs.getDate("CreatedDate"),     // Đúng tên cột SQL
                        rs.getString("SupplierID"),
                        rs.getString("CreatedBy"),
                        rs.getString("Status"),
                        rs.getInt("TotalPrice")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchaseOrders;
    }
//2
    public static String getSupplierNameByID(String supplierID) {
        String query = "SELECT SupplierName FROM supplier WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("SupplierName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Supplier"; // Trả về giá trị mặc định thay vì null
    }
//3
    public static String getEmployeeNameByEmployeeID(String employeeID) {
        String query = "SELECT EmployeeName FROM employee WHERE EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, employeeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("EmployeeName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Employee"; // Trả về giá trị mặc định thay vì null
    }
//4
    public static List<PurchaseOrderDetail> getPODetailsByPOIDs(List<String> poIDs) {
        List<PurchaseOrderDetail> poDetails = new ArrayList<>();

        if (poIDs == null || poIDs.isEmpty()) {
            return poDetails; // Trả về danh sách rỗng nếu không có POID nào
        }

        String query = "SELECT POdetailID, POID, ProductDetailID, Quantity, Price, TotalPrice " +
                "FROM POdetail WHERE POID IN (" +
                poIDs.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Gán giá trị cho các tham số "?"
            for (int i = 0; i < poIDs.size(); i++) {
                ps.setString(i + 1, poIDs.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderDetail detail = new PurchaseOrderDetail(
                            rs.getString("POdetailID"),
                            rs.getString("POID"),
                            rs.getString("ProductDetailID"),
                            rs.getInt("Quantity"),
                            rs.getInt("Price"),
                            rs.getInt("TotalPrice")
                    );
                    poDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return poDetails;
    }
//5
    public static List<Map<String, Object>> getProductDetailsByProductDetailIDs(List<String> productDetailIDs) {
        List<Map<String, Object>> productDetails = new ArrayList<>();

        if (productDetailIDs == null || productDetailIDs.isEmpty()) {
            return productDetails; // Trả về danh sách rỗng nếu không có ProductDetailID nào
        }

        String query = "SELECT pd.ProductDetailID, p.ProductName, p.Gender, p.Seasons, p.Material, " +
                "pd.Weight, pd.Color, pd.Size, p.Price " +
                "FROM productdetail pd " +
                "JOIN product p ON pd.ProductID = p.ProductID " +
                "WHERE pd.ProductDetailID IN (" +
                productDetailIDs.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Gán giá trị cho các tham số "?"
            for (int i = 0; i < productDetailIDs.size(); i++) {
                ps.setString(i + 1, productDetailIDs.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("ProductDetailID", rs.getString("ProductDetailID"));
                    detail.put("ProductName", rs.getString("ProductName"));
                    detail.put("Gender", rs.getString("Gender"));
                    detail.put("Seasons", rs.getString("Seasons"));
                    detail.put("Material", rs.getString("Material"));
                    detail.put("Weight", rs.getDouble("Weight"));
                    detail.put("Color", rs.getString("Color"));
                    detail.put("Size", rs.getString("Size"));
                    detail.put("Price", rs.getBigDecimal("Price"));
                    productDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetails;
    }
//6
    public static boolean addDO(String doID, Date plannedShippingDate, Date receiptDate, String poID, String createdBy, String recipient, boolean status) {
        String query = "INSERT INTO DO (DOID, PlannedShippingDate, ReceiptDate, POID, CreatedBy, Recipient, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doID);
            stmt.setDate(2, new java.sql.Date(plannedShippingDate.getTime()));
            stmt.setDate(3, new java.sql.Date(receiptDate.getTime()));
            stmt.setString(4, poID);
            stmt.setString(5, createdBy);
            stmt.setString(6, recipient);
            stmt.setBoolean(7, status);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
//7
    public static String generateDOID()  {
        String query = "SELECT MAX(DOID) FROM DO";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String maxDOID = rs.getString(1);
                if (maxDOID != null) {
                    int nextID = Integer.parseInt(maxDOID.substring(2)) + 1;
                    return "DO" + nextID;
                }
            }
        }catch(Exception e) {}
        return "DO1"; // Nếu chưa có dữ liệu, bắt đầu từ DO1
    }
    //8
    public static List<DeliveryOrder> getAllActiveDOs() {
        List<DeliveryOrder> doList = new ArrayList<>();
        String query = "SELECT * FROM DO WHERE Status = 1";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DeliveryOrder deliveryOrder = new DeliveryOrder();
                deliveryOrder.setDoID(rs.getString("doID"));
                deliveryOrder.setPlannedShippingDate(rs.getDate("PlannedShippingDate"));
                deliveryOrder.setReceiptDate(rs.getDate("ReceiptDate"));
                deliveryOrder.setPoID(rs.getString("poID"));
                deliveryOrder.setCreatedBy(rs.getString("CreatedBy"));
                deliveryOrder.setRecipient(rs.getString("Recipient"));
                deliveryOrder.setStatus(rs.getBoolean("Status"));

                doList.add(deliveryOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doList;
    }

//9
    public static List<Map<String, Object>> getDODetailsWithProductInfo(List<String> doIDs) {
        List<Map<String, Object>> doDetailList = new ArrayList<>();
        if (doIDs == null || doIDs.isEmpty()) {
            return doDetailList;
        }

        // Tạo danh sách dấu '?' cho PreparedStatement
        String placeholders = String.join(",", Collections.nCopies(doIDs.size(), "?"));
        String sql = "SELECT d.DODetailID, d.ProductDetailID, d.Quantity, d.DOID, " +
                "p.ProductName, p.Gender, p.Seasons, p.Material, p.Price, " +  // Thêm p.Price
                "pd.Weight, pd.Color, pd.Size " +
                "FROM DODetail d " +
                "JOIN productdetail pd ON d.ProductDetailID = pd.ProductDetailID " +
                "JOIN product p ON pd.ProductID = p.ProductID " +
                "WHERE d.DOID IN (" + placeholders + ")";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < doIDs.size(); i++) {
                ps.setString(i + 1, doIDs.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("DODetailID", rs.getString("DODetailID"));
                    detailMap.put("ProductDetailID", rs.getString("ProductDetailID"));
                    detailMap.put("Quantity", rs.getInt("Quantity"));
                    detailMap.put("DOID", rs.getString("DOID"));
                    detailMap.put("ProductName", rs.getString("ProductName"));
                    detailMap.put("Gender", rs.getString("Gender"));
                    detailMap.put("Seasons", rs.getString("Seasons"));
                    detailMap.put("Material", rs.getString("Material"));
                    detailMap.put("Weight", rs.getBigDecimal("Weight"));
                    detailMap.put("Color", rs.getString("Color"));
                    detailMap.put("Size", rs.getString("Size"));
                    detailMap.put("Price", rs.getBigDecimal("Price")); // Lấy giá sản phẩm

                    doDetailList.add(detailMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doDetailList;
    }
    //10
    public static void updateDO(String receiptDate, String Recipient) throws SQLException {
        String sql = "UPDATE DO SET ReceiptDate = ?, Recipient = ?, Status = false WHERE Status = true";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(receiptDate));
            stmt.setString(2, Recipient);
            stmt.executeUpdate();
        }
    }

//11
    // Cập nhật số lượng trong DODetail
    public static void updateDODetailQuantity(String dodetailId, int newQuantity) throws SQLException {
        String sql = "UPDATE DODetail SET Quantity = ? WHERE DODetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, dodetailId);
            stmt.executeUpdate();
        }
    }
    //12
    public static String getEmployeeIDByAccountID(String accountID) {
        String query = "SELECT e.EmployeeID FROM account e WHERE e.AccountID = ? LIMIT 1";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, accountID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("EmployeeID"); // Trả về EmployeeID đầu tiên tìm thấy
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy EmployeeID
    }
//13
    public static List<DeliveryOrder> getAllDOs() {
        List<DeliveryOrder> doList = new ArrayList<>();
        String query = "SELECT DOID, PlannedShippingDate, ReceiptDate, POID, CreatedBy, Recipient FROM DO";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DeliveryOrder dod = new DeliveryOrder();
                dod.setDoID(rs.getString("DOID"));
                dod.setPlannedShippingDate(rs.getDate("PlannedShippingDate"));
                dod.setReceiptDate(rs.getDate("ReceiptDate"));
                dod.setPoID(rs.getString("POID"));
                dod.setCreatedBy(rs.getString("CreatedBy"));
                dod.setRecipient(rs.getString("Recipient"));

                doList.add(dod);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doList;
    }



    public static void main(String[] args) {
        List<DeliveryOrder> doList = DeliveryOrderDAO.getAllDOs();

        if (doList == null || doList.isEmpty()) {
            System.out.println("❌ Test Failed: Không có DO nào được lấy ra hoặc danh sách null.");
        } else {
            System.out.println("✅ Test Passed: Lấy thành công " + doList.size() + " DOs.");
            for (DeliveryOrder dod : doList) {
                System.out.println("DOID: " + dod.getDoID() +
                        ", PlannedShippingDate: " + dod.getPlannedShippingDate() +
                        ", ReceiptDate: " + dod.getReceiptDate() +
                        ", POID: " + dod.getPoID() +
                        ", CreatedBy: " + dod.getCreatedBy() +
                        ", Recipient: " + dod.getRecipient());
            }
        }
    }


}
