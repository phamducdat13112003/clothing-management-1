package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.SectionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
