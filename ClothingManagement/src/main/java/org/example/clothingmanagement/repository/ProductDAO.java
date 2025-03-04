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
    public List<Product> getAllProducts() {
        try(Connection conn = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductID, ProductName, Price, binID, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn, Status FROM Product  ");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()){
                Product product = Product.builder()
                        .id(rs.getString("ProductID"))
                        .name(rs.getString("ProductName"))
                        .price(rs.getDouble("Price"))
                        .binId(rs.getString("binID"))
                        .categoryId(rs.getInt("CategoryID"))
                        .material(rs.getString("Material"))
                        .gender(rs.getString("Gender"))
                        .seasons(rs.getString("Seasons"))
                        .minQuantity(rs.getInt("MinQuantity"))
                        .createdDate(rs.getDate("CreatedDate"))
                        .description(rs.getString("Description"))
                        .createdBy(rs.getString("CreatedBy"))
                        .supplierId(rs.getString("SupplierID"))
                        .madeIn(rs.getString("MadeIn"))
                        .Status(rs.getInt("Status"))
                        .build();
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean addProduct(Product product) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO Product (ProductName, Price, BinId, CategoryID, Material, Gender, Seasons, MinQuantity, Description, CreatedBy, SupplierID, MadeIn) ");
            sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getBinId());
            ps.setInt(4, product.getCategoryId());
            ps.setString(5, product.getMaterial());
            ps.setString(6, product.getGender());
            ps.setString(7, product.getSeasons());
            ps.setInt(8, product.getMinQuantity());
            ps.setString(9, product.getDescription());
            ps.setString(10, product.getCreatedBy());
            ps.setString(11, product.getSupplierId());
            ps.setString(12, product.getMadeIn());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public boolean deleteProduct(String id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE Product ");
            sql.append(" SET Status=0");
            sql.append(" WHERE ProductID=?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean recoverProduct(String id) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE Product ");
            sql.append(" SET Status=1");
            sql.append(" WHERE ProductID=?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
                        .id(rs.getString("ProductID"))
                        .name(rs.getString("ProductName"))
                        .price(rs.getDouble("Price"))
                        .binId(rs.getString("binID"))
                        .categoryId(rs.getInt("CategoryID"))
                        .material(rs.getString("Material"))
                        .gender(rs.getString("Gender"))
                        .seasons(rs.getString("Seasons"))
                        .minQuantity(rs.getInt("MinQuantity"))
                        .createdDate(rs.getDate("CreatedDate"))
                        .description(rs.getString("Description"))
                        .createdBy(rs.getString("CreatedBy"))
                        .supplierId(rs.getString("SupplierID"))
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
        String sql = " SELECT * FROM product WHERE ProductID = ? ";
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

    public List<Product> getProductsByBinID(String binID, int page, int pageSize) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE BinID = ? LIMIT ? OFFSET ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getString("ProductID"));
                product.setName(rs.getString("ProductName"));
                product.setPrice(rs.getDouble("Price"));
                product.setBinId(rs.getString("binID"));
                product.setCategoryId(rs.getInt("CategoryID"));
                product.setMaterial(rs.getString("Material"));
                product.setGender(rs.getString("Gender"));
                product.setSeasons(rs.getString("Seasons"));
                product.setMinQuantity(rs.getInt("MinQuantity"));
                product.setCreatedDate(rs.getDate("CreatedDate"));
                product.setDescription(rs.getString("Description"));
                product.setCreatedBy(rs.getString("CreatedBy"));
                product.setSupplierId(rs.getString("SupplierID"));
                product.setMadeIn(rs.getString("MadeIn"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public int countProductsByBinID(String binID) {
        String sql = "SELECT COUNT(*) FROM product WHERE binID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countAllProducts() {
        String sql = "SELECT COUNT(*) FROM product";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
