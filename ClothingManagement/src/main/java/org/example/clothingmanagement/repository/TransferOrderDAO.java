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
        // SQL query to join TODetail, ProductDetail, and Product to get product name
        String sql = "SELECT td.*, p.ProductName, pd.Weight " +
                "FROM TODetail td " +
                "JOIN ProductDetail pd ON td.ProductDetailID = pd.ProductDetailID " +
                "JOIN Product p ON pd.ProductID = p.ProductID " +  // Join Product to get ProductName
                "WHERE td.TOID = ?";

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

                // Retrieve the ProductName from the join
                String productName = rs.getString("ProductName");
                toDetail.setProductName(productName);  // Set the product name in TODetail
                double weight = rs.getDouble("Weight");
                toDetail.setWeight(weight);

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


    public boolean addTODetail(TODetail toDetail) {
        String sql = "INSERT INTO todetail (TODetailID, ProductDetailID, Quantity, TOID, OriginBinId, FinalBinId) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, toDetail.getToDetailID());
            pstmt.setString(2, toDetail.getProductDetailID());
            pstmt.setInt(3, toDetail.getQuantity());
            pstmt.setString(4, toDetail.getToID());
            pstmt.setString(5, toDetail.getOriginBinID());
            pstmt.setString(6, toDetail.getFinalBinID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method to update the quantity of a product in a bin or create a new bin detail if it doesn't exist
    public boolean updateBinQuantity(Connection conn, String binID, String productDetailID, int quantity) throws SQLException {
        System.out.println("Updating bin " + binID + " with product " + productDetailID + " by quantity " + quantity);

        // First check if the bin detail record already exists
        String checkQuery = "SELECT * FROM bindetail WHERE BinID = ? AND ProductDetailID = ?";

        try (PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setString(1, binID);
            ps.setString(2, productDetailID);

            try (ResultSet rs = ps.executeQuery()) {
                // If the record exists, update the quantity
                if (rs.next()) {
                    System.out.println("Record exists. Current quantity: " + rs.getInt("Quantity"));
                    String updateQuery = "UPDATE bindetail SET Quantity = Quantity + ? WHERE BinID = ? AND ProductDetailID = ?";

                    try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                        updatePs.setInt(1, quantity);  // Add quantity (either positive or negative)
                        updatePs.setString(2, binID);
                        updatePs.setString(3, productDetailID);

                        int updatedRows = updatePs.executeUpdate();
                        System.out.println("Update result: " + (updatedRows > 0 ? "Success" : "Failed"));

                        return updatedRows > 0;
                    }
                } else {
                    // If no record exists, generate a new bin detail ID
                    String binDetailID = generateNewBinDetailID(conn, binID);
                    System.out.println("Record doesn't exist. Creating new record with ID: " + binDetailID + " and quantity: " + quantity);

                    // Insert a new bin detail record
                    String insertQuery = "INSERT INTO bindetail (BinDetailID, BinID, ProductDetailID, Quantity) VALUES (?, ?, ?, ?)";

                    try (PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                        insertPs.setString(1, binDetailID);
                        insertPs.setString(2, binID);
                        insertPs.setString(3, productDetailID);
                        insertPs.setInt(4, quantity);

                        int insertedRows = insertPs.executeUpdate();
                        System.out.println("Insert result: " + (insertedRows > 0 ? "Success" : "Failed"));

                        return insertedRows > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error updating bin quantity: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception to be handled by the calling method
        }
    }

    // Method to generate a new BinDetailID based on the highest existing ID for the bin
    private String generateNewBinDetailID(Connection conn, String binID) throws SQLException {
        String query = "SELECT BinDetailID FROM bindetail WHERE BinID = ? ORDER BY BinDetailID DESC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            // Set the BinID parameter
            ps.setString(1, binID);

            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String highestID = rs.getString("BinDetailID");

                    // Extract the sequential number part after the last dash
                    int lastDashIndex = highestID.lastIndexOf("-");
                    if (lastDashIndex != -1 && lastDashIndex < highestID.length() - 1) {
                        String sequentialPart = highestID.substring(lastDashIndex + 1);

                        try {
                            // Parse the sequential number and increment it
                            int sequentialNumber = Integer.parseInt(sequentialPart);
                            // Format with leading zeros (01, 02, etc.)
                            return binID + "-" + String.format("%02d", sequentialNumber + 1);
                        } catch (NumberFormatException e) {
                            // If parsing fails, default to starting with 01
                            return binID + "-01";
                        }
                    }
                }

                // If no existing records found or format is unexpected, start with 01
                return binID + "-01";
            }
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


    public double getCurrentBinWeight(String binID) {
        double totalWeight = 0.0;
        String query = "SELECT bd.ProductDetailId, bd.quantity, pd.Weight " +
                "FROM bindetail bd " +
                "JOIN productdetail pd ON bd.ProductDetailId = pd.ProductDetailID " +
                "WHERE bd.binId = ?";

        System.out.println("Checking weight for bin: " + binID);

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, binID);
            System.out.println("Executing query: " + query.replace("?", "'" + binID + "'"));

            ResultSet rs = stmt.executeQuery();
            int productCount = 0;

            while (rs.next()) {
                productCount++;
                String productId = rs.getString("ProductDetailId");
                double weight = rs.getDouble("Weight");
                int quantity = rs.getInt("quantity");
                double productTotalWeight = weight * quantity;

                System.out.println("Product: " + productId +
                        ", Weight: " + weight +
                        " kg, Quantity: " + quantity +
                        ", Total: " + productTotalWeight + " kg");

                totalWeight += productTotalWeight;
            }

            System.out.println("Found " + productCount + " products in bin");

            if (productCount == 0) {
                // Try a simpler query to see if the bin exists
                String checkBinQuery = "SELECT COUNT(*) FROM bindetail WHERE binId = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkBinQuery)) {
                    checkStmt.setString(1, binID);
                    ResultSet checkRs = checkStmt.executeQuery();
                    if (checkRs.next()) {
                        int count = checkRs.getInt(1);
                        System.out.println("Bin detail count from direct query: " + count);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Final total weight: " + totalWeight + " kg");
        return totalWeight;
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

    public boolean updateTODetailFinalBin(TODetail toDetail) {
        String sql = "UPDATE TODetail SET finalBinID = ? WHERE toDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, toDetail.getFinalBinID());
            stmt.setString(2, toDetail.getToDetailID());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public List<ProductDetail> searchProductDetailsByBin(String query, String binID) {
        List<ProductDetail> productDetailList = new ArrayList<>();

        // Query to get products that match the search query and are in the specified bin
        String sql = "SELECT pd.ProductDetailID, p.ProductName, pd.Weight, bd.quantity " +
                "FROM productdetail pd " +
                "JOIN product p ON pd.ProductID = p.ProductID " +
                "JOIN bindetail bd ON pd.ProductDetailID = bd.ProductDetailId " +
                "WHERE bd.binId = ? AND " +
                "(pd.ProductDetailID LIKE ? OR p.ProductName LIKE ?) " +
                "AND pd.Status = 1";  // Assuming Status = 1 means active products

        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, binID);
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ProductDetail product = new ProductDetail();
                product.setId(resultSet.getString("ProductDetailID"));
                product.setProductName(resultSet.getString("ProductName"));
                product.setWeight(resultSet.getDouble("Weight"));
                product.setQuantity(resultSet.getInt("quantity"));  // Get quantity from bindetail

                productDetailList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (you could throw it or log it)
        }

        return productDetailList;
    }


    public List<ProductDetail> searchProductDetails(String query) {
        System.out.println("Searching with query: '" + query + "'");
        List<ProductDetail> productDetailsList = new ArrayList<>();

        String sql = "SELECT pd.ProductDetailID, pd.Weight, p.ProductName, pd.ProductImage " +
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
                String image = rs.getString("ProductImage");

                System.out.println("Found product: ID=" + id + ", Name=" + name + ", Image=" + image);

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

    public String getEmployeeNameByID(String employeeID) throws SQLException {
        String employeeName = "";
        String query = "SELECT EmployeeName FROM Employee WHERE EmployeeID = ?";

        try(Connection connection = DBContext.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employeeID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employeeName = resultSet.getString("EmployeeName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeName;
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

    public TransferOrder getTransferOrderByID(String toID) {
        TransferOrder transferOrder = null;
        String sql = "SELECT * FROM transferorder WHERE toID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, toID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    transferOrder = new TransferOrder();
                    transferOrder.setToID(rs.getString("toID"));
                    transferOrder.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                    transferOrder.setCreatedBy(rs.getString("createdBy"));
                    transferOrder.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transferOrder;
    }

    public List<TODetail> getTODetailsByTOID(String toID) {
        List<TODetail> details = new ArrayList<>();
        String sql = "SELECT toDetailID, toID, productDetailID, quantity, originBinID, finalBinID FROM todetail WHERE toID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, toID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TODetail detail = new TODetail();
                    detail.setToDetailID(rs.getString("toDetailID"));
                    detail.setToID(rs.getString("toID"));
                    detail.setProductDetailID(rs.getString("productDetailID"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setOriginBinID(rs.getString("originBinID"));
                    detail.setFinalBinID(rs.getString("finalBinID"));
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return details;
    }

    public double getProcessingTransferTotalWeight(String finalBinID) {
        double totalPendingWeight = 0.0;
        String sql = "SELECT td.quantity, pd.weight " +
                "FROM TODetail td " +
                "JOIN TransferOrder t ON td.toID = t.toID " +
                "JOIN ProductDetail pd ON td.productDetailID = pd.productDetailID " +
                "WHERE td.finalBinID = ? AND t.status = 'Processing'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, finalBinID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int quantity = rs.getInt("quantity");
                    double weight = rs.getDouble("weight");
                    totalPendingWeight += quantity * weight;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error calculating pending transfer weight: " + e.getMessage());
        }

        return totalPendingWeight;
    }

    public boolean updateBinStatus(Connection conn, String binID, int status) throws SQLException {
        String sql = "UPDATE Bin SET Status = ? WHERE BinID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, status);
            stmt.setString(2, binID);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw to allow for transaction rollback
        }
    }

    public boolean updateTransferOrderStatus(String toID, String status) {
        String sql = "UPDATE transferorder SET status = ? WHERE toID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, toID);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isProductInBin(String binID, String productDetailID) {
        String query = "SELECT COUNT(*) FROM bindetail WHERE binId = ? AND productDetailId = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, binID);
            stmt.setString(2, productDetailID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count is greater than 0, product exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public int countTransferOrdersPending() {
        String sql = "SELECT COUNT(*) FROM TransferOrder WHERE Status = 'Pending'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateTransferOrderStatus(String toID) {
        String sql = "UPDATE TransferOrder SET Status = 'Processing' WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TransferOrder> getTransferOrdersPending(int page, int pageSize) {
        List<TransferOrder> transferOrders = new ArrayList<>();
        String sql = "SELECT * FROM TransferOrder WHERE Status ='Pending' LIMIT ? OFFSET ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
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

    public List<TransferOrder> searchTransferOrdersByTOID(String toID, int page, int pageSize) {
        List<TransferOrder> transferOrders = new ArrayList<>();
        String sql = "SELECT * FROM TransferOrder WHERE Status = 'Pending' AND TOID LIKE ? LIMIT ?, ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int offset = (page - 1) * pageSize;
            stmt.setString(1, "%" + toID + "%"); // Tìm kiếm TOID có chứa từ khóa
            stmt.setInt(2, offset);
            stmt.setInt(3, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String foundToID = rs.getString("TOID");
                    LocalDate createdDate = rs.getDate("CreatedDate").toLocalDate();
                    String createdBy = rs.getString("CreatedBy");
                    String status = rs.getString("Status");
                    TransferOrder transferOrder = new TransferOrder(foundToID, createdDate, createdBy, status);
                    transferOrders.add(transferOrder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transferOrders;
    }

    public int countTransferOrdersByTOID(String toID) {
        String sql = "SELECT COUNT(*) FROM TransferOrder WHERE Status = 'Pending' AND TOID LIKE ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + toID + "%"); // Tìm kiếm TOID có chứa từ khóa
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }




    public static void main(String[] args) {
        // Create an instance of your DAO class
        TransferOrderDAO transferOrderDAO = new TransferOrderDAO();

        // Test bin IDs - replace these with actual bin IDs from your database
        String[] testBinIds = {"FW001-002"};

        // Test the getCurrentBinWeight method for each bin
        for (String binId : testBinIds) {
            try {
                double weight = transferOrderDAO.getCurrentBinWeight(binId);
                System.out.println("Bin ID: " + binId);
                System.out.println("Total Weight: " + weight + " kg");
                System.out.println("------------------------------");
            } catch (Exception e) {
                System.out.println("Error testing bin " + binId + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public boolean removeTODetail(String toDetailID) {
        // SQL to delete a TODetail record
        String sql = "DELETE FROM TODetail WHERE toDetailID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toDetailID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean hasProcessingTO(String binID) {
        String sql = "SELECT COUNT(*) AS count " +
                "FROM TODetail td " +
                "JOIN TransferOrder to1 ON td.TOID = to1.TOID " +
                "WHERE (td.OriginBinId = ? OR td.FinalBinId = ?) " +
                "AND (to1.Status != 'Done' OR to1.Status != 'Cancel')";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, binID);
            stmt.setString(2, binID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;  // Nếu có đơn TO processing → không xóa duoc bin
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String getSectionByBinID(String binID) {
        String sectionID = null;
        String sql = "SELECT sectionID FROM bin WHERE binID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, binID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sectionID = rs.getString("sectionID");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting section by bin ID: " + e.getMessage());
        }

        return sectionID;
    }

    public List<TransferOrder> getTransferOrdersWithPaginationAndSearch(int offset, int limit, String search) {
        List<TransferOrder> transferOrders = new ArrayList<>();

        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT * FROM transferorder WHERE TOID LIKE ? OR CreatedBy LIKE ? OR Status LIKE ? " +
                    "ORDER BY CreatedDate DESC LIMIT ?, ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String searchPattern = "%" + search + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                ps.setString(3, searchPattern);
                ps.setInt(4, offset);
                ps.setInt(5, limit);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        TransferOrder to = new TransferOrder();
                        to.setToID(rs.getString("TOID"));
                        to.setCreatedBy(rs.getString("CreatedBy"));
                        to.setStatus(rs.getString("Status"));
                        to.setCreatedDate(rs.getDate("CreatedDate").toLocalDate());
                        transferOrders.add(to);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting transfer orders with pagination: " + e.getMessage());
            e.printStackTrace();
        }

        return transferOrders;
    }


    public int getTotalTransferOrders(String search) {
        int count = 0;

        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT COUNT(*) FROM transferorder WHERE TOID LIKE ? OR CreatedBy LIKE ? OR Status LIKE ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String searchPattern = "%" + search + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                ps.setString(3, searchPattern);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting transfer orders: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    public List<String> getAllDistinctStatuses() {
        List<String> statuses = new ArrayList<>();

        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT DISTINCT Status FROM transferorder ORDER BY Status";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String status = rs.getString("Status");
                    if (status != null && !status.isEmpty()) {
                        statuses.add(status);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting distinct statuses: " + e.getMessage());
            e.printStackTrace();
        }

        return statuses;
    }

    /**
     * Lấy danh sách các người tạo khác nhau của Transfer Order
     * @return Danh sách các người tạo
     */
    public List<String> getAllDistinctCreatedBy() {
        List<String> creators = new ArrayList<>();

        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT DISTINCT CreatedBy FROM transferorder ORDER BY CreatedBy";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String createdBy = rs.getString("CreatedBy");
                    if (createdBy != null && !createdBy.isEmpty()) {
                        creators.add(createdBy);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting distinct creators: " + e.getMessage());
            e.printStackTrace();
        }

        return creators;
    }

    /**
     * Lấy danh sách Transfer Order có phân trang, tìm kiếm và filter
     */
    public List<TransferOrder> getTransferOrdersWithPaginationAndFilter(int offset, int limit, String search,
                                                                        String statusFilter, String dateFrom,
                                                                        String dateTo, String createdBy) {
        List<TransferOrder> transferOrders = new ArrayList<>();

        try (Connection conn = DBContext.getConnection()) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT * FROM transferorder WHERE 1=1");

            // Thêm điều kiện tìm kiếm
            if (search != null && !search.trim().isEmpty()) {
                sqlBuilder.append(" AND (TOID LIKE ? OR CreatedBy LIKE ? OR Status LIKE ?)");
            }

            // Thêm điều kiện filter theo status
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                sqlBuilder.append(" AND Status = ?");
            }

            // Thêm điều kiện filter theo khoảng thời gian
            if (dateFrom != null && !dateFrom.trim().isEmpty()) {
                sqlBuilder.append(" AND CreatedDate >= ?");
            }

            if (dateTo != null && !dateTo.trim().isEmpty()) {
                sqlBuilder.append(" AND CreatedDate <= ?");
            }

            // Thêm điều kiện filter theo người tạo
            if (createdBy != null && !createdBy.trim().isEmpty()) {
                sqlBuilder.append(" AND CreatedBy = ?");
            }

            sqlBuilder.append(" ORDER BY CreatedDate DESC LIMIT ?, ?");

            try (PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {
                int paramIndex = 1;

                // Đặt các tham số tìm kiếm
                if (search != null && !search.trim().isEmpty()) {
                    String searchPattern = "%" + search + "%";
                    ps.setString(paramIndex++, searchPattern);
                    ps.setString(paramIndex++, searchPattern);
                    ps.setString(paramIndex++, searchPattern);
                }

                // Đặt các tham số filter
                if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                    ps.setString(paramIndex++, statusFilter);
                }

                if (dateFrom != null && !dateFrom.trim().isEmpty()) {
                    ps.setDate(paramIndex++, java.sql.Date.valueOf(dateFrom));
                }

                if (dateTo != null && !dateTo.trim().isEmpty()) {
                    ps.setDate(paramIndex++, java.sql.Date.valueOf(dateTo));
                }

                if (createdBy != null && !createdBy.trim().isEmpty()) {
                    ps.setString(paramIndex++, createdBy);
                }

                // Đặt các tham số limit và offset
                ps.setInt(paramIndex++, offset);
                ps.setInt(paramIndex, limit);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        TransferOrder to = new TransferOrder();
                        to.setToID(rs.getString("TOID"));
                        to.setCreatedBy(rs.getString("CreatedBy"));
                        to.setStatus(rs.getString("Status"));
                        to.setCreatedDate(rs.getDate("CreatedDate").toLocalDate());
                        transferOrders.add(to);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting transfer orders with filter: " + e.getMessage());
            e.printStackTrace();
        }

        return transferOrders;
    }


    public int getTotalTransferOrdersWithFilter(String search, String statusFilter,
                                                String dateFrom, String dateTo, String createdBy) {
        int count = 0;

        try (Connection conn = DBContext.getConnection()) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT COUNT(*) FROM transferorder WHERE 1=1");

            // Thêm điều kiện tìm kiếm
            if (search != null && !search.trim().isEmpty()) {
                sqlBuilder.append(" AND (TOID LIKE ? OR CreatedBy LIKE ? OR Status LIKE ?)");
            }

            // Thêm điều kiện filter theo status
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                sqlBuilder.append(" AND Status = ?");
            }

            // Thêm điều kiện filter theo khoảng thời gian
            if (dateFrom != null && !dateFrom.trim().isEmpty()) {
                sqlBuilder.append(" AND CreatedDate >= ?");
            }

            if (dateTo != null && !dateTo.trim().isEmpty()) {
                sqlBuilder.append(" AND CreatedDate <= ?");
            }

            // Thêm điều kiện filter theo người tạo
            if (createdBy != null && !createdBy.trim().isEmpty()) {
                sqlBuilder.append(" AND CreatedBy = ?");
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {
                int paramIndex = 1;

                // Đặt các tham số tìm kiếm
                if (search != null && !search.trim().isEmpty()) {
                    String searchPattern = "%" + search + "%";
                    ps.setString(paramIndex++, searchPattern);
                    ps.setString(paramIndex++, searchPattern);
                    ps.setString(paramIndex++, searchPattern);
                }

                // Đặt các tham số filter
                if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                    ps.setString(paramIndex++, statusFilter);
                }

                if (dateFrom != null && !dateFrom.trim().isEmpty()) {
                    ps.setDate(paramIndex++, java.sql.Date.valueOf(dateFrom));
                }

                if (dateTo != null && !dateTo.trim().isEmpty()) {
                    ps.setDate(paramIndex++, java.sql.Date.valueOf(dateTo));
                }

                if (createdBy != null && !createdBy.trim().isEmpty()) {
                    ps.setString(paramIndex++, createdBy);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting transfer orders with filter: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    public boolean updateTransferOrderLocation(String toID, String finalBinID) {
        String sqlTODetail = "UPDATE todetail SET FinalBinId = ? WHERE TOID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmtTODetail = conn.prepareStatement(sqlTODetail)) {

            // Disable auto-commit for transaction
            conn.setAutoCommit(false);

            // Update TODetail table
            pstmtTODetail.setString(1, finalBinID);
            pstmtTODetail.setString(2, toID);
            int toDetailRowsAffected = pstmtTODetail.executeUpdate();

            // Commit transaction
            conn.commit();

            // Return true if at least one row was updated
            return toDetailRowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getCurrentBinQuantity(Connection conn, String binID, String productDetailID) {
        String sqlGetQuantity = "SELECT Quantity FROM BinDetail WHERE BinId = ? AND ProductDetailId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlGetQuantity)) {
            pstmt.setString(1, binID);
            pstmt.setString(2, productDetailID);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt("Quantity") : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteBinDetail(Connection conn, String binID, String productDetailID) {
        String sql = "DELETE FROM bindetail WHERE BinID = ? AND ProductDetailID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, binID);
            ps.setString(2, productDetailID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting bin detail: " + e.getMessage());
            return false;
        }
    }

    public List<String> getAllUniqueStatuses() {
        List<String> statuses = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT DISTINCT status FROM transferorder WHERE status IS NOT NULL ORDER BY status");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                if (status != null) {
                    statuses.add(status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statuses;
    }
}
