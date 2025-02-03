package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> getAllProducts() {
       try(Connection conn = DBContext.getConnection()){
           StringBuilder sql = new StringBuilder();
           sql.append(" SELECT ProductID, ProductName, Price, binID, CategoryID, Material, Gender, Seasons, MinQuantity, CreatedDate, Description, CreatedBy, SupplierID, MadeIn FROM Product  ");
           PreparedStatement ps = conn.prepareStatement(sql.toString());
           ResultSet rs = ps.executeQuery();
           List<Product> products = new ArrayList<Product>();
           while(rs.next()){
               Product product = Product.builder()
                       .id(rs.getLong("ProductID"))
                       .name(rs.getString("ProductName"))
                       .price(rs.getDouble("Price"))
                       .binId(rs.getInt("binID"))
                       .categoryId(rs.getInt("CategoryID"))
                       .material(rs.getString("Material"))
                       .gender(rs.getString("Gender"))
                       .seasons(rs.getString("Seasons"))
                       .minQuantity(rs.getInt("MinQuantity"))
                       .createdDate(rs.getDate("CreatedDate"))
                       .description(rs.getString("Description"))
                       .createdBy(rs.getInt("CreatedBy"))
                       .supplierId(rs.getInt("SupplierID"))
                       .madeIn(rs.getString("MadeIn"))
                       .build();
               products.add(product);

           }
           return products;

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

    }




}
