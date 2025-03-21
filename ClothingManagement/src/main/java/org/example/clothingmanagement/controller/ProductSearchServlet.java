package org.example.clothingmanagement.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.repository.TransferOrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchProductDetailServlet", value = "/searchProductDetail")
public class ProductSearchServlet extends HttpServlet {
    private TransferOrderDAO transferOrderDAO;

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String binID = request.getParameter("binID"); // Get the bin ID parameter

        System.out.println("Received query: " + query);
        System.out.println("Received bin ID: " + binID);

        // Create JsonObject at the start of the method
        JsonObject responseJson = new JsonObject();

        try {
            // Get product details matching the search query AND located in the specified bin
            List<ProductDetail> suggestions = transferOrderDAO.searchProductDetailsByBin(query, binID);

            if (suggestions != null && !suggestions.isEmpty()) {
                // Explicitly set success to true
                responseJson.addProperty("success", true);

                JsonArray suggestionArray = new JsonArray();
                for (ProductDetail suggestion : suggestions) {
                    JsonObject suggestionObj = new JsonObject();

                    // Ensure non-null values
                    suggestionObj.addProperty("productDetailID",
                            suggestion.getId() != null ? suggestion.getId() : "");
                    suggestionObj.addProperty("productName",
                            suggestion.getProductName() != null ? suggestion.getProductName() : "");
                    suggestionObj.addProperty("weight",
                            suggestion.getWeight() != null ? suggestion.getWeight() : 0.0);

                    // Include the quantity in the response
                    if (binID != null && !binID.isEmpty()) {
                        suggestionObj.addProperty("quantity", suggestion.getQuantity());
                    }

                    suggestionArray.add(suggestionObj);
                }

                // Add suggestions array to response
                responseJson.add("suggestions", suggestionArray);

                // Debug logging
                System.out.println("Response JSON: " + responseJson.toString());
            } else {
                responseJson.addProperty("success", false);
                responseJson.addProperty("message", "No products found in this bin.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "Error occurred while searching for products: " + e.getMessage());
        }

        // Send the response as JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");  // Ensure proper character encoding
        response.getWriter().write(responseJson.toString());

        // Additional debug logging
        System.out.println("Final Response JSON: " + responseJson.toString());
    }
}
