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
    // Add this method to the existing BinDAO class
    public boolean addBin(Bin bin) {
        try(Connection con = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO Bin (BinID, BinName, MaxCapacity, Status, SectionID) ");
            sql.append(" VALUES (?, ?, ?, ?, ?) ");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, bin.getBinID());
            ps.setString(2, bin.getBinName());
            ps.setDouble(3, bin.getMaxCapacity());
            ps.setBoolean(4, bin.isStatus());
            ps.setString(5, bin.getSectionID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNextBinId(String sectionId) {
        int nextSequence = 1;

        try (Connection conn = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT BinID ");
            sql.append(" FROM Bin ");
            sql.append(" WHERE BinID LIKE ? ");
            sql.append(" ORDER BY BinID DESC ");
            sql.append(" LIMIT 1 ");

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setString(1, sectionId + "-%");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String lastBinId = rs.getString("BinID");
                String sequenceStr = lastBinId.substring(lastBinId.lastIndexOf("-") + 1);
                try {
                    nextSequence = Integer.parseInt(sequenceStr) + 1;
                } catch (NumberFormatException e) {
                    // If we can't parse the number for some reason, default to 1
                    nextSequence = 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Format the sequence number with leading zeros (e.g., 001, 002, etc.)
        return sectionId + "-" + String.format("%03d", nextSequence);
    }

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

    public boolean canDeleteBin(String binID) {
        String sql = "SELECT COUNT(*) AS count FROM bindetail WHERE binId = ? AND quantity > 0";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") == 0; // Nếu count == 0 thì bin không chứa sản phẩm nào có quantity > 0 → có thể xóa
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
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

    public List<Bin> getBinsBySectionId(String sectionId, int page, int pageSize) {
        List<Bin> bins = new ArrayList<>();
        String sql = """
        SELECT BinId, BinName, MaxCapacity, Status, SectionId 
        FROM Bin 
        WHERE SectionID = ? 
        LIMIT ?, ? """;
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sectionId);
            ps.setInt(2, (page - 1) * pageSize); // Offset
            ps.setInt(3, pageSize); // Limit
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
                bins.add(bin);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bins;
    }

    public List<Bin> getBinsBySectionIdWithoutPagination(String sectionId){
        try(Connection con= DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT BinId, BinName, MaxCapacity, Status, SectionId ");
            sql.append(" FROM Bin ");
            sql.append(" WHERE SectionID = ?");
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

    // Fetch all bins associated with a specific section ID
    public List<Bin> getBinsBySection(String sectionID) {
        List<Bin> bins = new ArrayList<>();
        String query = "SELECT * FROM bin WHERE SectionID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sectionID);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bin bin = new Bin();
                bin.setBinID(rs.getString("BinID"));
                bin.setBinName(rs.getString("BinName"));
                bin.setMaxCapacity(rs.getDouble("MaxCapacity"));
                bin.setStatus(rs.getBoolean("Status"));
                System.out.println("Found bin: " + bin.getBinID() + " - " + bin.getBinName());
                bins.add(bin);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bins;
    }

    // Search for bins by query (name or ID) across all sections
    public List<Bin> searchBinsByQuery(String query) {
        List<Bin> bins = new ArrayList<>();
        String sql = "SELECT * FROM bin WHERE BinName LIKE ? OR BinID LIKE ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bin bin = new Bin();
                bin.setBinID(rs.getString("BinID"));
                bin.setBinName(rs.getString("BinName"));
                bins.add(bin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bins;
    }

    // Search for bins by section ID and a search query (name or ID)
    public List<Bin> searchBinsBySectionAndQuery(String query, String sectionID) {
        List<Bin> bins = new ArrayList<>();
        String sql = "SELECT * FROM bin WHERE SectionID = ? AND (BinName LIKE ? OR BinID LIKE ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sectionID);
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bin bin = new Bin();
                bin.setBinID(rs.getString("BinID"));
                bin.setBinName(rs.getString("BinName"));
                bins.add(bin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bins;
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

    public boolean deleteBin(String binId){
        String sql= "UPDATE bin SET status = 0 WHERE binID = ?";
        try (Connection conn = DBContext.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getTotalBinsBySection(String sectionId) {
        String sql = "SELECT COUNT(*) FROM Bin WHERE SectionID = ?";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sectionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    public static void main(String[] args) {
        BinDAO dao = new BinDAO();
        List<Bin> list = dao.searchBinWithPagination("RP001","002",1,5);
        for (Bin bin : list) {
            System.out.println(bin);
        }
    }


}
