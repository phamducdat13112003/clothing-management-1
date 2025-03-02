package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransferOrderDAO {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Method to get all employee IDs
    public List<String> getAllEmployeeIds() {
        List<String> employeeIds = new ArrayList<>();
        String sql = "SELECT EmployeeID FROM Employee";  // Assuming EmployeeID is the field for the employee ID

        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employeeIds.add(rs.getString("EmployeeID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeIds;
    }

    public List<TransferOrder> getAllTransferOrders() {
        List<TransferOrder> transferOrders = new ArrayList<>();
        String sql = "SELECT * FROM TransferOrder";

        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String toID = rs.getString("TOID");
                LocalDate createdDate = rs.getDate("CreatedDate").toLocalDate();
                String createdBy = rs.getString("CreatedBy");
                String status = rs.getString("Status");
                TransferOrder transferOrder = new TransferOrder(toID, createdDate, createdBy, status);
                transferOrders.add(transferOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transferOrders;
    }

    public TransferOrder getTransferOrderById(String toID) {
        TransferOrder transferOrder = null;
        String sql = "SELECT * FROM TransferOrder WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate createdDate = rs.getDate("CreatedDate").toLocalDate();
                String createdBy = rs.getString("CreatedBy");
                String status = rs.getString("Status");
                transferOrder = new TransferOrder(toID, createdDate, createdBy, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transferOrder;
    }

    public boolean createTransferOrder(TransferOrder transferOrder) {
        String sql = "INSERT INTO TransferOrder (TOID, CreatedDate, CreatedBy, Status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transferOrder.getToID());
            stmt.setDate(2, Date.valueOf(transferOrder.getCreatedDate()));
            stmt.setString(3, transferOrder.getCreatedBy());
            stmt.setString(4, transferOrder.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTransferOrder(TransferOrder transferOrder) {
        String sql = "UPDATE TransferOrder SET CreatedDate = ?, CreatedBy = ?, Status = ? WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(transferOrder.getCreatedDate()));
            stmt.setString(2, transferOrder.getCreatedBy());
            stmt.setString(3, transferOrder.getStatus());
            stmt.setString(4, transferOrder.getToID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelTransferOrder(String toID) {
        String sql = "UPDATE TransferOrder SET Status = 'Inactive' WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTransferOrder(String toID) {
        boolean isDeleted = false;
        String sql = "DELETE FROM transferorder WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, toID);  // Set the TOID to be deleted
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                isDeleted = true;  // Successful deletion
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error
        }
        return isDeleted;  // Return whether the deletion was successful
    }

    public List<TODetail> getTODetailsByTransferOrderId(String toID) {
        List<TODetail> toDetails = new ArrayList<>();
        String sql = "SELECT * FROM TODetail WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TODetail toDetail = new TODetail();
                toDetail.setToDetailID(rs.getString("TODetailID"));
                toDetail.setProductDetailID(rs.getString("ProductDetailID"));
                toDetail.setQuantity(rs.getInt("Quantity"));
                toDetail.setToID(rs.getString("TOID"));
                toDetail.setOriginBinID(rs.getString("OriginBinID"));
                toDetail.setFinalBinID(rs.getString("FinalBinID"));
                toDetails.add(toDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toDetails;
    }

    public boolean isBinValid(String binID) {
        String sql = "SELECT COUNT(*) FROM bin WHERE BinID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if the BinID exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if BinID does not exist
    }

    public boolean isProductDetailIDValid(String productDetailID) {
        String sql = "SELECT COUNT(*) FROM productdetail WHERE ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productDetailID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if the ProductDetailID exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if ProductDetailID does not exist
    }

    public boolean isTransferOrderIDExist(String toID) {
        boolean exists = false;

        // Define SQL query to check if the Transfer Order ID exists
        String query = "SELECT COUNT(*) FROM TransferOrder WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection(); // Assuming getConnection() is your method for getting the DB connection
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameter
            pstmt.setString(1, toID);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // If the count is greater than 0, it means the Transfer Order ID exists
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions as necessary, you can throw custom exceptions or log the error
        }

        return exists;
    }



    // Method to insert a new TODetail into the database
    public boolean addTODetail(TODetail toDetail) {
        String sql = "INSERT INTO TODetail (TODetailID, ProductDetailID, Quantity, TOID, OriginBinID, FinalBinID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set the values in the PreparedStatement
            stmt.setString(1, toDetail.getToDetailID()); // TODetailID (Unique ID)
            stmt.setString(2, toDetail.getProductDetailID()); // ProductDetailID
            stmt.setInt(3, toDetail.getQuantity()); // Quantity
            stmt.setString(4, toDetail.getToID()); // TOID (Transfer Order ID)
            stmt.setString(5, toDetail.getOriginBinID()); // OriginBinID
            stmt.setString(6, toDetail.getFinalBinID()); // FinalBinID

            // Execute the update (insert the TODetail into the database)
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0; // Return true if the insert was successful
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }

        return false; // Return false if an error occurred
    }

    public boolean updateBinQuantity(String binID, String productDetailID, int quantity) {
        // Check if the BinDetail record exists
        String checkQuery = "SELECT * FROM bindetail WHERE BinID = ? AND ProductDetailID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setString(1, binID);
            ps.setString(2, productDetailID);
            ResultSet rs = ps.executeQuery();

            // If record exists, update the quantity
            if (rs.next()) {
                String updateQuery = "UPDATE bindetail SET Quantity = Quantity + ? WHERE BinID = ? AND ProductDetailID = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                    updatePs.setInt(1, quantity);  // Add quantity (either positive or negative)
                    updatePs.setString(2, binID);
                    updatePs.setString(3, productDetailID);
                    int updatedRows = updatePs.executeUpdate();

                    return updatedRows > 0;
                }
            } else {
                // If no record exists, insert a new row
                String insertQuery = "INSERT INTO bindetail (BinID, ProductDetailID, Quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                    insertPs.setString(1, binID);
                    insertPs.setString(2, productDetailID);
                    insertPs.setInt(3, quantity);
                    int insertedRows = insertPs.executeUpdate();

                    return insertedRows > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getBinQuantity(String binID, String productDetailID) {
        int quantity = 0;

        // Define SQL query to fetch the quantity from the BinDetail table
        String query = "SELECT Quantity FROM BinDetail WHERE BinID = ? AND ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection(); // Assuming getConnection() is your method for getting the DB connection
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameters
            pstmt.setString(1, binID);
            pstmt.setString(2, productDetailID);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // If a record is found, get the quantity
            if (rs.next()) {
                quantity = rs.getInt("Quantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions as necessary, you can throw custom exceptions or log the error
        }

        return quantity;
    }






    public boolean updateTODetail(TODetail toDetail) {
        // SQL query to either insert a new record or update an existing one
        String sql = "INSERT INTO todetail (TODetailID, ProductDetailID, Quantity, TOID, OriginBinID, FinalBinID) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE Quantity = ?, OriginBinID = ?, FinalBinID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set values for insert
            stmt.setString(1, toDetail.getToDetailID()); // TODetailID
            stmt.setString(2, toDetail.getProductDetailID()); // ProductDetailID
            stmt.setInt(3, toDetail.getQuantity()); // Quantity
            stmt.setString(4, toDetail.getToID()); // TOID
            stmt.setString(5, toDetail.getOriginBinID()); // OriginBinID
            stmt.setString(6, toDetail.getFinalBinID()); // FinalBinID

            // Set values for update (if TODetailID exists, it will update)
            stmt.setInt(7, toDetail.getQuantity()); // Update quantity
            stmt.setString(8, toDetail.getOriginBinID()); // Update OriginBinID
            stmt.setString(9, toDetail.getFinalBinID()); // Update FinalBinID

            // Execute the statement
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurred
        }
    }

    public TODetail getTODetailByProductDetailID(String toID, String productDetailID) {
        TODetail toDetail = null;
        String sql = "SELECT * FROM todetail WHERE TOID = ? AND ProductDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toID);
            stmt.setString(2, productDetailID);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                toDetail = new TODetail();
                toDetail.setToDetailID(rs.getString("TODetailID"));
                toDetail.setProductDetailID(rs.getString("ProductDetailID"));
                toDetail.setQuantity(rs.getInt("Quantity"));
                toDetail.setToID(rs.getString("TOID"));
                toDetail.setOriginBinID(rs.getString("OriginBinID"));
                toDetail.setFinalBinID(rs.getString("FinalBinID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toDetail;
    }



    public boolean addQuantityToFinalBin(String binID, int quantity) {
        String sql = "UPDATE bin SET Quantity = Quantity + ? WHERE BinID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setString(2, binID);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public List<ProductDetail> searchProductDetails(String query) {
        List<ProductDetail> productDetailsList = new ArrayList<>();
        String sql = "SELECT pd.ProductDetailID, pd.Weight, p.ProductName " +
                "FROM productdetail pd " +
                "JOIN product p ON pd.ProductID = p.ProductID " +
                "WHERE pd.ProductDetailID LIKE ? OR p.ProductName LIKE ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the query parameter to search in ProductDetailID or ProductName
            String searchQuery = "%" + query + "%";  // Adding % for partial match
            stmt.setString(1, searchQuery);
            stmt.setString(2, searchQuery);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductDetail productDetail = new ProductDetail();
                // Use Long.valueOf to convert to the correct data type for ProductDetailID
                productDetail.setProductDetailID(rs.getString("ProductDetailID"));  // Assuming ProductDetailID is a string
                productDetail.setWeight(rs.getDouble("Weight"));
                productDetail.setProductName(rs.getString("ProductName"));

                productDetailsList.add(productDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetailsList;
    }

}
