package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {
//    public List<Product> getAllProducts() {
//       try(Connection conn = DBContext.getConnection()){
//           StringBuilder sql = new StringBuilder();
//           sql.append(" SELECT ProductID, ProductName, Price, binID, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn FROM Product  ");
//           PreparedStatement ps = conn.prepareStatement(sql.toString());
//           ResultSet rs = ps.executeQuery();
//           List<Product> products = new ArrayList<>();
//           while(rs.next()){
//               Product product = Product.builder()
//                       .id(rs.getString("ProductID"))
//                       .name(rs.getString("ProductName"))
//                       .price(rs.getDouble("Price"))
//                       .binId(rs.getInt("binID"))
//                       .categoryId(rs.getInt("CategoryID"))
//                       .material(rs.getString("Material"))
//                       .gender(rs.getString("Gender"))
//                       .seasons(rs.getString("Seasons"))
//                       .minQuantity(rs.getInt("MinQuantity"))
//                       .createdDate(rs.getDate("CreatedDate"))
//                       .description(rs.getString("Description"))
//                       .createdBy(rs.getInt("CreatedBy"))
//                       .supplierId(rs.getInt("SupplierID"))
//                       .madeIn(rs.getString("MadeIn"))
//                       .build();
//               products.add(product);
//
//           }
//           return products;
//
//       } catch (SQLException e) {
//           throw new RuntimeException(e);
//       }
//
//    }

//    public boolean addProduct(Product product) {
//        try(Connection con = DBContext.getConnection()){
//            StringBuilder sql = new StringBuilder();
//            sql.append(" INSERT INTO Product (ProductName, Price, BinId, CategoryID, Material, Gender, Seasons, MinQuantity, Description, CreatedBy, SupplierID, MadeIn) ");
//            sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
//            PreparedStatement ps = con.prepareStatement(sql.toString());
//            ps.setString(1, product.getName());
//            ps.setDouble(2, product.getPrice());
//            ps.setInt(3, product.getBinId());
//            ps.setInt(4, product.getCategoryId());
//            ps.setString(5, product.getMaterial());
//            ps.setString(6, product.getGender());
//            ps.setString(7, product.getSeasons());
//            ps.setInt(8, product.getMinQuantity());
//            ps.setString(9, product.getDescription());
//            ps.setInt(10, product.getCreatedBy());
//            ps.setInt(11, product.getSupplierId());
//            ps.setString(12, product.getMadeIn());
//            ps.executeUpdate();
//            return true;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//
//        }
//    }

    public boolean deleteProduct(Integer id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE FROM Product ");
            sql.append(" WHERE ProductID = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setLong(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public Optional<Product> getProductById(Integer id) {
//        try(Connection con = DBContext.getConnection()){
//            StringBuilder sql = new StringBuilder();
//            sql.append(" SELECT ProductID, ProductName, Price, binID, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn FROM Product ");
//            sql.append(" WHERE ProductID = ?");
//            PreparedStatement ps = con.prepareStatement(sql.toString());
//            ps.setLong(1, id);
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()){
//                Product product = Product.builder()
//                        .id(rs.getString("ProductID"))
//                        .name(rs.getString("ProductName"))
//                        .price(rs.getDouble("Price"))
//                        .binId(rs.getInt("binID"))
//                        .categoryId(rs.getInt("CategoryID"))
//                        .material(rs.getString("Material"))
//                        .gender(rs.getString("Gender"))
//                        .seasons(rs.getString("Seasons"))
//                        .minQuantity(rs.getInt("MinQuantity"))
//                        .createdDate(rs.getDate("CreatedDate"))
//                        .description(rs.getString("Description"))
//                        .createdBy(rs.getInt("CreatedBy"))
//                        .supplierId(rs.getInt("SupplierID"))
//                        .madeIn(rs.getString("MadeIn"))
//                        .build();
//                return Optional.of(product);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return Optional.empty();
//    }



    public Optional<Product> getProductById(String id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductID, ProductName, Price, binID, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn FROM Product ");
            sql.append(" WHERE ProductID = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Product product = Product.builder()
                        .id(rs.getLong("ProductID"))
                        .name(rs.getString("ProductName"))
                        .price(rs.getDouble("Price"))
                        .binId(rs.getInt("binID"))
                        .categoryId(rs.getInt("CategoryID"))
                        .material(rs.getString("Material"))
                        .gender(rs.getString("Gender"))
                        .seasons(rs.getString("Seasons"))
                        .minQuantity(rs.getInt("MinQuantity"))
                        .createdDate(rs.getDate("CreatedDate"))
                        .description(rs.getString("Description"))
                        .createdBy(rs.getInt("CreatedBy"))
                        .supplierId(rs.getInt("SupplierID"))
                        .madeIn(rs.getString("MadeIn"))
                        .build();
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Product getProductByProductID(String productID) {
        String sql = "SELECT * FROM product WHERE ProductID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Product.builder()
                        .id(rs.getString("ProductID"))
                        .name(rs.getString("ProductName"))
                        .price(rs.getDouble("Price"))
                        .material(rs.getString("Material"))
                        .gender(rs.getString("Gender"))
                        .seasons(rs.getString("Seasons"))
                        .minQuantity(rs.getInt("MinQuantity"))
                        .createdDate(rs.getDate("CreatedDate"))
                        .description(rs.getString("Description"))
                        .madeIn(rs.getString("MadeIn"))
                        .binId(rs.getString("BinID"))
                        .categoryId(rs.getInt("CategoryID"))
                        .createdBy(rs.getString("CategoryID"))
                        .supplierId(rs.getString("SupplierID"))
                        .Status(rs.getInt("Status"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
