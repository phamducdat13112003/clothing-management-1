package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.*;


import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class DeliveryOrderDAO {

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

//9 chuyển đổi dữ liệu lấy dodetail chuyển thành những cái cần thiêt
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
     //AccountID thành empployeID cho nhập dữ liệu vào DO
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
    String query = "SELECT DOID, PlannedShippingDate, ReceiptDate, POID, CreatedBy, Recipient " +
            "FROM DO WHERE Status = False"; // Thêm điều kiện lọc

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

    ///////////////////////////////////////////////////////////////////////////////////////////////////
// lấy các supplier có trong database
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
// filter đe lay po
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


    public static List<PurchaseOrderDetail> getPODetailsByPOID(String poID) {
        List<PurchaseOrderDetail> poDetails = new ArrayList<>();

        if (poID == null || poID.isEmpty()) {
            return poDetails; // Trả về danh sách rỗng nếu không có POID nào
        }

        String query = "SELECT POdetailID, POID, ProductDetailID, Quantity, Price, TotalPrice " +
                "FROM POdetail WHERE POID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, poID); // Chỉ truyền vào một POID

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

    public static Map<String, Object> getProductDetailByProductDetailID(String productDetailID) {
        Map<String, Object> productDetail = new HashMap<>();

        if (productDetailID == null || productDetailID.isEmpty()) {
            return productDetail; // Trả về map rỗng nếu không có productDetailID hợp lệ
        }

        String query = "SELECT pd.ProductDetailID, p.ProductName, p.Gender, p.Seasons, p.Material, " +
                "pd.Weight, pd.Color, pd.Size, p.Price " +
                "FROM productdetail pd " +
                "JOIN product p ON pd.ProductID = p.ProductID " +
                "WHERE pd.ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Gán giá trị cho tham số "?"
            ps.setString(1, productDetailID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Lấy dữ liệu của productDetailID đầu tiên tìm thấy
                    productDetail.put("ProductDetailID", rs.getString("ProductDetailID"));
                    productDetail.put("ProductName", rs.getString("ProductName"));
                    productDetail.put("Gender", rs.getString("Gender"));
                    productDetail.put("Seasons", rs.getString("Seasons"));
                    productDetail.put("Material", rs.getString("Material"));
                    productDetail.put("Weight", rs.getDouble("Weight"));
                    productDetail.put("Color", rs.getString("Color"));
                    productDetail.put("Size", rs.getString("Size"));
                    productDetail.put("Price", rs.getBigDecimal("Price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetail;
    }
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

    public boolean isDOIDExist(String doID) {
        String sql = "SELECT COUNT(*) FROM DO WHERE DOID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu số lượng > 0 thì DOID đã tồn tại
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isValidDate(String poId, Date dateToCheck) {
        String sql = "SELECT CreatedDate FROM PO WHERE POID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, poId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date poCreatedDate = rs.getDate("CreatedDate");
                Date today = new Date(System.currentTimeMillis());

                // Kiểm tra điều kiện ngày
                return !(dateToCheck.before(today) || dateToCheck.before(poCreatedDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Lỗi hoặc POID không tồn tại
    }

    public static List<DeliveryOrder> filterDOs(String supplierID, String startDate, String endDate, String poID, String createdBy) {
        List<DeliveryOrder> doList = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT d.DOID, d.PlannedShippingDate, d.ReceiptDate, d.POID, d.CreatedBy, d.Recipient, d.Status " +
                "FROM DO d " +
                "JOIN PO p ON d.POID = p.POID " +
                "WHERE d.Status = TRUE "); // Thêm điều kiện chỉ lấy DO có status = true

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

    public static String getEmployeeIDByName(String employeeName) {
        String employeeID = null;
        String query = "SELECT EmployeeID FROM employee WHERE EmployeeName = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, employeeName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employeeID = rs.getString("EmployeeID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeID;
    }

    public static Map<String, Object> getDODetailsByDOID(String doID) {
        Map<String, Object> productDetail = null;

        if (doID == null || doID.isEmpty()) {
            return null; // Trả về null nếu không có doID hợp lệ
        }

        String query = "SELECT pd.ProductDetailID, p.ProductName, p.Gender, p.Seasons, p.Material, " +
                "pd.Weight, pd.Color, pd.Size, p.Price " +
                "FROM DODetail dd " +
                "JOIN productdetail pd ON dd.ProductDetailID = pd.ProductDetailID " +
                "JOIN product p ON pd.ProductID = p.ProductID " +
                "WHERE dd.DOID = ? LIMIT 1"; // Lấy 1 sản phẩm duy nhất

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Lấy đúng 1 sản phẩm đầu tiên
                    productDetail = new HashMap<>();
                    productDetail.put("ProductDetailID", rs.getString("ProductDetailID"));
                    productDetail.put("ProductName", rs.getString("ProductName"));
                    productDetail.put("Gender", rs.getString("Gender"));
                    productDetail.put("Seasons", rs.getString("Seasons"));
                    productDetail.put("Material", rs.getString("Material"));
                    productDetail.put("Weight", rs.getDouble("Weight"));
                    productDetail.put("Color", rs.getString("Color"));
                    productDetail.put("Size", rs.getString("Size"));
                    productDetail.put("Price", rs.getBigDecimal("Price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetail;
    }
    public static void updateDO(String doID, String receiptDate, String recipient, Boolean setStatusFalse) throws SQLException {
        String sql = "UPDATE DO SET ReceiptDate = ?, Recipient = ?, Status = ? WHERE DOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(receiptDate));
            stmt.setString(2, recipient);
            stmt.setBoolean(3, setStatusFalse); // true hoặc false
            stmt.setString(4, doID);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(); // Thêm xử lý ngoại lệ
        }
    }


    // Cập nhật số lượng trong DODetail
    public static void updateDODetailQuantity(String productDetailID, int newQuantity) throws SQLException {
        String sql = "UPDATE DODetail SET Quantity = ? WHERE ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, productDetailID);
            stmt.executeUpdate();
        }catch(Exception e) {}
    }


    public static int getDOQuantity(String doID, String productDetailID) {
        int quantity = 0;
        String query = "SELECT Quantity FROM DODetail WHERE DOID = ? AND ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doID);
            ps.setString(2, productDetailID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("Quantity"); // Lấy số lượng từ bảng DODetail
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quantity;
    }

    public static String getProductDetailIDByPOIDAndDOID(String poID, String doID) {
        String productDetailID = null;
        String sql = "SELECT dd.ProductDetailID " +
                "FROM DODetail dd " +
                "JOIN DO d ON dd.DOID = d.DOID " +
                "WHERE d.POID = ? AND dd.DOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, poID);
            stmt.setString(2, doID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // Chỉ lấy giá trị đầu tiên
                productDetailID = rs.getString("ProductDetailID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productDetailID;
    }






    public static void main(String[] args) {
            // Giả sử productDetailID có sẵn trong database
            String productDetailID = "PD001"; // Thay bằng ID hợp lệ trong DB

            // Kiểm tra số lượng còn lại
            int remainingQuantity = getRemainingQuantity(productDetailID);
            System.out.println("Số lượng còn lại của sản phẩm " + productDetailID + " là: " + remainingQuantity);

            // Kiểm tra tính hợp lệ của tổng số lượng DO
            boolean isValid = isDOQuantityValid(productDetailID);
            System.out.println("Tổng số lượng DO có hợp lệ không: " + isValid);
        }


}
