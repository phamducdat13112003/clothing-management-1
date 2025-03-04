package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.repository.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDetailDAO {
    public List<ProductDetail> findAll() {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, status FROM ProductDetail ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<ProductDetail> list = new ArrayList<ProductDetail>();
            while(rs.next()){
                ProductDetail productDetail = ProductDetail.builder()
                        .id(rs.getString("ProductDetailId"))
                        .quantity(rs.getInt("Quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .image(rs.getString("ProductImage"))
                        .productId(rs.getString("ProductId"))
                        .status(rs.getInt("Status"))
                        .build();
                list.add(productDetail);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductDetail> findByProductId(String productId) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status FROM ProductDetail ");
            sql.append(" WHERE ProductId = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            List<ProductDetail> list = new ArrayList<>();
            while(rs.next()){
                ProductDetail productDetail = ProductDetail.builder()
                        .id(rs.getString("ProductDetailId"))
                        .quantity(rs.getInt("Quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .image(rs.getString("ProductImage"))
                        .productId(rs.getString("ProductId"))
                        .status(rs.getInt("Status"))
                        .build();
                list.add(productDetail);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public Optional<ProductDetail> findTheFirstProductDetailOfProductId(String id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId FROM ProductDetail ");
            sql.append(" WHERE ProductId = ? ");
            sql.append(" LIMIT 1 ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ProductDetail productDetail = ProductDetail.builder()
                        .id(rs.getString("ProductDetailId"))
                        .quantity(rs.getInt("Quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .image(rs.getString("ProductImage"))
                        .productId(rs.getString("ProductId"))
                        .build();
                return Optional.of(productDetail);
            }
            else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProductDetail(String id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE ProductDetail ");
            sql.append(" SET status = 0 ");
            sql.append(" WHERE ProductDetailId = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean recoverProductDetail(String id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE ProductDetail ");
            sql.append(" SET status = 1 ");
            sql.append(" WHERE ProductDetailId = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductDetail getProductDetailByProductDetailID(String productDetailID) {
        try (Connection con = DBContext.getConnection()) {
            String sql = "SELECT * FROM `productdetail` WHERE ProductDetailID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, productDetailID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return ProductDetail.builder()
                        .id(rs.getString("ProductDetailID"))
                        .quantity(rs.getInt("Quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .image(rs.getString("ProductImage"))
                        .productId(rs.getString("ProductID"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Optional<ProductDetail> getOptionalProductDetailByProductDetailID(String productDetailID) {
        try(Connection  con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status FROM ProductDetail ");
            sql.append(" WHERE ProductDetailId = ? ");
            PreparedStatement ps =con.prepareStatement(sql.toString());
            ps.setString(1,productDetailID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ProductDetail productDetail = ProductDetail.builder()
                        .id(rs.getString("ProductDetailId"))
                        .quantity(rs.getInt("Quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .image(rs.getString("ProductImage"))
                        .productId(rs.getString("ProductId"))
                        .status(rs.getInt("Status"))
                        .build();
                return Optional.of(productDetail);
            }
            else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getProductIDByProductDetailID(String productDetailID) {
        try (Connection con = DBContext.getConnection()) {
            String sql = "SELECT ProductID FROM `productdetail` WHERE ProductDetailID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, productDetailID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("ProductID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<ProductDetail> getProductDetailByProductId(String productId, int page, int pageSize) {
        List<ProductDetail> productDetails = new ArrayList<>();
        String sql = "SELECT * FROM ProductDetail WHERE ProductID = ? LIMIT ? OFFSET ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // Lặp qua tất cả các dòng kết quả
                ProductDetail productDetail = new ProductDetail();
                productDetail.setId(rs.getString("ProductDetailId"));
                productDetail.setQuantity(rs.getInt("Quantity"));
                productDetail.setWeight(rs.getDouble("Weight"));
                productDetail.setColor(rs.getString("Color"));
                productDetail.setSize(rs.getString("Size"));
                productDetail.setImage(rs.getString("ProductImage"));
                productDetail.setProductId(rs.getString("ProductId"));
                productDetail.setStatus(rs.getInt("Status"));

                productDetails.add(productDetail); // Thêm vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetails; // Trả về danh sách thay vì chỉ một đối tượng
    }

    public int getTotalProductDetails(String productId) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM ProductDetail WHERE ProductID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

}
