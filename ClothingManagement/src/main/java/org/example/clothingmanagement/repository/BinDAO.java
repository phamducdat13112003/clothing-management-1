package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Bin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.clothingmanagement.repository.DBContext.getConnection;

public class BinDAO {
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

}
