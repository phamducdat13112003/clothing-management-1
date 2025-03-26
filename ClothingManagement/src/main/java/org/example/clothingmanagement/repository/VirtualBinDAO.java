package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.TODetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VirtualBinDAO {
    private String generateNewVirtualBinID(String toID, String productDetailID) throws SQLException {
        // Updated prefix to include both toID and productDetailID
        String prefix = "VT-" + toID + "-" + productDetailID + "-";
        String query = "SELECT COALESCE(MAX(CAST(SUBSTRING_INDEX(VirtualBinID, '-', -1) AS UNSIGNED)), 0) + 1 AS NextNumber " +
                "FROM VirtualBin WHERE VirtualBinID LIKE ?";

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, prefix + "%");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int nextNumber = rs.getInt("NextNumber");
                    return prefix + String.format("%03d", nextNumber);
                }
            }
        }

        return prefix + "001";
    }

    public boolean updateVirtualBin(Connection conn, String toID, String productDetailID,
                                    String originBinID, String finalBinID, int quantity) throws SQLException {
        System.out.println("Updating VirtualBin for Transfer Order " + toID +
                ", product " + productDetailID +
                " by quantity " + quantity);

        // First check if the virtual bin record already exists for this product and transfer order
        String checkQuery = "SELECT * FROM VirtualBin WHERE TOID = ? AND ProductDetailID = ? " +
                "AND OriginBinID = ? AND FinalBinID = ?";

        try (PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setString(1, toID);
            ps.setString(2, productDetailID);
            ps.setString(3, originBinID);
            ps.setString(4, finalBinID);

            try (ResultSet rs = ps.executeQuery()) {
                // If the record exists, update the quantity
                if (rs.next()) {
                    String existingVirtualBinID = rs.getString("VirtualBinID");
                    int currentQuantity = rs.getInt("Quantity");

                    System.out.println("Record exists. Current quantity: " + currentQuantity);

                    // Calculate new quantity
                    int newQuantity = currentQuantity + quantity;

                    // If new quantity is 0 or less, remove the record
                    if (newQuantity <= 0) {
                        String deleteQuery = "DELETE FROM VirtualBin WHERE VirtualBinID = ?";
                        try (PreparedStatement deletePs = conn.prepareStatement(deleteQuery)) {
                            deletePs.setString(1, existingVirtualBinID);
                            int deletedRows = deletePs.executeUpdate();
                            System.out.println("Deleted record: " + (deletedRows > 0 ? "Success" : "Failed"));
                            return deletedRows > 0;
                        }
                    }

                    // Update the existing record with new quantity
                    String updateQuery = "UPDATE VirtualBin SET Quantity = ? WHERE VirtualBinID = ?";

                    try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                        updatePs.setInt(1, newQuantity);
                        updatePs.setString(2, existingVirtualBinID);

                        int updatedRows = updatePs.executeUpdate();
                        System.out.println("Update result: " + (updatedRows > 0 ? "Success" : "Failed"));

                        return updatedRows > 0;
                    }
                } else {
                    // If no record exists and quantity is positive, create a new virtual bin entry
                    if (quantity > 0) {
                        // Generate a new unique VirtualBinID (now including productDetailID)
                        String virtualBinID = generateNewVirtualBinID(toID, productDetailID);
                        System.out.println("Record doesn't exist. Creating new record with ID: " +
                                virtualBinID + " and quantity: " + quantity);

                        // Insert a new virtual bin record
                        String insertQuery = "INSERT INTO VirtualBin (VirtualBinID, TOID, ProductDetailID, " +
                                "Quantity, OriginBinID, FinalBinID) VALUES (?, ?, ?, ?, ?, ?)";

                        try (PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                            insertPs.setString(1, virtualBinID);
                            insertPs.setString(2, toID);
                            insertPs.setString(3, productDetailID);
                            insertPs.setInt(4, quantity);
                            insertPs.setString(5, originBinID);
                            insertPs.setString(6, finalBinID);

                            int insertedRows = insertPs.executeUpdate();
                            System.out.println("Insert result: " + (insertedRows > 0 ? "Success" : "Failed"));

                            return insertedRows > 0;
                        }
                    } else {
                        // If trying to subtract from a non-existent record, return false
                        System.out.println("Cannot subtract from non-existent VirtualBin entry");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error updating VirtualBin: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception to be handled by the calling method
        }
    }

    public List<TODetail> getToDetail(String toID) throws SQLException {
        List<TODetail> toDetails = new ArrayList<>();
        String query = "SELECT ProductDetailID, Quantity, OriginBinID, FinalBinID " +
                "FROM virtualbin WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, toID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TODetail detail = new TODetail();
                    detail.setProductDetailID(rs.getString("ProductDetailID"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setOriginBinID(rs.getString("OriginBinID"));
                    detail.setFinalBinID(rs.getString("FinalBinID"));
                    toDetails.add(detail);
                }
            }
        }

        return toDetails;
    }

    public boolean updateVirtualBinLocation(String productDetailID, String toID, String finalBinID) throws SQLException {
        String query = "UPDATE virtualbin " +
                "SET FinalBinID = ? " +
                "WHERE ProductDetailID = ? AND TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, finalBinID);
            pstmt.setString(2, productDetailID);
            pstmt.setString(3, toID);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean removeVirtualBinEntry(String toID, String productDetailID, String binID) throws SQLException {
        String sql = "DELETE FROM virtualbin WHERE TOID = ? AND ProductDetailID = ? AND OriginBinId = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, toID);
            ps.setString(2, productDetailID);
            ps.setString(3, binID);

            int deletedRows = ps.executeUpdate();
            return deletedRows > 0;
        }
    }
}
