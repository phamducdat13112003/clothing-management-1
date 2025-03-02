package org.example.clothingmanagement.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.clothingmanagement.repository.DBContext.getConnection;

public class BinDAO {
    // Method to get all bins
    public List<String> getAllBins() {
        List<String> bins = new ArrayList<>();
        String sql = "SELECT BinID FROM Bin";  // Simple query to get all BinIDs

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bins.add(rs.getString("BinID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bins;
    }
}
