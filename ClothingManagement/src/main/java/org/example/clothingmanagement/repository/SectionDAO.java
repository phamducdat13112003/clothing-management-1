package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Section;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO {
    public List<Section> getAllSection(){
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" Select sectionId, sectionName, sectionTypeId ");
            sql.append(" From section ");
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
}
