package org.example.clothingmanagement.service;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.repository.DBContext;

public class CategoryDAO {

    public static List<Category> selectAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/warehousemanagement", "root", "");
             PreparedStatement pt = conn.prepareStatement(sql);
             ResultSet rs = pt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setCreatedDate(rs.getDate("createdDate"));
                category.setCreatedBy(rs.getInt("createdBy"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Phương thức lấy xóa Category theo id từ cơ sở dữ liệu
    public static void deleteByIDCategories(int CategoryID) throws SQLException {

    }

    // Phương thức cập nhật Category theo ID
    public static void updateByID(Category category) {
        String sql = "UPDATE category SET categoryName = ?, createdDate = ?, createdBy = ? WHERE categoryID = ?";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/warehousemanagement", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getCategoryName());
            pstmt.setDate(2, new java.sql.Date(category.getCreatedDate().getTime()));
            pstmt.setInt(3, category.getCreatedBy());
            pstmt.setInt(4, category.getCategoryID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Phương thức lấy 1 Category từ cơ sở dữ liệu dựa trên ID
    public static Category selectByID(int categoryID) {
        String sql = "SELECT * FROM category WHERE categoryID = ?";
        Category category = null;

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/warehousemanagement", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    category = new Category();
                    category.setCategoryID(rs.getInt("categoryID"));
                    category.setCategoryName(rs.getString("categoryName"));
                    category.setCreatedDate(rs.getDate("createdDate"));
                    category.setCreatedBy(rs.getInt("createdBy"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }


    // Phương thức tạo mới một Category
    public static void createCategory(Category category) {
        String sql = "INSERT INTO category (categoryName, createdDate, createdBy) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/warehousemanagement", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getCategoryName());
            pstmt.setDate(2, new java.sql.Date(category.getCreatedDate().getTime()));
            pstmt.setInt(3, category.getCreatedBy());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


/*cần hoàn thành 4 chức năng:
add, update, delete, view
view: đẫ làm xong view cơ bản, tuy nhiên bây giờ phải lọc view theo từng loại:
date, tên, id, người,tạo
với create phải tạo category khi mà không cần sử dụng toiws createdDate, createBy
với delete phải kiểm tra xem category nào có product thì không được phép xóa
với create thì phải xem có vi phạm nguyeen tắc nào hay không
nư vậy phải chú ý đến view( view theo field và product) và delete kiểm tra xem
có dính gì đê product

* */



    // Phương thức main để in dữ liệu
    public static void main(String[] args) {
        // Gọi phương thức selectAll để lấy danh sách category
        List<Category> categories = selectAll();

        // In thông tin của mỗi category
        for (Category category : categories) {
            System.out.println(category);
        }
    }
}

