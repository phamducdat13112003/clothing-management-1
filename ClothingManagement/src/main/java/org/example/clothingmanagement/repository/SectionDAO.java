package org.example.clothingmanagement.repository;

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
            sql.append(" Where sectionTypeId = ? ");
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
            sql.append(" Where sectionTypeId = ? ");
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
            String sql = "SELECT sectionID FROM section ORDER BY sectionID DESC LIMIT 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                String lastID = rs.getString("sectionID");
                int numericPart = Integer.parseInt(lastID.substring(2));
                numericPart++;
                return String.format("SE%03d", numericPart);
            } else {
                return "SE001"; // First section
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Section> getAllSections() {
        try(Connection con = DBContext.getConnection()){
            String sql = "SELECT sectionID, sectionName, sectionTypeID FROM section";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<Section> sections = new ArrayList<>();
            while(rs.next()){
                Section section = Section.builder()
                        .sectionID(rs.getString("sectionID"))
                        .sectionName(rs.getString("sectionName"))
                        .sectionTypeId(rs.getInt("sectionTypeID"))
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


}
