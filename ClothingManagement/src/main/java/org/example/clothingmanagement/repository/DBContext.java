package org.example.clothingmanagement.repository;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/warehousemanagement?useSSL=false&serverTimezone=UTC";
    private static final String username = "root"; // Tên người dùng mặc định của XAMPP
    private static final String password = ""; // Mật khẩu mặc định của XAMPP (rỗng)

    public static Connection getConnection() throws SQLException {
        try {
            // Tải driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(jdbcURL, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver không tìm thấy", e);
        }
    }
}