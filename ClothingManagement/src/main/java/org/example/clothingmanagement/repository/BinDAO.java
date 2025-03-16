package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.clothingmanagement.repository.DBContext.getConnection;

public class BinDAO {
    public List<Bin> getBinsWithPagination(String sectionId,int page, int pageSize){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT BinId, BinName, MaxCapacity, Status, SectionId ");
            sql.append(" FROM Bin ");
            sql.append(" WHERE SectionID = ? ");
            sql.append(" ORDER BY BinId ASC ");
            sql.append(" LIMIT ? OFFSET ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, sectionId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<Bin> bins = new ArrayList<>();
            while (rs.next()) {
                Bin bin = Bin.builder()
                        .binID(rs.getString("BinID"))
                        .binName(rs.getString("BinName"))
                        .maxCapacity(rs.getDouble("MaxCapacity"))
                        .status(rs.getBoolean("Status"))
                        .currentCapacity(0.0)
                        .sectionID(rs.getString("SectionID"))
                        .build();
                bins.add(bin);
            }
            return bins;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Bin> searchBinWithPagination(String sectionId,String nameSearch,int page, int pageSize){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT BinId, BinName, MaxCapacity, Status, SectionId ");
            sql.append(" FROM Bin ");
            sql.append(" WHERE SectionID = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (BinName LIKE ? ");
                sql.append(" OR BinId LIKE ? ) ");
            }
            sql.append(" ORDER BY BinId ASC ");
            sql.append(" LIMIT ? OFFSET ?");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setString(paramIndex++, sectionId);
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<Bin> bins = new ArrayList<>();
            while (rs.next()) {
                Bin bin = Bin.builder()
                        .binID(rs.getString("BinID"))
                        .binName(rs.getString("BinName"))
                        .maxCapacity(rs.getDouble("MaxCapacity"))
                        .status(rs.getBoolean("Status"))
                        .currentCapacity(0.0)
                        .sectionID(rs.getString("SectionID"))
                        .build();
                bins.add(bin);
            }
            return bins;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Bin> searchBinWithoutPagination(String sectionId,String nameSearch){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT BinId, BinName, MaxCapacity, Status, SectionId ");
            sql.append(" FROM Bin ");
            sql.append(" WHERE SectionID = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (BinName LIKE ? ");
                sql.append(" OR BinId LIKE ? ) ");
            }
            sql.append(" ORDER BY BinId ASC ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setString(paramIndex++, sectionId);
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }

            ResultSet rs = ps.executeQuery();
            List<Bin> bins = new ArrayList<>();
            while (rs.next()) {
                Bin bin = Bin.builder()
                        .binID(rs.getString("BinID"))
                        .binName(rs.getString("BinName"))
                        .maxCapacity(rs.getDouble("MaxCapacity"))
                        .status(rs.getBoolean("Status"))
                        .currentCapacity(0.0)
                        .sectionID(rs.getString("SectionID"))
                        .build();
                bins.add(bin);
            }
            return bins;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get all bins
    public List<Bin> getAllBins() {
        List<Bin> bins = new ArrayList<>();
        String sql = "SELECT * FROM Bin";  // Simple query to get all BinIDs

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Bin bin = new Bin();
                bin.setBinID(rs.getString("BinID"));
                bin.setBinName(rs.getString("BinName"));
                bin.setMaxCapacity(rs.getDouble("MaxCapacity"));
                bin.setStatus(rs.getBoolean("Status"));
                bin.setSectionID(rs.getString("SectionID"));
                bins.add(bin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bins;
    }

    public List<Bin> getAllBin(){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select BinId, BinName, MaxCapacity, Status, SectionID ");
            sql.append(" FROM Bin");
            PreparedStatement stmt = con.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            List<Bin> bins = new ArrayList<>();
            while (rs.next()) {
                Bin bin = Bin.builder()
                        .binID(rs.getString("BinID"))
                        .binName(rs.getString("BinName"))
                        .maxCapacity(rs.getDouble("MaxCapacity"))
                        .status(rs.getBoolean("Status"))
                        .currentCapacity(0.0)
                        .sectionID(rs.getString("SectionID"))
                        .build();
                bins.add(bin);
            }
            return bins;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double getMaxCapacityByBinID(String binID) {
        String sql = "SELECT MaxCapacity FROM Bin WHERE BinID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("maxCapacity"); // Lấy giá trị DECIMAL từ DB dưới dạng double
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Nếu không có dữ liệu, trả về 0.0
    }

    public List<BinDetail> getBinDetailByBinID(String binID, int page, int pageSize) {
        List<BinDetail> list = new ArrayList<>();
        String sql = "SELECT bd.BinDetailID, bd.BinID, bd.ProductDetailID, bd.Quantity, " +
                "       b.BinName, b.MaxCapacity, b.Status AS BinStatus, b.SectionID, " +
                "       pd.Quantity AS PD_Quantity, pd.Weight, pd.Color, pd.Size, pd.ProductImage, pd.ProductID, pd.Status " +
                "FROM BinDetail bd " +
                "JOIN Bin b ON bd.BinID = b.BinID " +
                "JOIN ProductDetail pd ON bd.ProductDetailID = pd.ProductDetailID " +
                "WHERE bd.BinID = ? " +
                "ORDER BY b.Status DESC, bd.BinID ASC " +
                "LIMIT ? OFFSET ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BinDetail binDetail = new BinDetail();
                binDetail.setBinDetailId(rs.getString("BinDetailID"));
                binDetail.setBinId(rs.getString("BinID"));
                binDetail.setProductDetailId(rs.getString("ProductDetailID"));
                binDetail.setQuantity(rs.getInt("Quantity"));
                binDetail.setStatus(rs.getInt("Status"));
                binDetail.setColor(rs.getString("Color"));
                binDetail.setSize(rs.getString("Size"));
                binDetail.setImage(rs.getString("ProductImage"));
                list.add(binDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BinDetail> searchBinDetail(String nameSearch, String binID, int page, int pageSize) {
        List<BinDetail> binDetails = new ArrayList<>();
        String sql = "SELECT bd.BinDetailID, bd.BinID, bd.ProductDetailID, bd.Quantity, " +
                "       b.BinName, b.MaxCapacity, b.Status, b.SectionID, " +
                "       p.ProductImage, p.Color, p.Size " +
                "FROM BinDetail bd " +
                "JOIN Bin b ON bd.BinID = b.BinID " +
                "JOIN ProductDetail p ON bd.ProductDetailID = p.ProductDetailID " +
                "WHERE 1=1 ";

        if (!nameSearch.isEmpty()) {
            sql += " AND bd.ProductDetailID LIKE ? ";
        }
        if (!binID.isEmpty()) {
            sql += " AND bd.BinID = ? ";
        }

        sql += " ORDER BY b.Status DESC, bd.BinID ASC LIMIT ? OFFSET ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;

            if (!nameSearch.isEmpty()) {
                stmt.setString(paramIndex++, "%" + nameSearch + "%");
            }
            if (!binID.isEmpty()) {
                stmt.setString(paramIndex++, binID);
            }
            stmt.setInt(paramIndex++, pageSize);
            stmt.setInt(paramIndex++, (page - 1) * pageSize);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BinDetail binDetail = new BinDetail();
                binDetail.setBinDetailId(rs.getString("BinDetailID"));
                binDetail.setBinId(rs.getString("BinID"));
                binDetail.setProductDetailId(rs.getString("ProductDetailID"));
                binDetail.setQuantity(rs.getInt("Quantity"));
                binDetail.setColor(rs.getString("Color"));
                binDetail.setSize(rs.getString("Size"));
                binDetail.setImage(rs.getString("ProductImage"));
                binDetail.setStatus(rs.getInt("Status"));
                binDetails.add(binDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return binDetails;
    }


    public int countBinDetail(String nameSearch, String binID) {
        String sql = "SELECT COUNT(*) FROM BinDetail WHERE 1=1";

        if (!nameSearch.isEmpty()) {
            sql += " AND ProductDetailID LIKE ? ";
        }
        if (!binID.isEmpty()) {
            sql += " AND BinID = ? ";
        }

        int count = 0;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;

            if (!nameSearch.isEmpty()) {
                stmt.setString(paramIndex++, "%" + nameSearch + "%");
            }
            if (!binID.isEmpty()) {
                stmt.setString(paramIndex++, binID);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    public int countBinDetailByBinID(String binID) {
        String sql = "SELECT COUNT(*) FROM BinDetail WHERE binID = ?";
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

    public int countAllBins() {
        String sql = "SELECT COUNT(*) FROM Bin";
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

    public List<Bin> getBinsBySectionId(String sectionId){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT BinId, BinName, MaxCapacity, Status, SectionId ");
            sql.append(" FROM Bin ");
            sql.append(" WHERE SectionID = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, sectionId);
            ResultSet rs = ps.executeQuery();
            List<Bin> bins = new ArrayList<>();
            while (rs.next()) {
                Bin bin = Bin.builder()
                        .binID(rs.getString("BinID"))
                        .binName(rs.getString("BinName"))
                        .maxCapacity(rs.getDouble("MaxCapacity"))
                        .status(rs.getBoolean("Status"))
                        .currentCapacity(0.0)
                        .sectionID(rs.getString("SectionID"))
                        .build();
                bins.add(bin);
            }
            return bins;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Bin> getBinByBinId(String binId){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT BinId, BinName, MaxCapacity, Status, SectionId ");
            sql.append(" FROM Bin ");
            sql.append(" WHERE binId = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, binId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bin bin = Bin.builder()
                        .binID(rs.getString("BinID"))
                        .binName(rs.getString("BinName"))
                        .maxCapacity(rs.getDouble("MaxCapacity"))
                        .status(rs.getBoolean("Status"))
                        .currentCapacity(0.0)
                        .sectionID(rs.getString("SectionID"))
                        .build();
                return Optional.of(bin);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        BinDAO dao = new BinDAO();
        List<Bin> list = dao.searchBinWithPagination("RP001","002",1,5);
        for (Bin bin : list) {
            System.out.println(bin);
        }
    }
}
