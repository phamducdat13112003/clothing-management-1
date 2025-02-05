package org.example.clothingmanagement.service;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.repository.DBContext;

public class CategoryDAO {



    // Phương thức lấy tất cả Category từ cơ sở dữ liệu
    public static List<Category> selectAll()  {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";

        // Thiết lập kết nối và thực thi truy vấn
        try (Connection connection = DBContext.getConnection();  // Sử dụng Context.getConnection()
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Duyệt qua kết quả truy vấn và thêm vào danh sách categories
            while (rs.next()) {
                categories.add(new Category(rs.getInt("categoryID"), rs.getString("categoryName"),
                        rs.getDate("createdDate"),rs.getInt("createdBy")));
            }
        }catch (Exception e){

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
            pstmt.setDate(2,new java.sql.Date(category.getCreatedDate().getTime()));
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
    public static List<Category> filterCategories(Integer categoryId, String categoryName, Date createDate, Integer createdBy) throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT categoryID, categoryName, createdDate, createdBy FROM category WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (categoryId != null) {
            sql += " AND categoryID = ?";
            params.add(categoryId);
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql += " AND categoryName LIKE ?";
            params.add("%" + categoryName + "%");
        }
        if (createDate != null) {
            sql += " AND createdDate = ?";
            params.add(new java.sql.Date(createDate.getTime()));
        }
        if (createdBy != null) {
            sql += " AND createdBy = ?";
            params.add(createdBy);
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







    }

