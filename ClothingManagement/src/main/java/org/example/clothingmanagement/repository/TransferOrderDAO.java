package org.example.clothingmanagement.repository;

import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        String sql = "UPDATE TransferOrder SET Status = 'Cancelled' WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateNextTOID() throws SQLException {
        String sql = "SELECT TOP 1 toID FROM TransferOrder ORDER BY toID DESC";
        String latestToID = null;

        try (Connection connection = DBContext.getConnection(); // Make sure to use your connection logic
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                latestToID = resultSet.getString("toID");
            }
        }

        // If no Transfer Order exists, start from TO001
        if (latestToID == null) {
            return "TO001";
        }

        // Extract the numeric part and increment it
        String numericPart = latestToID.substring(2); // Get the part after "TO"
        int nextNumber = Integer.parseInt(numericPart) + 1;

        // Format the number to always have 3 digits
        return "TO" + String.format("%03d", nextNumber);
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

    public String getNextToID() {
        String nextToID = "TO001"; // Default first ID
        String sql = "SELECT MAX(toID) FROM transferorder";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getString(1) != null) {
                String lastToID = rs.getString(1);

                // Extract the numeric part
                String numericPart = lastToID.substring(2); // Remove "TO" prefix

                // Parse to integer and increment
                int nextNumber = Integer.parseInt(numericPart) + 1;

                // Format with leading zeros (TO001, TO002, etc.)
                nextToID = "TO" + String.format("%03d", nextNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // If there's an error, use the default TO001
        }

        return nextToID;
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

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameter
            pstmt.setString(1, toID);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // If the count is greater than 0, it means the Transfer Order ID exists
            if (rs.next()) {
                int count = rs.getInt(1);
                exists = count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking Transfer Order ID: " + toID);
            System.out.println("SQL Exception: " + e.getMessage());
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
        System.out.println("Searching with query: '" + query + "'");
        List<ProductDetail> productDetailsList = new ArrayList<>();

        String sql = "SELECT pd.ProductDetailID, pd.Weight, p.ProductName " +
                "FROM productdetail pd " +
                "JOIN product p ON pd.ProductID = p.ProductID " +
                "WHERE pd.ProductDetailID LIKE ? OR p.ProductName LIKE ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchQuery = "%" + query + "%";
            System.out.println("SQL search pattern: '" + searchQuery + "'");

            stmt.setString(1, searchQuery);
            stmt.setString(2, searchQuery);

            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                ProductDetail productDetail = new ProductDetail();
                String id = rs.getString("ProductDetailID");
                String name = rs.getString("ProductName");

                System.out.println("Found product: ID=" + id + ", Name=" + name);

                productDetail.setId(id);
                productDetail.setWeight(rs.getDouble("Weight"));
                productDetail.setProductName(name);

                productDetailsList.add(productDetail);
            }
            System.out.println("Total products found: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetailsList;
    }

    public List<String> getAllBinIds() {
        List<String> binIds = new ArrayList<>();
        String sql = "SELECT BinID FROM bin";  // Adjust based on your table and column names

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String binId = rs.getString("BinID");  // Replace with the actual column name in your database
                binIds.add(binId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return binIds;
    }


    public double getBinMaxCapacity(String binID) {
        double maxCapacity = 0.0;

        // Query the database to get the max capacity for the given finalBinID
        String query = "SELECT MaxCapacity FROM bin WHERE BinID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, binID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                maxCapacity = resultSet.getDouble("maxCapacity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (you could throw it or log it)
        }

        return maxCapacity;
    }

    public double getBinCurrentCapacity(String binID) {
        double currentCapacity = 0.0;

        // SQL query to retrieve the current capacity from the 'bin' table
        String query = "SELECT CurrentCapacity FROM bin WHERE BinID = ?";

        // Execute the query and get the current capacity
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the bin ID parameter
            statement.setString(1, binID);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // If the result exists, get the current capacity value
            if (resultSet.next()) {
                currentCapacity = resultSet.getDouble("CurrentCapacity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (e.g., log or rethrow)
        }

        return currentCapacity;
    }


    public double getProductWeight(String productDetailID) {
        double weight = 0.0;

        // Query the database to get the weight of the product based on productDetailID
        String query = "SELECT weight FROM productdetail WHERE productDetailID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, productDetailID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                weight = resultSet.getDouble("weight");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (you could throw it or log it)
        }

        return weight;
    }


    // Method 2: With Capacity Limit Validation
    public boolean updateBinCurrentCapacity(String binID, double weightToAdd, double maxCapacity) {
        // First, check if adding the weight would exceed max capacity
        String checkSql = "SELECT currentCapacity FROM Bin WHERE binID = ?";
        String updateSql = "UPDATE Bin SET currentCapacity = currentCapacity + ? WHERE binID = ? AND (currentCapacity + ?) <= ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {

            // First, check current capacity
            checkStmt.setString(1, binID);

            // Perform the update with capacity limit
            updateStmt.setDouble(1, weightToAdd);
            updateStmt.setString(2, binID);
            updateStmt.setDouble(3, weightToAdd);
            updateStmt.setDouble(4, maxCapacity);

            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected == 0) {
                // This means the update failed due to capacity limit
                System.err.println("Cannot add weight. Bin capacity would be exceeded.");
                return false;
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error updating bin capacity: " + e.getMessage());
            return false;
        }
    }
}
