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

@WebServlet(name = "SearchProductDetailServlet", value = "/transfer-order/searchProductDetail")
public class ProductSearchServlet extends HttpServlet {
    private TransferOrderDAO transferOrderDAO;

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        JsonObject responseJson = new JsonObject();

        try {
            // Get product details matching the search query
            List<ProductDetail> suggestions = transferOrderDAO.searchProductDetails(query);

            if (suggestions != null && !suggestions.isEmpty()) {
                responseJson.addProperty("success", true);
                JsonArray suggestionArray = new JsonArray();
                for (ProductDetail suggestion : suggestions) {
                    JsonObject suggestionObj = new JsonObject();
                    suggestionObj.addProperty("productDetailID", suggestion.getId());
                    suggestionObj.addProperty("productName", suggestion.getProductName());
                    suggestionObj.addProperty("weight", suggestion.getWeight());
                    suggestionArray.add(suggestionObj);
                }
                responseJson.add("suggestions", suggestionArray);
            } else {
                responseJson.addProperty("success", false);
                responseJson.addProperty("message", "No products found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "Error occurred while searching for products.");
        }

        // Send the response as JSON
        response.setContentType("application/json");
        response.getWriter().write(responseJson.toString());
    }


}
