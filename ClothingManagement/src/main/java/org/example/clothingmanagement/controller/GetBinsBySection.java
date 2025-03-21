package org.example.clothingmanagement.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.repository.BinDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/getBinsBySection")
public class GetBinsBySection extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Get the section ID from the request parameter
            String sectionID = request.getParameter("sectionID");

            // Debug output
            System.out.println("Received sectionID: " + sectionID);

            if (sectionID == null || sectionID.isEmpty()) {
                sendErrorResponse(out, "Section ID is required");
                return;
            }

            // Create a BinDAO to fetch bins
            BinDAO binDAO = new BinDAO();

            // Get all bins for the specified section
            List<Bin> bins = binDAO.getBinsBySection(sectionID);

            System.out.println("Found " + bins.size() + " bins for section " + sectionID);

            // Create a map to hold the response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("bins", bins);

            // Convert the map to JSON and send it as the response
            Gson gson = new Gson();
            out.print(gson.toJson(responseData));

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(out, "Error retrieving bins: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", errorMessage);

        Gson gson = new Gson();
        out.print(gson.toJson(errorResponse));
    }
}
