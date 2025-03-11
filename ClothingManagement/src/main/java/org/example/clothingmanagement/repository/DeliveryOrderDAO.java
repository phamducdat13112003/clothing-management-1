package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.*;


import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class DeliveryOrderDAO {
    
    //Tạo DOID mới tự động
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

    // Lấy tất cả DO
    public static List<DeliveryOrder> getAllDOs() {
        List<DeliveryOrder> doList = new ArrayList<>();
        String query = "SELECT DOID, PlannedShippingDate, ReceiptDate, POID, CreatedBy, Recipient, Status FROM DO";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DeliveryOrder dod = new DeliveryOrder();
                dod.setDoID(rs.getString("doID"));
                dod.setPlannedShippingDate(rs.getDate("PlannedShippingDate"));
                dod.setReceiptDate(rs.getDate("ReceiptDate"));
                dod.setPoID(rs.getString("poID"));
                dod.setCreatedBy(rs.getString("CreatedBy"));
                dod.setRecipient(rs.getString("Recipient"));
                dod.setStatus(rs.getBoolean("Status")); // Thêm trường Status vào đối tượng

                doList.add(dod);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doList;
    }

    // Lấy tất cả Supplier
    public static List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT SupplierID, SupplierName FROM supplier WHERE Status = 1";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getString("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    // Chuyển các employeeId thành employeeName
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

    // Chuyển các SupplierId thành SupplierName
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

    // Lấy danh sách PODetail thành POID
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

    // Đổi AccountID thành EmployeeId
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

    // Lấy POs theo yêu cầu
    public static List<PurchaseOrder> filterPO(String supplierID, String startDate, String endDate, String poid) {
        List<PurchaseOrder> poList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM PO WHERE Status = 'Open'");

        // Thêm điều kiện nếu có giá trị
        if (supplierID != null && !supplierID.isEmpty()) {
            sql.append(" AND SupplierID = ?");
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND CreatedDate >= ?");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND CreatedDate <= ?");
        }
        if (poid != null && !poid.isEmpty()) {
            sql.append(" AND POID = ?");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (supplierID != null && !supplierID.isEmpty()) {
                stmt.setString(paramIndex++, supplierID);
            }
            if (startDate != null && !startDate.isEmpty()) {
                stmt.setDate(paramIndex++, Date.valueOf(startDate));
            }
            if (endDate != null && !endDate.isEmpty()) {
                stmt.setDate(paramIndex++, Date.valueOf(endDate));
            }
            if (poid != null && !poid.isEmpty()) {
                stmt.setString(paramIndex++, poid);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PurchaseOrder po = new PurchaseOrder(
                        rs.getString("POID"),
                        rs.getDate("CreatedDate"),
                        rs.getString("SupplierID"),
                        rs.getString("CreatedBy"),
                        rs.getString("Status"),
                        rs.getInt("TotalPrice")
                );
                poList.add(po);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poList;
    }

    // Lấy các thông tin product từ productdetailID
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

    // lọc các con số còn lại của PO
    public static int getRemainingQuantity(String productDetailID) {
        int remainingQuantity = 0;
        String query = "SELECT (p.Quantity - COALESCE(SUM(d.Quantity), 0)) AS RemainingQuantity " +
                "FROM POdetail p " +
                "LEFT JOIN DODetail d ON p.ProductDetailID = d.ProductDetailID " +
                "WHERE p.ProductDetailID = ? " +
                "GROUP BY p.Quantity";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, productDetailID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    remainingQuantity = rs.getInt("RemainingQuantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remainingQuantity;
    }

    // lấy các DO đang hoạt
    public static boolean isDOQuantityValid(String productDetailID) {
        String query = "SELECT (COALESCE(SUM(d.Quantity), 0) = p.Quantity) AS IsValid " +
                "FROM POdetail p " +
                "LEFT JOIN DODetail d ON p.ProductDetailID = d.ProductDetailID " +
                "WHERE p.ProductDetailID = ? " +
                "GROUP BY p.Quantity";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, productDetailID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("IsValid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // thêm do cho purchase order
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

    // tìm các DO
    public static List<DeliveryOrder> filterDOs(String supplierID, String startDate, String endDate, String poID, String createdBy) {
        List<DeliveryOrder> doList = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT d.DOID, d.PlannedShippingDate, d.ReceiptDate, d.POID, d.CreatedBy, d.Recipient, d.Status " +
                "FROM DO d " +
                "JOIN PO p ON d.POID = p.POID "); // Thêm điều kiện chỉ lấy DO có status = true

        // Map điều kiện vào câu truy vấn
        if (supplierID != null && !supplierID.isEmpty()) {
            query.append("AND p.SupplierID = ? ");
        }
        if (startDate != null && !startDate.trim().isEmpty()) {
            query.append("AND d.PlannedShippingDate >= ? ");
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            query.append("AND d.PlannedShippingDate <= ? ");
        }
        if (poID != null && !poID.isEmpty()) {
            query.append("AND d.POID = ? ");
        }
        if (createdBy != null && !createdBy.isEmpty()) {
            query.append("AND d.CreatedBy = ? ");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int index = 1;
            if (supplierID != null && !supplierID.isEmpty()) {
                ps.setString(index++, supplierID);
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                try {
                    ps.setDate(index++, Date.valueOf(startDate.trim()));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                try {
                    ps.setDate(index++, Date.valueOf(endDate.trim()));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            if (poID != null && !poID.isEmpty()) {
                ps.setString(index++, poID);
            }
            if (createdBy != null && !createdBy.isEmpty()) {
                ps.setString(index++, createdBy);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DeliveryOrder dod = new DeliveryOrder();
                    dod.setDoID(rs.getString("DOID"));
                    dod.setPlannedShippingDate(rs.getDate("PlannedShippingDate"));
                    dod.setReceiptDate(rs.getDate("ReceiptDate"));
                    dod.setPoID(rs.getString("POID"));
                    dod.setCreatedBy(rs.getString("CreatedBy"));
                    dod.setRecipient(rs.getString("Recipient"));
                    dod.setStatus(rs.getBoolean("Status"));

                    doList.add(dod);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doList;
    }

    //
    public static boolean isPOFullyReceived(String poID) {
        String sql = "SELECT " +
                "(SELECT SUM(Quantity) FROM PODetail WHERE POID = ?) AS totalOrdered, " +
                "(SELECT SUM(Quantity) FROM DODetail WHERE DOID IN (SELECT DOID FROM DO WHERE POID = ?)) AS totalReceived";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, poID);
            ps.setString(2, poID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int totalOrdered = rs.getInt("totalOrdered");
                int totalReceived = rs.getInt("totalReceived");
                return totalReceived >= totalOrdered; // Nếu số lượng nhập >= số lượng đặt, PO đã hoàn tất
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Map<String, Integer> getRemainingQuantities(String poID) {
        Map<String, Integer> remainingQuantities = new HashMap<>();

        String sql = "SELECT pd.ProductDetailID, pd.Quantity - COALESCE(SUM(dd.Quantity), 0) AS RemainingQuantity " +
                "FROM POdetail pd " +
                "LEFT JOIN DODetail dd ON pd.ProductDetailID = dd.ProductDetailID " +
                "LEFT JOIN DO d ON dd.DOID = d.DOID " +
                "WHERE pd.POID = ? " +
                "GROUP BY pd.ProductDetailID, pd.Quantity";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, poID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String productDetailID = rs.getString("ProductDetailID");
                int remainingQuantity = rs.getInt("RemainingQuantity");
                remainingQuantities.put(productDetailID, remainingQuantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return remainingQuantities;
    }

    public static boolean deleteDOWithDetails(String doID) {
        if (doID == null || doID.isEmpty()) {
            return false;
        }

        Connection conn = null;
        PreparedStatement checkStatusStmt = null;
        PreparedStatement deleteDODetailsStmt = null;
        PreparedStatement deleteDOStmt = null;

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Kiểm tra status của DO
            String checkStatusQuery = "SELECT Status FROM DO WHERE DOID = ?";
            checkStatusStmt = conn.prepareStatement(checkStatusQuery);
            checkStatusStmt.setString(1, doID);
            ResultSet rs = checkStatusStmt.executeQuery();

            if (rs.next() && rs.getBoolean("Status")) {
                return false; // Không xóa nếu status = true
            }

            // Xóa tất cả các DODetail liên quan
            String deleteDODetailsQuery = "DELETE FROM DODetail WHERE DOID = ?";
            deleteDODetailsStmt = conn.prepareStatement(deleteDODetailsQuery);
            deleteDODetailsStmt.setString(1, doID);
            deleteDODetailsStmt.executeUpdate();

            // Xóa DO
            String deleteDOQuery = "DELETE FROM DO WHERE DOID = ?";
            deleteDOStmt = conn.prepareStatement(deleteDOQuery);
            deleteDOStmt.setString(1, doID);
            deleteDOStmt.executeUpdate();

            conn.commit(); // Xác nhận giao dịch
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Quay lại nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (checkStatusStmt != null) checkStatusStmt.close();
                if (deleteDODetailsStmt != null) deleteDODetailsStmt.close();
                if (deleteDOStmt != null) deleteDOStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<DeliveryOrderDetail> getDODetailsByDOID(String doID) {
        List<DeliveryOrderDetail> doDetails = new ArrayList<>();

        if (doID == null || doID.isEmpty()) {
            return doDetails; // Trả về danh sách rỗng nếu DOID không hợp lệ
        }

        String query = "SELECT DODetailID, ProductDetailID, Quantity, DOID FROM DODetail WHERE DOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DeliveryOrderDetail detail = new DeliveryOrderDetail(
                            rs.getString("DODetailID"),
                            rs.getString("ProductDetailID"),
                            rs.getInt("Quantity"),
                            rs.getString("DOID")
                    );
                    doDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doDetails;
    }

    public static boolean updateDOWithDetails(String doID, String plannedShippingDate, String createdBy, List<Map<String, Object>> productDetails) {
        if (doID == null || doID.trim().isEmpty()) {
            return false; // DOID không hợp lệ
        }

        Connection conn = null;
        PreparedStatement psCheckStatus = null;
        PreparedStatement psUpdateDO = null;
        PreparedStatement psDeleteDetails = null;
        PreparedStatement psInsertDetails = null;

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1️⃣ Kiểm tra nếu DO có status = false thì mới được update
            String checkStatusQuery = "SELECT Status FROM DO WHERE DOID = ?";
            psCheckStatus = conn.prepareStatement(checkStatusQuery);
            psCheckStatus.setString(1, doID);
            var rs = psCheckStatus.executeQuery();
            if (!rs.next() || rs.getBoolean("Status")) {
                return false; // Không thể update nếu status = true
            }

            // 2️⃣ Cập nhật thông tin DO
            String updateDOQuery = "UPDATE DO SET PlannedShippingDate = ?, CreatedBy = ? WHERE DOID = ?";
            psUpdateDO = conn.prepareStatement(updateDOQuery);
            psUpdateDO.setString(1, plannedShippingDate);
            psUpdateDO.setString(2, createdBy);
            psUpdateDO.setString(3, doID);
            psUpdateDO.executeUpdate();

            // 3️⃣ Xóa tất cả DODetail cũ của DOID
            String deleteDODetailQuery = "DELETE FROM DODetail WHERE DOID = ?";
            psDeleteDetails = conn.prepareStatement(deleteDODetailQuery);
            psDeleteDetails.setString(1, doID);
            psDeleteDetails.executeUpdate();

            // 4️⃣ Chèn lại các sản phẩm mới trong danh sách productDetails
            String insertDODetailQuery = "INSERT INTO DODetail (DODetailID, ProductDetailID, Quantity, DOID) VALUES (?, ?, ?, ?)";
            psInsertDetails = conn.prepareStatement(insertDODetailQuery);

            for (Map<String, Object> product : productDetails) {
                psInsertDetails.setString(1, (String) product.get("DODetailID")); // ID mới hoặc giữ nguyên
                psInsertDetails.setString(2, (String) product.get("ProductDetailID"));
                psInsertDetails.setInt(3, (int) product.get("Quantity"));
                psInsertDetails.setString(4, doID);
                psInsertDetails.addBatch();
            }
            psInsertDetails.executeBatch(); // Thực thi batch insert

            conn.commit(); // Commit nếu không lỗi
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psCheckStatus != null) psCheckStatus.close();
                if (psUpdateDO != null) psUpdateDO.close();
                if (psDeleteDetails != null) psDeleteDetails.close();
                if (psInsertDetails != null) psInsertDetails.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static DeliveryOrder getDOByID(String doID) {
        DeliveryOrder deliveryOrder = null;
        String query = "SELECT doID, plannedShippingDate, createdBy, status FROM DO WHERE doID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, doID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                deliveryOrder = new DeliveryOrder();
                deliveryOrder.setDoID(rs.getString("doID"));
                deliveryOrder.setPlannedShippingDate(rs.getDate("plannedShippingDate"));
                deliveryOrder.setCreatedBy(rs.getString("createdBy"));
                deliveryOrder.setStatus(rs.getBoolean("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Xử lý lỗi SQL
        }

        return deliveryOrder;
    }

    public boolean updateDOForPS(String doID, String plannedShippingDate, String createdBy) {
            String sql = "UPDATE DO SET PlannedShippingDate = ?, CreatedBy = ? WHERE DOID = ?";
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                // Chuyển đổi plannedShippingDate từ String thành java.sql.Date
                Date sqlDate = Date.valueOf(plannedShippingDate);

                ps.setDate(1, sqlDate);
                ps.setString(2, createdBy);
                ps.setString(3, doID);

                return ps.executeUpdate() > 0; // Trả về true nếu có dòng được cập nhật
            } catch (SQLException | IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }

    public boolean confirmDOForWS(String doID, Date receiptDate, String recipient) {
        String sql = "UPDATE DO SET ReceiptDate = ?, Recipient = ?, Status = ? WHERE DOID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(receiptDate.getTime())); // Chuyển đổi Date sang SQL Date
            ps.setString(2, recipient);
            ps.setBoolean(3, true); // Đánh dấu DO là hoàn thành
            ps.setString(4, doID);
            return ps.executeUpdate() > 0; // Trả về true nếu có dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





    public static void main(String[] args) {
        DeliveryOrder dao = getDOByID("DO1");
            System.out.println(dao);
    }


}
