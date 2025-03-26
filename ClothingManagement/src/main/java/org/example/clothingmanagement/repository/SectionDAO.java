package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.service.SectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectionDAO {
    public List<String> getAllSectionIds() {
        List<String> sectionIds = new ArrayList<>();

        try (Connection conn = DBContext.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SectionID ");
            sql.append(" FROM Section ");
            sql.append(" ORDER BY SectionID ");

            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sectionIds.add(rs.getString("SectionID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sectionIds;
    }

    public List<Section> getAllSection(){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
            sql.append(" Order by sectiontypeId ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> getSectionsWithPagination(int sectionTypeId,int page, int pageSize){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
            sql.append(" Where sectionTypeId = ? AND Status = 1 ");
            sql.append(" Order by sectiontypeId ");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setInt(1, sectionTypeId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> searchSectionWithoutPagination(int sectionTypeId,String nameSearch){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
            sql.append(" Where sectionTypeId = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (sectionName LIKE ? ");
                sql.append(" OR sectionId LIKE ? )");
            }
            sql.append(" Order by sectionTypeId ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setInt(paramIndex++, sectionTypeId);
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }
            ResultSet rs = ps.executeQuery();
            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> searchSectionWithPagination(int sectionTypeId,String nameSearch,int page, int pageSize){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
            sql.append(" Where sectionTypeId = ? ");
            if(!nameSearch.isEmpty()){
                sql.append(" AND (sectionName LIKE ? ");
                sql.append(" OR sectionId LIKE ? ) ");
            }
            sql.append(" Order by sectionTypeId ");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            ps.setInt(paramIndex++, sectionTypeId);
            if(!nameSearch.isEmpty()){
                ps.setString(paramIndex++, "%" + nameSearch + "%");
                ps.setString(paramIndex++, "%" + nameSearch + "%");
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSectionCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM section";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public Section getSectionsById(String sectionId) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
            sql.append(" Where sectionId = ? ");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, sectionId);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                return section;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Section> getSectionById(String sectionId){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
            sql.append(" Where sectionId = ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setString(1, sectionId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                return Optional.of(section);

            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> getSectionsBySectionTypeId(int sectionTypeId){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
            sql.append(" Where sectionTypeId = ? AND Status = 1");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setInt(1, sectionTypeId);
            ResultSet rs = ps.executeQuery();
            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Section getSectionBySectionId(String sectionId) {
        try (Connection con = DBContext.getConnection()) {
            String sql = "SELECT s.sectionID, s.sectionName, s.sectionTypeID, " +
                    "st.sectionTypeName, st.description " +
                    "FROM section s " +
                    "JOIN sectiontype st ON s.sectionTypeID = st.sectionTypeID " +
                    "WHERE s.sectionID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sectionId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Section.builder()
                        .sectionID(rs.getString("sectionID"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeID"))
                        .sectionTypeName(rs.getString("sectionTypeName"))
                        .description(rs.getString("description"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Trả về null nếu không tìm thấy section
    }


    public List<Section> getSectionsBySectionTypeIsReceiptStorage(){
        try(Connection con = DBContext.getConnection()){
            String sql = "SELECT * FROM section WHERE SectionTypeID = '1'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionId"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeId"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSectionByBin(String binID) {
        String sql = "SELECT sectionID FROM bin WHERE binID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, binID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("sectionID");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void main(String[] args) {
        SectionDAO sd = new SectionDAO();
        List<Section> list = sd.searchSectionWithPagination(2,"",1,5);
        for(Section section : list){
            System.out.println(section);
        }

    }


    public String generateSectionID() {
        try(Connection con = DBContext.getConnection()){
            String sql = "SELECT sectionID FROM section WHERE sectionID LIKE 'SE%'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int highestNumericPart = 0;

            while(rs.next()){
                String sectionID = rs.getString("sectionID");
                // Make sure we only process IDs that match our pattern
                if (sectionID.matches("SE\\d+")) {
                    try {
                        int numericPart = Integer.parseInt(sectionID.substring(2));
                        if (numericPart > highestNumericPart) {
                            highestNumericPart = numericPart;
                        }
                    } catch (NumberFormatException e) {
                        // Skip any malformed IDs
                        continue;
                    }
                }
            }

            // Increment the highest found numeric part
            highestNumericPart++;

            // Format the new ID with leading zeros
            return String.format("SE%03d", highestNumericPart);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> getAllSections() {
        try(Connection con = DBContext.getConnection()){
            String sql = "SELECT s.sectionID, s.sectionName, s.sectionTypeID, " +
                    "st.sectionTypeName, st.description " +
                    "FROM section s " +
                    "JOIN sectiontype st ON s.sectionTypeID = st.sectionTypeID";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionID"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeID"))
                        .sectionTypeName(rs.getString("sectionTypeName"))
                        .description(rs.getString("description"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> getAllSectionWithPagination(int page, int pageSize) {
        try (Connection con = DBContext.getConnection()) {
            String sql = "SELECT s.sectionID, s.sectionName, s.sectionTypeID, " +
                    "st.sectionTypeName, st.description " +
                    "FROM section s " +
                    "JOIN sectiontype st ON s.sectionTypeID = st.sectionTypeID " +
                    "LIMIT ? OFFSET ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            List<Section> sections = new ArrayList<>();
            while (rs.next()) {
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionID"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeID"))
                        .sectionTypeName(rs.getString("sectionTypeName"))
                        .description(rs.getString("description"))
                        .build();
                sections.add(section);
            }
            return sections;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Section getSectionByID(String sectionID) {
        try(Connection con = DBContext.getConnection()){
            String sql = "SELECT sectionID, sectionName, sectionTypeID FROM section WHERE sectionID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sectionID);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return Section.builder()
                        .sectionID(rs.getString("sectionID"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeID"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insertSection(Section section) {
        try(Connection con = DBContext.getConnection()){
            String sql = "INSERT INTO section (sectionID, sectionName, sectionTypeID) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, section.getSectionID());
            ps.setString(2, section.getSectionName());
            ps.setInt(3, section.getSectionTypeId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateSection(Section section) {
        try(Connection con = DBContext.getConnection()){
            String sql = "UPDATE section SET sectionName = ?, sectionTypeID = ? WHERE sectionID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, section.getSectionName());
            ps.setInt(2, section.getSectionTypeId());
            ps.setString(3, section.getSectionID());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteSection(String sectionID) {
        try(Connection con = DBContext.getConnection()){
            // First check if section has bins
            String checkSql = "SELECT COUNT(*) FROM bin WHERE sectionID = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, sectionID);
            ResultSet checkRs = checkPs.executeQuery();

            if(checkRs.next() && checkRs.getInt(1) > 0) {
                return false; // Section has bins, cannot delete
            }

            String sql = "DELETE FROM section WHERE sectionID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sectionID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> getSectionsWithBinCount(int page, int pageSize, int sectionTypeId) {
        List<Section> sections = new ArrayList<>();
        try (Connection con = DBContext.getConnection()) {
            String sql = """
            SELECT s.SectionID, s.SectionName, COUNT(b.BinID) AS NumberOfBins
            FROM Section s
            LEFT JOIN Bin b ON s.SectionID = b.SectionID
            WHERE s.SectionTypeID = ? AND s.Status = 1
            GROUP BY s.SectionID, s.SectionName
            LIMIT ? OFFSET ?
        """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, sectionTypeId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Section section = Section.builder()
                        .sectionID(rs.getString("SectionID"))
                        .sectionName(rs.getString("SectionName"))
                        .numberOfBins(rs.getInt("NumberOfBins"))
                        .build();
                sections.add(section);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sections;
    }

    public int getSectionTypeIdBySectionId(String sectionId) {
        String sql = "SELECT SectionTypeID FROM Section WHERE SectionID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sectionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("SectionTypeID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0; // Trả về null nếu không tìm thấy
    }



    public int getTotalSections() {
        try (Connection con = DBContext.getConnection()) {
            String sql = "SELECT COUNT(*) FROM Section WHERE Status = 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    public boolean isSectionNameExistsExcludingCurrent(String sectionName, String currentSectionID) {
        boolean exists = false;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM section WHERE LOWER(sectionName) = LOWER(?) AND sectionID != ?")) {

            ps.setString(1, sectionName);
            ps.setString(2, currentSectionID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean isSectionNameExists(String sectionName) {
        boolean exists = false;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM section WHERE LOWER(sectionName) = LOWER(?)")) {

            ps.setString(1, sectionName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public int getNextSectionNumber(String sectionId) {
        String sql = "SELECT MAX(CAST(SUBSTRING(SectionID, 3, 3) AS UNSIGNED)) FROM section WHERE SectionID LIKE ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sectionId + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maxNumber = rs.getInt(1);
                    return maxNumber + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Nếu chưa có thì bắt đầu từ 1
    }

    public List<Bin> getBinsBySection(String sectionID) throws SQLException {
        List<Bin> bins = new ArrayList<>();
        String sql = "SELECT BinID, SectionID, MaxCapacity, Status " +
                "FROM Bin " +
                "WHERE SectionID = ? AND Status = TRUE"; // Using boolean true for active bins

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sectionID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Bin bin = new Bin();
                    bin.setBinID(rs.getString("BinID"));
                    bin.setSectionID(rs.getString("SectionID"));
                    bin.setMaxCapacity(rs.getDouble("MaxCapacity"));
                    bin.setStatus(rs.getBoolean("Status"));

                    bins.add(bin);
                }
            }
        }

        return bins;
    }

    public boolean deleteSections(String sectionId) {
        String sql = "UPDATE Section SET Status = 0 WHERE SectionID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sectionId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0; // true if rowsDeleted > 0
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Section> getSectionsByTypeId(int sectionTypeId) {
        String sql = "SELECT * FROM Section WHERE SectionTypeID = ?";
        List<Section> sections = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sectionTypeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Section section = new Section();
                section.setSectionID(rs.getString("SectionID"));
                section.setSectionName(rs.getString("SectionName"));
                section.setSectionTypeId(rs.getInt("SectionTypeID"));
                sections.add(section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sections;
    }


}
