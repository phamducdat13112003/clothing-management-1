package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.clothingmanagement.repository.DBContext.getConnection;

public class InventoryDocDAO {

    public List<InventoryDoc> getAllInventoryDocs() {
        List<InventoryDoc> inventoryDocs = new ArrayList<>();
        String sql = "SELECT InventoryDocID, CreatedBy, CreatedDate, BinID, Status " +
                "FROM inventorydoc ORDER BY InventoryDocID  DESC"; // Sắp xếp theo ngày tạo mới nhất


        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InventoryDoc inventoryDoc = new InventoryDoc();
                inventoryDoc.setInventoryDocId(rs.getString("InventoryDocID"));
                inventoryDoc.setCreatedBy(rs.getString("CreatedBy"));
                inventoryDoc.setCreatedDate(rs.getDate("CreatedDate"));
                inventoryDoc.setBinId(rs.getString("BinID"));
                inventoryDoc.setStatus(rs.getString("Status"));
                inventoryDocs.add(inventoryDoc);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log lỗi hoặc xử lý tùy ý
        }

        return inventoryDocs;
    }

    public static List<BinDetail> getBinDetailByBinID(String binID) {
        List<BinDetail> list = new ArrayList<>();
        String sql = "SELECT bd.BinDetailID, bd.BinID, bd.ProductDetailID, bd.Quantity, " +
                "       b.BinName, b.MaxCapacity, b.Status AS BinStatus, b.SectionID, " +
                "       pd.Quantity AS PD_Quantity, pd.Weight, pd.Color, pd.Size, pd.ProductImage, pd.ProductID, pd.Status " +
                "FROM BinDetail bd " +
                "JOIN Bin b ON bd.BinID = b.BinID " +
                "JOIN ProductDetail pd ON bd.ProductDetailID = pd.ProductDetailID " +
                "WHERE bd.BinID = ? " +
                "ORDER BY b.Status DESC, bd.BinID ASC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BinDetail binDetail = new BinDetail();
                binDetail.setBinDetailId(rs.getString("BinDetailID"));
                binDetail.setBinId(rs.getString("BinID"));
                binDetail.setProductDetailId(rs.getString("ProductDetailID"));
                binDetail.setQuantity(rs.getInt("Quantity"));
                binDetail.setStatus(rs.getInt("Status"));
                binDetail.setColor(rs.getString("Color"));
                binDetail.setSize(rs.getString("Size"));
                binDetail.setImage(rs.getString("ProductImage"));
                list.add(binDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void createInventoryDoc(String inventoryDocID,String createdBy, Date createdDate, String binID, String status) throws SQLException {
        String sql = "INSERT INTO inventorydoc (InventoryDocID, CreatedBy, CreatedDate, BinID, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inventoryDocID);
            stmt.setString(2, createdBy);
            stmt.setDate(3, createdDate);
            stmt.setString(4, binID);
            stmt.setString(5, status);
            stmt.executeUpdate();
        }
    }

    public static String generateInventoryDocID() throws SQLException {
        String sql = "SELECT MAX(InventoryDocID) FROM inventorydoc WHERE InventoryDocID LIKE 'INV%'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                String lastID = rs.getString(1); // Lấy ID lớn nhất hiện tại
                int num = Integer.parseInt(lastID.substring(3)); // Bỏ "INV" và lấy số
                return String.format("INV%05d", num + 1); // Tăng lên 1 và định dạng lại
            }
        }
        return "INV00001"; // Trường hợp chưa có dữ liệu
    }

    public static void updateInventoryDocStatus(String inventoryDocId, String newStatus) {
        String sql = "UPDATE inventorydoc SET Status = ? WHERE InventoryDocID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setString(2, inventoryDocId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cập nhật trạng thái thành công!");
            } else {
                System.out.println("Không tìm thấy InventoryDocID để cập nhật.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public static List<SectionType> getAllSectionTypes() {
        List<SectionType> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM sectiontype";
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new SectionType(
                        rs.getInt("SectionTypeID"),
                        rs.getString("SectionTypeName"),
                        rs.getString("WarehouseID"),
                        rs.getString("Description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Section> getSectionsByType(int sectionTypeID) {
        List<Section> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM section WHERE SectionTypeID = ?";
            Connection conn = DBContext.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, sectionTypeID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Section(
                        rs.getString("SectionID"),
                        rs.getString("SectionName"),
                        rs.getInt("SectionTypeID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Bin> getBinsBySection(String sectionID) {
        List<Bin> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM bin WHERE SectionID = ?";
            Connection conn = DBContext.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sectionID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Bin(
                        rs.getString("BinID"),
                        rs.getString("BinName"),
                        rs.getDouble("MaxCapacity"),
                        rs.getBoolean("Status"),
                        rs.getString("SectionID"),
                        null,  // currentCapacity (Không có trong DB)
                        null   // availableCapacity (Không có trong DB)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void changeBinStatus(String binID, int status) {
        String sql = "UPDATE bin SET Status = ? WHERE BinID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, status);
            stmt.setString(2, binID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean canAddProductsToBin(String binId, List<String> productDetailIds, List<Integer> quantities) {
        String maxCapacityQuery = "SELECT MaxCapacity FROM bin WHERE BinID = ?";
        String existingWeightQuery = "SELECT SUM(pd.Weight * bd.Quantity) FROM bindetail bd " +
                "JOIN productdetail pd ON bd.ProductDetailId = pd.ProductDetailID " +
                "WHERE bd.BinID = ? AND bd.ProductDetailId NOT IN (";
        String productWeightQuery = "SELECT Weight FROM productdetail WHERE ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement maxCapStmt = conn.prepareStatement(maxCapacityQuery);
             PreparedStatement productWeightStmt = conn.prepareStatement(productWeightQuery)) {

            // Lấy giá trị MaxCapacity của Bin
            maxCapStmt.setString(1, binId);
            ResultSet rs = maxCapStmt.executeQuery();
            if (!rs.next()) return false;
            double maxCapacity = rs.getDouble(1);

            // Tính tổng trọng lượng sản phẩm mới được thêm vào (x)
            double x = 0;
            for (int i = 0; i < productDetailIds.size(); i++) {
                productWeightStmt.setString(1, productDetailIds.get(i));
                ResultSet weightRs = productWeightStmt.executeQuery();
                if (weightRs.next()) {
                    double weight = weightRs.getDouble(1);
                    x += weight * quantities.get(i);
                }
            }

            // Tạo câu truy vấn động để lấy tổng trọng lượng sản phẩm cũ (y)
            StringBuilder queryBuilder = new StringBuilder(existingWeightQuery);
            for (int i = 0; i < productDetailIds.size(); i++) {
                queryBuilder.append("?");
                if (i < productDetailIds.size() - 1) queryBuilder.append(", ");
            }
            queryBuilder.append(")");

            try (PreparedStatement existingWeightStmt = conn.prepareStatement(queryBuilder.toString())) {
                existingWeightStmt.setString(1, binId);
                for (int i = 0; i < productDetailIds.size(); i++) {
                    existingWeightStmt.setString(i + 2, productDetailIds.get(i));
                }
                ResultSet existingWeightRs = existingWeightStmt.executeQuery();
                double y = existingWeightRs.next() ? existingWeightRs.getDouble(1) : 0;

                // Kiểm tra điều kiện x + y <= z
                return (x + y) <= maxCapacity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateProductDetailQuantity(List<String> productDetailIds, List<Integer> differenceQuantities) {
        String sql = "UPDATE productdetail SET Quantity = Quantity + ? WHERE ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < productDetailIds.size(); i++) {
                pstmt.setInt(1, differenceQuantities.get(i)); // Cộng số lượng chênh lệch vào Quantity hiện tại
                pstmt.setString(2, productDetailIds.get(i));  // Chọn ProductDetailID cần cập nhật
                pstmt.addBatch(); // Thêm vào batch để tối ưu
            }

            pstmt.executeBatch(); // Thực thi batch update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getProductDetailToProductNameMap() {
        Map<String, String> productDetailMap = new HashMap<>();
        String sql = "SELECT pd.ProductDetailID, p.ProductName " +
                "FROM productdetail pd " +
                "JOIN product p ON pd.ProductID = p.ProductID";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productDetailMap.put(rs.getString("ProductDetailID"), rs.getString("ProductName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDetailMap;
    }

    public static Map<String, String> getCreatedByToEmployeeNameMap() {
        Map<String, String> createdByMap = new HashMap<>();
        String sql = "SELECT i.CreatedBy, e.EmployeeName " +
                "FROM inventorydoc i " +
                "JOIN employee e ON i.CreatedBy = e.EmployeeID";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                createdByMap.put(rs.getString("CreatedBy"), rs.getString("EmployeeName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return createdByMap;
    }

    public static void updateBinDetails(String binID, List<String> productDetailIDs, List<Integer> quantities) throws SQLException {
        if (productDetailIDs == null || quantities == null || productDetailIDs.size() != quantities.size()) {
            throw new IllegalArgumentException("Danh sách ProductDetailID và quantity không hợp lệ.");
        }

        String updateSQL = "UPDATE bindetail SET quantity = ? WHERE binId = ? AND ProductDetailId = ?";
        String deleteSQL = "DELETE FROM bindetail WHERE binId = ? AND ProductDetailId = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {

            conn.setAutoCommit(false); // Bắt đầu transaction

            for (int i = 0; i < productDetailIDs.size(); i++) {
                String productDetailID = productDetailIDs.get(i);
                int quantity = quantities.get(i);

                if (quantity == 0) {
                    // Nếu quantity = 0, xóa bản ghi
                    deleteStmt.setString(1, binID);
                    deleteStmt.setString(2, productDetailID);
                    deleteStmt.addBatch();
                } else {
                    // Ngược lại, cập nhật quantity
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, binID);
                    updateStmt.setString(3, productDetailID);
                    updateStmt.addBatch();
                }
            }

            // Thực thi batch update và delete
            deleteStmt.executeBatch();
            updateStmt.executeBatch();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static List<Employee> getAllEmployee() {
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT e.employeeId, e.employeeName, e.email, e.phone, e.address, e.gender, ");
            sql.append(" e.dob, e.status, e.image, e.warehouseId ");
            sql.append(" FROM employee e ");
            sql.append(" JOIN account a ON e.employeeId = a.employeeId "); // JOIN với account để lấy roleId
            sql.append(" WHERE e.warehouseId = 'W001' AND a.roleId = 4 "); // Lọc theo roleId = 4
            sql.append(" ORDER BY e.employeeId");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<Employee> employees = new ArrayList<>();

            while (rs.next()) {
                Employee employee = Employee.builder()
                        .employeeID(rs.getString("EmployeeID"))
                        .employeeName(rs.getString("EmployeeName"))
                        .email(rs.getString("Email"))
                        .phone(rs.getString("Phone"))
                        .address(rs.getString("Address"))
                        .gender(rs.getBoolean("Gender"))
                        .dob(LocalDate.parse(rs.getString("Dob")))
                        .status(rs.getString("Status"))
                        .warehouseID(rs.getString("WarehouseID"))
                        .image(rs.getString("Image"))
                        .build();
                employees.add(employee);
            }
            return employees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Bin> getBinsBySectionWithStatusOne(String sectionID) {
        List<Bin> list = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT b.* FROM bin b " +
                    "JOIN bindetail bd ON b.BinID = bd.BinID " +  // Chỉ lấy bin có trong bindetail
                    "WHERE b.SectionID = ? AND b.Status = 1";

            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sectionID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Bin(
                        rs.getString("BinID"),
                        rs.getString("BinName"),
                        rs.getDouble("MaxCapacity"),
                        rs.getBoolean("Status"),
                        rs.getString("SectionID"),
                        null,  // currentCapacity (Không có trong DB)
                        null   // availableCapacity (Không có trong DB)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<InventoryDoc> searchInventoryDoc(String inventoryDocID, String binID) {
        List<InventoryDoc> inventoryDocs = new ArrayList<>();
        String sql = "SELECT * FROM inventorydoc WHERE InventoryDocID = ? or BinID = ? ORDER BY InventoryDocID  DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inventoryDocID);
            stmt.setString(2, binID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InventoryDoc doc = new InventoryDoc(
                            rs.getString("InventoryDocID"),
                            rs.getString("CreatedBy"),
                            rs.getDate("CreatedDate"),
                            rs.getString("BinID"),
                            rs.getString("Status")
                    );
                    inventoryDocs.add(doc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryDocs;
    }






}
