package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BinDetailDAO {
    public List<BinDetail> getAllBinDetails(){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT binDetailId, binId, productDetailId, quantity ");
            sql.append(" FROM binDetail ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while(rs.next()){
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

    public List<BinDetail> getBinDetailsByBinId(String binID){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT binDetailId, binId, productDetailId, quantity ");
            sql.append(" FROM binDetail ");
            sql.append(" WHERE binId = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, binID);
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while(rs.next()){
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

    public List<BinDetail> getAllBinDetailAndProductDetail(){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT b.binid, b.binDetailId, b.ProductDetailId,c.Weight,c.Color,c.Size,c.ProductImage, b.quantity ");
            sql.append(" FROM bindetail b ");
            sql.append(" JOIN productdetail c ON b.productdetailid = c.productdetailid; ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<BinDetail> binDetails = new ArrayList<>();
            while(rs.next()){
                BinDetail bd = BinDetail.builder()
                        .binDetailId(rs.getString("binDetailId"))
                        .binId(rs.getString("binId"))
                        .productDetailId(rs.getString("ProductDetailId"))
                        .quantity(rs.getInt("Quantity"))
                        .weight(rs.getDouble("Weight"))
                        .color(rs.getString("Color"))
                        .size(rs.getString("Size"))
                        .image(rs.getString("ProductImage"))
                        .build();
                binDetails.add(bd);
            }
            return binDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
