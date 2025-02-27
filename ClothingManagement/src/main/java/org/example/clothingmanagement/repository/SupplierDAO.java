package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    public List<Supplier> getAllSuppliers() {
        try(Connection con = DBContext.getConnection()){
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SupplierID, SupplierName, Address, ContactEmail, Phone FROM Supplier ");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            List<Supplier> suppliers = new ArrayList<>();
            while(rs.next()){
                Supplier supplier = Supplier.builder()
                        .id(rs.getString("SupplierID"))
                        .name(rs.getString("SupplierName"))
                        .address(rs.getString("Address"))
                        .email(rs.getString("ContactEmail"))
                        .phone(rs.getString("Phone"))
                        .status(rs.getBoolean("Status"))
                        .build();
                suppliers.add(supplier);
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
