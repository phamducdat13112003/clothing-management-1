package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.SectionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectionTypeDAO {




    public List<SectionType> getAllSectionTypes() {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SectionTypeID, SectionTypeName, WarehouseID, Description ");
            sql.append(" FROM SectionType ");
            sql.append(" ORDER BY SectionTypeID");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<SectionType> sectionTypes = new ArrayList<>();
            while (rs.next()) {
                SectionType sectionType = SectionType.builder()
                        .sectionTypeId(rs.getInt("SectionTypeID"))
                        .sectionTypeName(rs.getString("SectionTypeName"))
                        .warehouseId(rs.getString("WarehouseID"))
                        .description(rs.getString("Description"))
                        .build();
                sectionTypes.add(sectionType);
            }
            return sectionTypes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SectionType> getSectionTypesWithPagination(int page, int pageSize) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SectionTypeID, SectionTypeName, WarehouseID, Description ");
            sql.append(" FROM SectionType ");
            sql.append(" ORDER BY SectionTypeID");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setInt(1, pageSize);
            ps.setInt(2, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<SectionType> sectionTypes = new ArrayList<>();
            while (rs.next()) {
                SectionType sectionType = SectionType.builder()
                        .sectionTypeId(rs.getInt("SectionTypeID"))
                        .sectionTypeName(rs.getString("SectionTypeName"))
                        .warehouseId(rs.getString("WarehouseID"))
                        .description(rs.getString("Description"))
                        .build();
                sectionTypes.add(sectionType);
            }
            return sectionTypes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SectionType> searchSectionTypes(String search) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SectionTypeID, SectionTypeName, WarehouseID, Description ");
            sql.append(" FROM SectionType ");
            sql.append(" WHERE 1=1 ");

            if(!search.isEmpty()){
                sql.append(" AND (SectionTypeName LIKE ? ");
                sql.append(" OR Description LIKE ?) ");
            }
            sql.append(" ORDER BY SectionTypeID");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            if(!search.isEmpty()){
                ps.setString(paramIndex++, "%"+search+"%");
                ps.setString(paramIndex++, "%"+search+"%");
            }
            ResultSet rs = ps.executeQuery();
            List<SectionType> sectionTypes = new ArrayList<>();
            while (rs.next()) {
                SectionType sectionType = SectionType.builder()
                        .sectionTypeId(rs.getInt("SectionTypeID"))
                        .sectionTypeName(rs.getString("SectionTypeName"))
                        .warehouseId(rs.getString("WarehouseID"))
                        .description(rs.getString("Description"))
                        .build();
                sectionTypes.add(sectionType);
            }
            return sectionTypes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SectionType> searchSectionTypesWithPagination(String search,int page, int pageSize) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SectionTypeID, SectionTypeName, WarehouseID, Description ");
            sql.append(" FROM SectionType ");
            sql.append(" WHERE 1=1 ");
            if(!search.isEmpty()){
                sql.append(" AND (SectionTypeName LIKE ? ");
                sql.append(" OR Description LIKE ?) ");
            }
            sql.append(" ORDER BY SectionTypeID");
            sql.append(" LIMIT ? OFFSET ? ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            if(!search.isEmpty()){
                ps.setString(paramIndex++, "%"+search+"%");
                ps.setString(paramIndex++, "%"+search+"%");
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page-1)*pageSize);
            ResultSet rs = ps.executeQuery();
            List<SectionType> sectionTypes = new ArrayList<>();
            while (rs.next()) {
                SectionType sectionType = SectionType.builder()
                        .sectionTypeId(rs.getInt("SectionTypeID"))
                        .sectionTypeName(rs.getString("SectionTypeName"))
                        .warehouseId(rs.getString("WarehouseID"))
                        .description(rs.getString("Description"))
                        .build();
                sectionTypes.add(sectionType);
            }
            return sectionTypes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public SectionType getSectionTypeById(int sectionTypeId) {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SectionTypeID, SectionTypeName, WarehouseID, Description ");
            sql.append(" FROM SectionType ");
            sql.append(" WHERE SectionTypeID = ? ");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setInt(1, sectionTypeId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                SectionType sectionType = SectionType.builder()
                        .sectionTypeId(rs.getInt("SectionTypeID"))
                        .sectionTypeName(rs.getString("SectionTypeName"))
                        .warehouseId(rs.getString("WarehouseID"))
                        .description(rs.getString("Description"))
                        .build();
                return sectionType;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SectionType getSectionTypeBySectionId(String sectionId) {
        try (Connection con = DBContext.getConnection()) {
            // Truy vấn trực tiếp thông tin của SectionType từ SectionID
            String sql = "SELECT st.SectionTypeID, st.SectionTypeName, st.WarehouseID, st.Description " +
                    "FROM Section s " +
                    "JOIN SectionType st ON s.SectionTypeID = st.SectionTypeID " +
                    "WHERE s.SectionID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sectionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new SectionType(
                        rs.getInt("SectionTypeID"),
                        rs.getString("SectionTypeName"),
                        rs.getString("WarehouseID"),
                        rs.getString("Description")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Trả về null nếu không tìm thấy SectionType
    }

    public String getSectionTypeNameById(int sectionTypeID) {
        String sectionTypeName = null;
        String sql = "SELECT SectionTypeName FROM SectionType WHERE SectionTypeID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionTypeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sectionTypeName = rs.getString("SectionTypeName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sectionTypeName;
    }
}
