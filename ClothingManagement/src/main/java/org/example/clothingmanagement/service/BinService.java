package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.repository.BinDAO;
import org.example.clothingmanagement.repository.DBContext;
import org.example.clothingmanagement.repository.SectionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BinService {

    private final BinDAO binDAO = new BinDAO();

    public List<Bin> getAllBins(){
        return binDAO.getAllBins();
    }

    public double getMaxCapacityByBinID(String binID) {
        return binDAO.getMaxCapacityByBinID(binID);
    }

    public List<BinDetail> getBinDetailByBinID(String binID, int page, int pageSize){
        return binDAO.getBinDetailByBinID(binID, page, pageSize);
    }

    public boolean canDeleteBin(String binID) {
        return binDAO.canDeleteBin(binID);
    }
    public int getTotalBinsBySection(String sectionId){
        return binDAO.getTotalBinsBySection(sectionId);
    }

    public List<Bin> getBinsWithPagination(String sectionId, int page, int pageSize){
        return binDAO.getBinsWithPagination(sectionId, page, pageSize);
    }

    public List<Bin> searchBinWithPagination(String sectionId, String nameSearch, int page, int pageSize){
        return binDAO.searchBinWithPagination(sectionId, nameSearch, page, pageSize);
    }

    public List<Bin> searchBinWithoutPagination(String sectionId, String nameSearch){
        return binDAO.searchBinWithoutPagination(sectionId, nameSearch);
    }

    public List<Bin> getBinsBySectionId(String sectionId, int page, int pageSize){
        return binDAO.getBinsBySectionId(sectionId, page, pageSize);
    }

    public int countBinDetailByBinID(String binID){
        return binDAO.countBinDetailByBinID(binID);
    }

    public int countAllBins(){
        return binDAO.countAllBins();
    }

    public List<BinDetail> searchBinDetail(String nameSearch, String binID, int page, int pageSize){
        return binDAO.searchBinDetail(nameSearch, binID, page, pageSize);
    }

    public Optional<Bin> getBinByBinId(String binID){
        return binDAO.getBinByBinId(binID);
    }

    public List<Bin> getAllBin(){
        List<Bin> bins = binDAO.getAllBin();
        for(Bin bin : bins){
            bin.setCurrentCapacity(0.0);
            bin.setAvailableCapacity(bin.getMaxCapacity());
        }
        return bins;
    }

    public List<Bin> getBinsBySectionIdWithoutPagination(String sectionId){
        return binDAO.getBinsBySectionIdWithoutPagination(sectionId);
    }

    public boolean deleteBin(String binId) {
        return binDAO.deleteBin(binId);
    }

    public int countBinDetail(String nameSearch, String binID){
        return binDAO.countBinDetail(nameSearch, binID);
    }

    public List<Bin> getBinAndWeightBySectionId(String sectionId) throws SQLException{
        return binDAO.getBinAndWeightBySectionId(sectionId);
    }

    public double getWeightOfBinByBinID(String binID) throws SQLException{
        return binDAO.getWeightOfBinByBinID(binID);
    }

    public int countBinsBySectionId(String sectionId){
        return binDAO.countBinsBySectionId(sectionId);
    }
    public boolean updateSectionStatus(String sectionId) {
        return binDAO.updateSectionStatus(sectionId);
    }

    public static void main(String[] args){
        BinService bs = new BinService();
        List<Bin> list = bs.searchBinWithPagination("RP001","002",1,5);
        for(Bin b : list){
            System.out.println(b);
        }
    }

    public int getBinCountForSection(String sectionId) {
        return binDAO.getTotalBinsBySection(sectionId);
    }

    public String generateNextBinId(String sectionId) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT binID FROM Bin WHERE sectionID = ? ORDER BY binID DESC LIMIT 1";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, sectionId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String lastBinId = rs.getString("binID");

                        // Extract the numeric part
                        String numericPart = lastBinId.substring(lastBinId.lastIndexOf('-') + 1);

                        // Increment the numeric part
                        int nextNumber = Integer.parseInt(numericPart) + 1;

                        // Format the next bin ID with leading zeros
                        return String.format("%s-%03d", sectionId, nextNumber);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If no bins exist, start with the first bin
        return sectionId + "-001";
    }

    public boolean addBin(Bin newBin) {
        return binDAO.addBin(newBin);
    }

    public boolean isBinEmpty(String binId) {
        try {
            // Count the number of distinct product detail IDs in the bin
            String query = "SELECT COUNT(DISTINCT ProductDetailId) FROM bindetail WHERE binId = ?";
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, binId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // If count is 0, bin is empty
                        return rs.getInt(1) == 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasBinProducts(String binId) {
        String query = "SELECT COUNT(*) FROM bindetail WHERE binId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, binId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Return true if there are any products in the bin
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getTotalBinCapacityForSection(String sectionId) {
        List<Bin> binsInSection = getBinsBySectionIdWithoutPagination(sectionId);

        double totalCapacity = 0.0;
        for (Bin bin : binsInSection) {
            totalCapacity += bin.getMaxCapacity();
        }

        return totalCapacity;
    }
}
