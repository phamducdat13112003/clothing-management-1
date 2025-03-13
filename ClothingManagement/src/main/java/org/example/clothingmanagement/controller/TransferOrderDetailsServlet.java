package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.TODetail;
import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.TransferOrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "TransferOrderDetailsServlet", value = "/TODetail")
public class TransferOrderDetailsServlet extends HttpServlet {

    private TransferOrderDAO transferOrderDAO;

    @Override
    public void init() {
        transferOrderDAO = new TransferOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toID = request.getParameter("toID");

        // Fetch the Transfer Order based on the provided toID
        TransferOrder transferOrder = transferOrderDAO.getTransferOrderById(toID);

        if (transferOrder == null) {
            // Handle case where TransferOrder is not found
            request.setAttribute("errorMessage", "Transfer Order not found.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Fetch all TODetails for the given Transfer Order ID
        List<TODetail> toDetails = transferOrderDAO.getTODetailsByTransferOrderId(toID);

        // Pass the Transfer Order and its details to the JSP
        request.setAttribute("transferOrder", transferOrder);
        request.setAttribute("toDetails", toDetails);

        // Forward the request to the JSP page to display the details
        request.getRequestDispatcher("to-detail.jsp").forward(request, response);
    }
}
