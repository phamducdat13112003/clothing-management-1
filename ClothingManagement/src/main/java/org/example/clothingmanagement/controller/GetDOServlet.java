package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.DeliveryOrderDetail;
import org.example.clothingmanagement.repository.DeliveryOrderDAO;
import org.example.clothingmanagement.repository.DeliveryOrderDetailDAO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet(name = "GetDOServlet", value = "/GetDOServlet")
public class GetDOServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

            String[] doIDsArray = request.getParameterValues("doID");

            if (doIDsArray == null || doIDsArray.length == 0) {
                request.setAttribute("error", "No DO selected. Please select at least one.");
                request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);
                return;
            }

            List<String> doIDs = Arrays.asList(doIDsArray);
            List<Map<String, Object>> doDetails = DeliveryOrderDAO.getDODetailsWithProductInfo(doIDs);

            if (doDetails.isEmpty()) {
                request.setAttribute("error", "No DO details found for the selected IDs.");
                request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);
                return;
            }

            request.setAttribute("doDetails", doDetails);
            request.setAttribute("doIDs", doIDs);
            request.getRequestDispatcher("deliveryOrderWS.jsp").forward(request, response);

    }




}