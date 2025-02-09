package org.example.clothingmanagement.repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.clothingmanagement.entity.*;

public class CategoryDAO {


    // Phương thức lấy tất cả Category từ cơ sở dữ liệu
    public static List<Category> selectAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";

        // Thiết lập kết nối và thực thi truy vấn
        try (Connection connection = DBContext.getConnection();  // Sử dụng Context.getConnection()
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Duyệt qua kết quả truy vấn và thêm vào danh sách categories
            while (rs.next()) {
                categories.add(new Category(rs.getInt("categoryID"), rs.getString("categoryName"),
                        rs.getDate("createdDate"), rs.getInt("createdBy")));
            }
        } catch (Exception e) {

        }
        return categories;
    }

    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category (categoryName, createdDate, createdBy) VALUES (?, ?, ?)";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, category.getCategoryName());
            pstmt.setDate(2, new java.sql.Date(category.getCreatedDate().getTime()));
            pstmt.setInt(3, category.getCreatedBy());

            pstmt.executeUpdate();
        }
    }

    public void deleteCategory(int categoryID) throws SQLException {
        String sql = "DELETE FROM category WHERE categoryID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, categoryID);
            pstmt.executeUpdate();
        }
    }

    public void updateCategory(Category category) throws SQLException {
        String sql = "UPDATE category SET categoryName = ?, createdDate = ?, createdBy = ? WHERE categoryID = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, category.getCategoryName());
            pstmt.setDate(2, new java.sql.Date(category.getCreatedDate().getTime()));
            pstmt.setInt(3, category.getCreatedBy());
            pstmt.setInt(4, category.getCategoryID());
            pstmt.executeUpdate();
        }
    }

    public Category getCategoryByID(int categoryID) throws SQLException {
        String sql = "SELECT * FROM category WHERE categoryID = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, categoryID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("categoryID"), rs.getString("categoryName"),
                            rs.getDate("createdDate"), rs.getInt("createdBy"));
                }
            }
        }

        return null;
    }

    public boolean checkProductCategory(int categoryID) throws SQLException {
        String sql = "SELECT 1 FROM product WHERE categoryId = ? LIMIT 1";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Trả về true nếu có kết quả, false nếu không có
            }
        }
    }

    public boolean checkCategoryNameExist(String categoryName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Category WHERE categoryName = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Nếu số lượng > 0, tức là đã tồn tại
                }
            }
        }
        return false;
    }

    public static Integer getEmployeeIDByName(String name) throws SQLException {
        String sql = "SELECT employeeID FROM Employee WHERE employeeName = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("employeeID");
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }


    public static List<Category> filterCategories(String categoryName, Date startDate, Date endDate, Integer createdBy) throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT categoryID, categoryName, createdDate, createdBy FROM category WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql += " AND categoryName LIKE ?";
            params.add("%" + categoryName + "%");
        }
        if (startDate != null) {
            sql += " AND createdDate >= ?";
            params.add(new java.sql.Date(startDate.getTime()));
        }
        if (endDate != null) {
            sql += " AND createdDate <= ?";
            params.add(new java.sql.Date(endDate.getTime()));
        }
        if (createdBy != null) {
            sql += " AND createdBy = ?";
            params.add(createdBy);
        }

        // Nếu không có tham số nào, lấy tất cả danh mục
        if (params.isEmpty()) {
            sql = "SELECT categoryID, categoryName, createdDate, createdBy FROM category";
        }

        try (Connection connection = DBContext.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("categoryID"),
                            rs.getString("categoryName"),
                            rs.getDate("createdDate"),
                            rs.getInt("createdBy")
                    ));
                }
            }
        }
        return categories;
    }


    public String getEmployeeNameByCreatedBy(int createdBy) {
        String query = "SELECT EmployeeName FROM employee WHERE EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, createdBy);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("EmployeeName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }

    public Integer getEmployeeIDByAccountID(int accountID) {
        String query = "SELECT e.EmployeeID FROM employee e WHERE e.AccountID = ? LIMIT 1";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accountID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("EmployeeID"); // Trả về EmployeeID đầu tiên tìm thấy
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy EmployeeID
    }





}

