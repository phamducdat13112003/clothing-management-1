package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Warehouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {
    public List<Warehouse> getAllWareHouse(){
        List<Warehouse> list = new ArrayList<>();
        String sql = "select * from warehouse";
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Warehouse w = new Warehouse();
                w.setWarehouseId(rs.getString("WarehouseID"));
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

    // Create a new warehouse
    public void createWarehouse(Warehouse warehouse) {
        String sql = "INSERT INTO warehouse (WarehouseName, BranchID, Address) VALUES (?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, warehouse.getWarehouseName());
            stmt.setInt(2, warehouse.getBranchId());
            stmt.setString(3, warehouse.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating warehouse", e);
        }
    }

    // Update an existing warehouse
    public void updateWarehouse(Warehouse warehouse) {
        String sql = "UPDATE warehouse SET WarehouseName = ?, BranchID = ?, Address = ? WHERE WarehouseID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, warehouse.getWarehouseName());
            stmt.setInt(2, warehouse.getBranchId());
            stmt.setString(3, warehouse.getAddress());
            stmt.setString(4, warehouse.getWarehouseId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while updating warehouse", e);
        }
    }

    // WarehouseDAO class
    public void deleteWarehouse(int warehouseID) {
        String sql = "DELETE FROM warehouse WHERE WarehouseID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, warehouseID);
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // Handle the exception when there are employees associated with the warehouse
            throw new RuntimeException("Cannot delete warehouse. There are employees associated with this warehouse.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while deleting warehouse", e);
        }
    }


    public Warehouse getWarehouseById(int warehouseID) {
        String sql = "SELECT * FROM warehouse WHERE WarehouseID = ?";
        Warehouse warehouse = null;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the warehouse ID parameter
            stmt.setInt(1, warehouseID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create a new Warehouse object and populate it with the data
                    warehouse = new Warehouse();
                    warehouse.setWarehouseId(rs.getString("WarehouseID"));
                    warehouse.setWarehouseName(rs.getString("WarehouseName"));
                    warehouse.setBranchId(rs.getInt("BranchID"));
                    warehouse.setAddress(rs.getString("Address"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while retrieving warehouse by ID", e);
        }

        return warehouse; // Return the warehouse object or null if not found

    }

    // Retrieve warehouse name by ID
    public static String getWarehouseNameById(int warehouseID) throws SQLException {
        String sql = "SELECT WarehouseName FROM Warehouse WHERE WarehouseID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            pt.setInt(1, warehouseID);
            try (ResultSet resultSet = pt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("WarehouseName");
                }
            }
        }
        return null; // Return null if not found
    }


    public static void main(String[] args) {
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        for(Warehouse warehouse : warehouseDAO.getAllWareHouse()){
            System.out.println(warehouse.getWarehouseName());
        }
    }
}

