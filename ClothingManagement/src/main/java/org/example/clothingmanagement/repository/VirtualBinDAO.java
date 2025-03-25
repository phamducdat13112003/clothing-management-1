package org.example.clothingmanagement.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

//    private String generateNewVirtualBinID(Connection conn) throws SQLException {
//        // Query to get the maximum existing ID
//        String maxIDQuery = "SELECT MAX(VirtualBinID) AS MaxID FROM VirtualBin WHERE VirtualBinID LIKE 'VT-%'";
//
//        try (PreparedStatement ps = conn.prepareStatement(maxIDQuery);
//             ResultSet rs = ps.executeQuery()) {
//
//            int nextNumber = 1;
//
//            // If an existing ID is found, increment the number
//            if (rs.next() && rs.getString("MaxID") != null) {
//                String maxID = rs.getString("MaxID");
//                // Extract the numeric part and increment
//                String numberPart = maxID.substring(3); // Remove 'VT-'
//                nextNumber = Integer.parseInt(numberPart) + 1;
//            }
//
//            // Format the new ID with leading zeros
//            return String.format("VT-%03d", nextNumber);
//        }
//    }
}
