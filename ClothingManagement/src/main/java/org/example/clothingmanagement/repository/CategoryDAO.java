package org.example.clothingmanagement.repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.clothingmanagement.entity.*;

public class CategoryDAO {


    // Phương thức lấy tất cả Category từ cơ sở dữ liệu
    public List<Category> selectAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";

        // Thiết lập kết nối và thực thi truy vấn
        try (Connection connection = DBContext.getConnection();  // Sử dụng Context.getConnection()
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Duyệt qua kết quả truy vấn và thêm vào danh sách categories
            while (rs.next()) {
                categories.add(new Category(rs.getInt("categoryID"), rs.getString("categoryName"),
                        rs.getDate("createdDate"), rs.getString("createdBy")));
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
            pstmt.setString(3, category.getCreatedBy());

            pstmt.executeUpdate();
        }catch(Exception e){

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
            pstmt.setString(3, category.getCreatedBy());
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
                            rs.getDate("createdDate"), rs.getString("createdBy"));
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

    public static String getEmployeeIDByName(String name) throws SQLException {
        String sql = "SELECT employeeID FROM Employee WHERE employeeName = ?";
        try (Connection connection = DBContext.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("employeeID");
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }


    public static List<Category> filterCategories(String categoryName, Date startDate, Date endDate, String createdBy) throws SQLException {
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
        if (createdBy != null && !createdBy.trim().isEmpty()) {
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
                            rs.getString("createdBy")
                    ));
                }
            }
        }
        return categories;
    }


    public String getEmployeeNameByCreatedBy(String createdBy) {
        String query = "SELECT EmployeeName FROM employee WHERE EmployeeID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, createdBy);
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

    public String getEmployeeIDByAccountID(String accountID) {
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


    public List<String> validateCategoryName(String name) throws SQLException {
        List<String> errors = new ArrayList<>();
        try{
        if (name == null || name.trim().isEmpty()) {
            errors.add("Tên danh mục không được để trống.");
            return errors;
        }

        // Chuẩn hóa khoảng trắng: loại bỏ khoảng trắng dư thừa
        name = name.replaceAll("\\s+", " ").trim();

        // Tự động viết hoa chữ cái đầu
        if (!name.isEmpty()) {
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1).replace(" ", "").replace("-", "");
        }

        // Kiểm tra độ dài không quá 20 ký tự (không tính khoảng trắng và dấu '-')
        String nameWithoutSpaces = name.replace(" ", "").replace("-", "");
        if (nameWithoutSpaces.length() > 20) {
            errors.add("Tên danh mục không được dài quá 20 ký tự (không tính khoảng trắng và dấu '-').");
        }

        // Kiểm tra chỉ chứa chữ cái, số, khoảng trắng và dấu '-'
        if (!name.matches("^[a-zA-Z0-9À-ỹ\s-]+$")) {
            errors.add("Tên danh mục chỉ được chứa chữ cái, số, khoảng trắng và dấu '-'.");
        }

        if (checkCategoryNameExist(name)) {
            errors.add("Tên danh mục này đã tồn tại hihi.");
        }
           return errors;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter thẻ name: ");
        String testName = scanner.nextLine();
        CategoryDAO dao = new CategoryDAO();
        List<String> errors =dao.validateCategoryName(testName);

        if (errors.isEmpty()) {
            System.out.println("Tên danh mục hợp lệ: " + testName);
        } else {
            errors.forEach(System.out::println);
        }


    }



}

