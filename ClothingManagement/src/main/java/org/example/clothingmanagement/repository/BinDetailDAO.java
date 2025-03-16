package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;

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
            sql.append(" SELECT binDetailId, binId, productDetailId, quantity ");
            sql.append(" FROM binDetail ");
            sql.append(" WHERE binId = ? ");
            if(!nameSearch.isEmpty()){

            }
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
}

