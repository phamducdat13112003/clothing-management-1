package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Warehouse;
import org.example.clothingmanagement.repository.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WareHouseDAO {
    public List<Warehouse> getAllWareHouse(){
        List<Warehouse> list = new ArrayList<>();
        String sql = "select * from warehouse";
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Warehouse w = new Warehouse();
                w.setId(rs.getInt("WarehouseID"));
                w.setWarehouseName(rs.getString("WarehouseName"));
                w.setAddress(rs.getString("Address"));
                w.setBranchId(rs.getInt("BranchID"));
                list.add(w);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
