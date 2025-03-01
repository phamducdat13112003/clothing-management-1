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
                        .id(rs.getString("ProductId"))
                        .status(rs.getInt("Status"))
                        .build();
                list.add(productDetail);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductDetail> findByProductId(Long productId) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId FROM ProductDetail ");
            sql.append(" WHERE ProductId = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setLong(1, productId);
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
                        .id(rs.getString("ProductId"))
                        .build();
                list.add(productDetail);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public Optional<ProductDetail> findTheFirstProductDetailOfProductId(Long id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId FROM ProductDetail ");
            sql.append(" WHERE ProductId = ? ");
            sql.append(" AND ProductDetailId = 1 ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ProductDetail productDetail = ProductDetail.builder()
                        .id(rs.getString("ProductDetailId"))
                        .quantity(rs.getInt("Quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .image(rs.getString("ProductImage"))
                        .id(rs.getString("ProductId"))
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




}
