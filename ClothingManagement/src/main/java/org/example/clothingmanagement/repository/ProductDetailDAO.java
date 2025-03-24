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

    public List<ProductDetail> findAllWithPagination(int page, int pageSize) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status FROM ProductDetail ");
            sql.append(" ORDER BY ProductDetailId ASC ");
            sql.append (" LIMIT ? OFFSET ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setInt(1, pageSize);
            ps.setInt(2, (page-1)*pageSize);
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

    public List<ProductDetail> searchAllWithPagination(String nameSearch,int page, int pageSize) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status FROM ProductDetail ");
            sql.append(" WHERE 1=1 ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (Color LIKE ? ");
                sql.append(" OR Size LIKE ? ");
                sql.append(" OR ProductDetailId LIKE ?) ");
            }
            sql.append(" ORDER BY ProductDetailId ASC ");
            sql.append (" LIMIT ? OFFSET ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%"+nameSearch+"%");
                ps.setString(paramIndex++, "%"+nameSearch+"%");
                ps.setString(paramIndex++, "%"+nameSearch+"%");
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page-1)*pageSize);
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

    public List<ProductDetail> searchAllWithoutPagination(String nameSearch) {
        try(Connection con = DBContext.getConnection()){

            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status FROM ProductDetail ");
            sql.append(" WHERE 1=1 ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (Color LIKE ? ");
                sql.append(" OR Size LIKE ? ");
                sql.append(" OR ProductDetailId LIKE ?) ");
            }
            sql.append(" ORDER BY ProductDetailId ASC ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%"+nameSearch+"%");
                ps.setString(paramIndex++, "%"+nameSearch+"%");
                ps.setString(paramIndex++, "%"+nameSearch+"%");
            }
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

    public List<ProductDetail> getProductDetailByProductIdWithPagination(String productId,int page,int pageSize) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status ");
            sql.append(" FROM ProductDetail ");
            sql.append(" WHERE ProductId = ? ");
            sql.append(" ORDER BY ProductDetailId ");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, productId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
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

    public List<ProductDetail> SearchProductDetailByProductIdAndNameSearch(String productId,String nameSearch) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status ");
            sql.append(" FROM ProductDetail ");
            sql.append(" WHERE productId = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (color LIKE ? ");
                sql.append(" OR ProductId LIKE ? ");
                sql.append(" OR size LIKE ?) ");
            }
            sql.append(" ORDER BY ProductDetailId ");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setString(paramIndex++, productId);

            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }

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

    public List<ProductDetail> SearchProductDetailByProductIdWithPagination(String productId,String nameSearch,int page,int pageSize) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status ");
            sql.append(" FROM ProductDetail ");
            sql.append(" WHERE productId = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (color LIKE ? ");
                sql.append(" OR ProductDetailId LIKE ? ");
                sql.append(" OR size LIKE ?) ");
            }
            sql.append(" ORDER BY ProductDetailId ");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setString(paramIndex++, productId);

            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page - 1) * pageSize);
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
            sql.append(" WHERE ProductId = ? AND status = 1 ");
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

    public boolean updateProductDetail(ProductDetail pd) {
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE ProductDetail ");
            sql.append(" SET Weight = ?, ProductImage = ? ");
            sql.append(" WHERE ProductDetailId = ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setDouble(1, pd.getWeight());
            ps.setString(2, pd.getImage());
            ps.setString(3, pd.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public List<ProductDetail> getProductDetailByProductDetailId(String productId, int page, int pageSize) {
        List<ProductDetail> productDetails = new ArrayList<>();
        String sql = "SELECT * FROM ProductDetail WHERE ProductDetailId = ? LIMIT ? OFFSET ?";

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
        String sql = "SELECT COUNT(*) FROM ProductDetail WHERE ProductDetailId = ?";

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
    public List<ProductDetail> searchProductDetailsByID(String id, int page, int pageSize){
        List<ProductDetail> list= new ArrayList<>();
        String sql ="SELECT * FROM ProductDetail WHERE ProductDetailId = ? OR ProductId = ? LIMIT ? OFFSET ?";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, id);
            stmt.setInt(3, pageSize);
            stmt.setInt(4, (page-1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setId(rs.getString("ProductDetailId"));
                productDetail.setQuantity(rs.getInt("Quantity"));
                productDetail.setWeight(rs.getDouble("Weight"));
                productDetail.setColor(rs.getString("Color"));
                productDetail.setSize(rs.getString("Size"));
                productDetail.setImage(rs.getString("ProductImage"));
                productDetail.setProductId(rs.getString("ProductId"));
                productDetail.setStatus(rs.getInt("Status"));
                list.add(productDetail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int getTotalProductDetailCount(String code) {
        String sql = "SELECT COUNT(*) AS total FROM productDetail WHERE ProductDetailId LIKE ? ";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchCode = "%" + code + "%";
            stmt.setString(1, searchCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalProductCount() {
        String sql = "SELECT COUNT(*) AS total FROM productDetail";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ProductDetail> getAllProductDetailWithPagination(int page, int pageSize){
        List<ProductDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM ProductDetail LIMIT ? OFFSET ?";
        try(Connection conn = DBContext.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page-1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setId(rs.getString("ProductDetailId"));
                productDetail.setQuantity(rs.getInt("Quantity"));
                productDetail.setWeight(rs.getDouble("Weight"));
                productDetail.setColor(rs.getString("Color"));
                productDetail.setSize(rs.getString("Size"));
                productDetail.setImage(rs.getString("ProductImage"));
                productDetail.setProductId(rs.getString("ProductId"));
                productDetail.setStatus(rs.getInt("Status"));
                list.add(productDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertProductDetail(ProductDetail pd) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO ProductDetail (ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status) ");
            sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, pd.getId());
            ps.setInt(2, pd.getQuantity());
            ps.setDouble(3, pd.getWeight());
            ps.setString(4, pd.getColor());
            ps.setString(5, pd.getSize());
            ps.setString(6, pd.getImage());
            ps.setString(7, pd.getProductId());
            ps.setInt(8, pd.getStatus());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ProductDetail> getLastProductDetail(String productId){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ProductDetailId, Quantity, Weight, Color, Size, ProductImage, ProductId, Status FROM productdetail  ");
            sql.append(" WHERE productid  = ? ");
            sql.append("ORDER BY productdetailid DESC LIMIT 1 ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, productId);
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

    public List<ProductDetail> getColorNSize(String productId){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT color,size FROM productdetail  ");
            sql.append(" WHERE productid  = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            List<ProductDetail> list = new ArrayList<>();
            while (rs.next()) {
                ProductDetail productDetail = ProductDetail.builder()
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .build();
                list.add(productDetail);

            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAllProductDetail(String productId){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE productdetail  ");
            sql.append(" Set status = 0");
            sql.append(" WHERE productid = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, productId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateQuantityProduct(String productDetailId, int quantity) {
        try (Connection con = DBContext.getConnection()) {
            String sql = "UPDATE `productdetail` SET `Quantity`=? WHERE ProductDetailID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setString(2, productDetailId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getTotalQuantityByProductID(String productId) {
        String sql = "SELECT SUM(Quantity) AS TotalQuantity FROM ProductDetail WHERE ProductID = ?";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TotalQuantity");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching total quantity by ProductID", e);
        }
        return 0;
    }
    public static void main(String[] args){
        final ProductDetailDAO productDetailDAO = new ProductDetailDAO();
        List<ProductDetail> list = productDetailDAO.searchAllWithPagination("m",1,5);
        for(ProductDetail productDetail : list){
            System.out.println(productDetail);
        }
    }
}
