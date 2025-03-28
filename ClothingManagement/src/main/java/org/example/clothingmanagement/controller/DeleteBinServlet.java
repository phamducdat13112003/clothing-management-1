package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.service.BinDetailService;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.SectionService;
import org.example.clothingmanagement.service.TOService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DeleteBinServlet", value = "/deletebin")
public class DeleteBinServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddAcc</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddAcc at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String binId = request.getParameter("binId").trim();
        String sectionId = request.getParameter("sectionId");

        BinService binService = new BinService();

        // Check if the bin has any products
        if (binService.hasBinProducts(binId)) {
            // If bin has products, set error message
            request.getSession().setAttribute("errorMessage", "Cannot delete bin. Bin contains products.");
        } else {
            // If bin is empty, proceed with deletion
            boolean isDeletedBin = binService.deleteBin(binId);
            if (isDeletedBin) {
                request.getSession().setAttribute("successMessage", "Bin deleted successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to delete bin");
            }
        }

        // Redirect back to the list-bin page
        response.sendRedirect("list-bin?id=" + sectionId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
