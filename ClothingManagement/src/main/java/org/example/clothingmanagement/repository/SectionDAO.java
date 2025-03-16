package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.service.SectionService;

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

    public static void main(String[] args) {
        SectionDAO sd = new SectionDAO();
        List<Section> list = sd.searchSectionWithPagination(2,"",1,5);
        for(Section section : list){
            System.out.println(section);
        }

    }
}
