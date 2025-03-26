package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.BinDetailService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class BinDetailDAO {
    public List<BinDetail> getBinDetailsWithPagination(String binId,int page, int pageSize){
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT binDetailId, binId, productDetailId, quantity ");
            sql.append(" FROM binDetail ");
            sql.append(" WHERE binId = ? ");
            sql.append(" AND quantity !=0 ");
            sql.append(" ORDER BY binId ASC ");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, binId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while (rs.next()) {
                BinDetail bd = BinDetail.builder()
                        .binDetailId(rs.getString("binDetailId"))
                        .binId(rs.getString("binId"))
                        .productDetailId(rs.getString("productDetailId"))
                        .quantity(rs.getInt("quantity"))
                        .build();
                binDetails.add(bd);
            }
            return binDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BinDetail> searchBinDetailWithPagination(String binId,String nameSearch, int page, int pageSize){
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT b.binDetailId, b.binId, b.quantity, b.ProductDetailId, p.Color, p.size");
            sql.append(" FROM binDetail b ");
            sql.append(" JOIN productDetail p ON b.ProductDetailId = p.ProductDetailID ");
            sql.append(" WHERE b.binId = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (b.binDetailId LIKE ? ");
                sql.append(" OR b.ProductDetailId LIKE ? ");
                sql.append(" OR p.color LIKE ? ");
                sql.append(" OR p.size LIKE ? )");
            }
            sql.append(" ORDER BY binDetailId ASC ");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setString(paramIndex++, binId);
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while (rs.next()) {
                BinDetail bd = BinDetail.builder()
                        .binDetailId(rs.getString("binDetailId"))
                        .binId(rs.getString("binId"))
                        .productDetailId(rs.getString("productDetailId"))
                        .quantity(rs.getInt("quantity"))
                        .build();
                binDetails.add(bd);
            }
            return binDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BinDetail> searchBinDetailWithoutPagination(String binId,String nameSearch){
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT b.binDetailId, b.binId, b.quantity, b.ProductDetailId, p.Color, p.size");
            sql.append(" FROM binDetail b ");
            sql.append(" JOIN productDetail p ON b.ProductDetailId = p.ProductDetailID ");
            sql.append(" WHERE b.binId = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (b.binDetailId LIKE ? ");
                sql.append(" OR b.productDetailId LIKE ? ");
                sql.append(" OR p.Color = ? ");
                sql.append(" OR p.size = ? )");
            }
            sql.append(" ORDER BY binDetailId ASC ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setString(paramIndex++, binId);
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while (rs.next()) {
                BinDetail bd = BinDetail.builder()
                        .binDetailId(rs.getString("binDetailId"))
                        .binId(rs.getString("binId"))
                        .productDetailId(rs.getString("productDetailId"))
                        .quantity(rs.getInt("quantity"))
                        .build();
                binDetails.add(bd);
            }
            return binDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BinDetail> getAllBinDetails() {
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT binDetailId, binId, productDetailId, quantity ");
            sql.append(" FROM binDetail ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while (rs.next()) {
                BinDetail bd = BinDetail.builder()
                        .binDetailId(rs.getString("binDetailId"))
                        .binId(rs.getString("binId"))
                        .productDetailId(rs.getString("productDetailId"))
                        .quantity(rs.getInt("quantity"))
                        .build();
                binDetails.add(bd);
            }
            return binDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BinDetail> getBinDetailsByBinId(String binID) {
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT binDetailId, binId, productDetailId, quantity ");
            sql.append(" FROM binDetail ");
            sql.append(" WHERE binId = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, binID);
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while (rs.next()) {
                BinDetail binDetail = BinDetail.builder()
                        .binDetailId(rs.getString("binDetailId"))
                        .binId(rs.getString("binId"))
                        .productDetailId(rs.getString("productDetailId"))
                        .quantity(rs.getInt("quantity"))
                        .build();
                binDetails.add(binDetail);
            }
            return binDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BinDetail> getAllBinDetailAndProductDetailByBinId(String binID) {
        try (Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.bindetailid, a.binId, a.ProductDetailId, a.quantity,b.Weight,b.Color,b.size,b.ProductImage,b.ProductID,b.`Status` ");
            sql.append(" FROM bindetail a ");
            sql.append(" JOIN productdetail b ");
            sql.append(" ON a.productdetailid = b.productdetailid ");
            sql.append(" WHERE a.BinId = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, binID);
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while (rs.next()) {
                BinDetail binDetail = BinDetail.builder()
                        .binDetailId(rs.getString("binDetailId"))
                        .binId(rs.getString("binId"))
                        .productDetailId(rs.getString("ProductDetailId"))
                        .quantity(rs.getInt("quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("size"))
                        .image(rs.getString("ProductImage"))
                        .status(rs.getInt("Status"))
                        .build();
                binDetails.add(binDetail);
            }
            return binDetails;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }


    }
    public String getLastBinDetailId(String binId) {
        String lastBinDetailId = null;
        String sql = "SELECT binDetailId FROM binDetail WHERE binId = ? ORDER BY binDetailId DESC LIMIT 1";

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, binId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lastBinDetailId = rs.getString("binDetailId");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy binDetailId cuối cùng: " + e.getMessage(), e);
        }

        return lastBinDetailId;
    }

    public boolean addBinDetail(String binDetailId, String binId, String productDetailId, int quantity) {
        String sql = "INSERT INTO binDetail (binDetailId, binId, productDetailId, quantity) VALUES (?, ?, ?, ?)";

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, binDetailId);
            ps.setString(2, binId);
            ps.setString(3, productDetailId);
            ps.setInt(4, quantity);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu chèn thành công
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm binDetail: " + e.getMessage(), e);
        }
    }



    public boolean deleteProductFromBin(String binId, String productDetailId) {
        String sql = "DELETE FROM binDetail WHERE binId = ? AND productDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binId);
            stmt.setString(2, productDetailId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean canDeleteProduct(String binId, String productDetailId) {
        String sql = "SELECT quantity FROM binDetail WHERE binId = ? AND productDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binId);
            stmt.setString(2, productDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity") == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ProductDetail> getProductsInBin(String binId) {
        List<ProductDetail> productList = new ArrayList<>();
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT bd.ProductDetailId, bd.quantity, " +
                    "pd.size, pd.color, pd.weight, pd.productImage " +
                    "FROM BinDetail bd " +
                    "JOIN ProductDetail pd ON bd.ProductDetailId = pd.ProductDetailId " +
                    "WHERE bd.binId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, binId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductDetail product = new ProductDetail();
                product.setId(rs.getString("ProductDetailId"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSize(rs.getString("size"));
                product.setColor(rs.getString("color"));
                product.setWeight(rs.getDouble("weight"));
                product.setImage(rs.getString("ProductImage"));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
    public String findBinDetailIdByBinAndProduct(String binId, String productDetailId) {
        String sql = "SELECT binDetailId FROM BinDetail WHERE binId = ? AND ProductDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binId);
            stmt.setString(2, productDetailId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("binDetailId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBinDetailQuantity(String binDetailId, int additionalQuantity) {
        String sql = "UPDATE BinDetail SET quantity = quantity + ? WHERE binDetailId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, additionalQuantity);
            stmt.setString(2, binDetailId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        BinDetailService binDetailService = new BinDetailService();
        List<BinDetail> list = binDetailService.searchBinDetailWithPagination("RP001-001","blue",1,5);
        List<ProductDetail> listP = binDetailService.getProductsInBin("RP001-001");
        for(ProductDetail bd : listP){
            System.out.println(bd);
        }
    }

}

