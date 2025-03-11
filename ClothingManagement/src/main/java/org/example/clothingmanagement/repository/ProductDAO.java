package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductDAO {
    public List<Product> getAllProducts() {
        try(Connection conn = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductID, ProductName, Price, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn, Status FROM Product  ");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()){
                Product product = Product.builder()
                        .id(rs.getString("ProductID"))
                        .name(rs.getString("ProductName"))
                        .price(rs.getDouble("Price"))
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
            sql.append(" INSERT INTO Product (ProductName, Price, CategoryID, Material, Gender, Seasons, MinQuantity, Description, CreatedBy, SupplierID, MadeIn) ");
            sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getCategoryId());
            ps.setString(4, product.getMaterial());
            ps.setString(5, product.getGender());
            ps.setString(6, product.getSeasons());
            ps.setInt(7, product.getMinQuantity());
            ps.setString(8, product.getDescription());
            ps.setString(9, product.getCreatedBy());
            ps.setString(10, product.getSupplierId());
            ps.setString(11, product.getMadeIn());
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
            sql.append(" SELECT ProductID, ProductName, Price, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn FROM Product ");
            sql.append(" WHERE ProductID = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Product product = Product.builder()
                        .id(rs.getString("ProductID"))
                        .name(rs.getString("ProductName"))
                        .price(rs.getDouble("Price"))
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
                        .categoryId(rs.getInt("CategoryID"))
                        .createdBy(rs.getString("CreatedBy"))
                        .supplierId(rs.getString("SupplierID"))
                        .Status(rs.getInt("Status"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

//    public List<Product> getProductsByBinID(String binID, int page, int pageSize) {
//        List<Product> products = new ArrayList<>();
//        String sql = "SELECT * FROM product WHERE BinID = ? LIMIT ? OFFSET ?";
//
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, binID);
//            stmt.setInt(2, pageSize);
//            stmt.setInt(3, (page - 1) * pageSize);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                Product product = new Product();
//                product.setId(rs.getString("ProductID"));
//                product.setName(rs.getString("ProductName"));
//                product.setPrice(rs.getDouble("Price"));
//                product.setCategoryId(rs.getInt("CategoryID"));
//                product.setMaterial(rs.getString("Material"));
//                product.setGender(rs.getString("Gender"));
//                product.setSeasons(rs.getString("Seasons"));
//                product.setMinQuantity(rs.getInt("MinQuantity"));
//                product.setCreatedDate(rs.getDate("CreatedDate"));
//                product.setDescription(rs.getString("Description"));
//                product.setCreatedBy(rs.getString("CreatedBy"));
//                product.setSupplierId(rs.getString("SupplierID"));
//                product.setMadeIn(rs.getString("MadeIn"));
//                products.add(product);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return products;
//    }

//    public int countProductsByBinID(String binID) {
//        String sql = "SELECT COUNT(*) FROM product WHERE binID = ?";
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, binID);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

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
    public HashMap<Product, String> searchProducts(String txt) {
        HashMap<Product, String> products = new HashMap<>();
        String sql = "SELECT ProductID, ProductName, Price, BinID, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn, Status FROM Product WHERE ProductName LIKE ? OR ProductID LIKE ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, "%" + txt + "%");
            statement.setString(2, "%" + txt + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = Product.builder()
                        .id(resultSet.getString("ProductID"))
                        .name(resultSet.getString("ProductName"))
                        .price(resultSet.getDouble("Price"))
                        .binId(resultSet.getString("BinID"))
                        .categoryId(resultSet.getInt("CategoryID"))
                        .material(resultSet.getString("Material"))
                        .gender(resultSet.getString("Gender"))
                        .seasons(resultSet.getString("Seasons"))
                        .minQuantity(resultSet.getInt("MinQuantity"))
                        .createdDate(resultSet.getDate("CreatedDate"))
                        .description(resultSet.getString("Description"))
                        .createdBy(resultSet.getString("CreatedBy"))
                        .supplierId(resultSet.getString("SupplierID"))
                        .madeIn(resultSet.getString("MadeIn"))
                        .Status(resultSet.getInt("Status"))
                        .build();
                products.put(product, resultSet.getString("ProductID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public List<Map<String, Object>> getAllProductProductDetail() {
        List<Map<String, Object>> productDetails = new ArrayList<>();
        String sql = "SELECT p.ProductID,  p.ProductName, p.Price,  p.Material, p.Gender, p.Seasons, p.MinQuantity, p.CreatedDate, p.Description, p.MadeIn, p.CategoryID, p.CreatedBy, p.SupplierID, p.Status AS product_status, pd.ProductDetailID AS product_detail_id, pd.Quantity, pd.Weight, pd.Color, pd.Size, pd.ProductImage, pd.Status AS product_detail_status FROM Product p INNER JOIN ProductDetail pd ON p.ProductID = pd.ProductID";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> productDetail = new HashMap<>();
                productDetail.put("ProductID", rs.getString("ProductID"));
                productDetail.put("ProductName", rs.getString("ProductName"));
                productDetail.put("Price", rs.getDouble("Price"));
                productDetail.put("Material", rs.getString("Material"));
                productDetail.put("Gender", rs.getString("Gender"));
                productDetail.put("Seasons", rs.getString("Seasons"));
                productDetail.put("MinQuantity", rs.getInt("MinQuantity"));
                productDetail.put("CreatedDate", rs.getDate("CreatedDate"));
                productDetail.put("Description", rs.getString("Description"));
                productDetail.put("MadeIn", rs.getString("MadeIn"));
                productDetail.put("CategoryID", rs.getInt("CategoryID"));
                productDetail.put("CreatedBy", rs.getString("CreatedBy"));
                productDetail.put("SupplierID", rs.getString("SupplierID"));
                productDetail.put("ProductStatus", rs.getInt("product_status"));
                productDetail.put("ProductDetailID", rs.getString("product_detail_id"));
                productDetail.put("Quantity", rs.getInt("Quantity"));
                productDetail.put("Weight", rs.getDouble("Weight"));
                productDetail.put("Color", rs.getString("Color"));
                productDetail.put("Size", rs.getString("Size"));
                productDetail.put("ProductImage", rs.getString("ProductImage"));
                productDetail.put("ProductDetailStatus", rs.getInt("product_detail_status"));

                productDetails.add(productDetail);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product details", e);
        }
        return productDetails;
    }

    public static void main(String[] args) {

    }

}
