package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.repository.BinDAO;
import org.example.clothingmanagement.repository.SectionDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewBin")
public class ViewBinDetail extends HttpServlet {
    private BinDAO binDAO;
    private SectionDAO sectionDAO;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    public void init() throws ServletException {
        System.out.println("DEBUG: Initializing ViewBinDetail servlet");
        binDAO = new BinDAO();
        sectionDAO = new SectionDAO();
        System.out.println("DEBUG: DAOs initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("DEBUG: ViewBinDetail doGet method called");

        String binID = request.getParameter("binID");
        System.out.println("DEBUG: Received binID: " + binID);

        if (binID == null || binID.isEmpty()) {
            System.out.println("DEBUG: binID is null or empty, redirecting");
            response.sendRedirect("section?action=list");
            return;
        }

        // Get filtering parameters
        String search = request.getParameter("search") != null ? request.getParameter("search") : "";
        String material = request.getParameter("material") != null ? request.getParameter("material") : "";
        String season = request.getParameter("season") != null ? request.getParameter("season") : "";
        String madeIn = request.getParameter("madeIn") != null ? request.getParameter("madeIn") : "";

        System.out.println("DEBUG: Filters - search: " + search + ", material: " + material +
                ", season: " + season + ", madeIn: " + madeIn);

        // Pagination parameters
        int page = 1;
        int pageSize = DEFAULT_PAGE_SIZE;

        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) page = 1;
            }

            if (request.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
                if (pageSize < 1) pageSize = DEFAULT_PAGE_SIZE;
            }
            System.out.println("DEBUG: Using page=" + page + ", pageSize=" + pageSize);
        } catch (NumberFormatException e) {
            System.out.println("DEBUG: NumberFormatException with pagination params, using defaults");
        }

        try {
            // Get bin details
            System.out.println("DEBUG: Fetching bin details for binID: " + binID);
            Bin bin = binDAO.getBinDetailByBinId(binID);

            if (bin == null) {
                System.out.println("DEBUG: No bin found with ID: " + binID);
                response.sendRedirect("section?action=list");
                return;
            }
            System.out.println("DEBUG: Found bin: " + bin.getBinID() + " - " + bin.getBinName());

            // Get section details for this bin
            System.out.println("DEBUG: Fetching section details for sectionID: " + bin.getSectionID());
            Section section = sectionDAO.getSectionByID(bin.getSectionID());
            System.out.println("DEBUG: Section found: " + (section != null));

            // Get filter dropdown data
            List<String> allMaterials = binDAO.getAllDistinctMaterials();
            List<String> allSeasons = binDAO.getAllDistinctSeasons();
            List<String> allMadeIn = binDAO.getAllDistinctMadeIn();

            System.out.println("DEBUG: Fetched filter data - Materials: " + allMaterials.size() +
                    ", Seasons: " + allSeasons.size() + ", Made In: " + allMadeIn.size());

            // Calculate offset for pagination
            int offset = (page - 1) * pageSize;

            // Get product details with pagination and filtering
            System.out.println("DEBUG: Fetching product details with filters");
            List<ProductDetail> productDetails = binDAO.getProductDetailsWithFilters(
                    offset, pageSize, search, material, season, madeIn);
            System.out.println("DEBUG: Found " + (productDetails != null ? productDetails.size() : 0) + " product details");

            // Get total number of records for pagination with filters applied
            int totalRecords = binDAO.countProductDetailsWithFilters(search, material, season, madeIn);
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
            System.out.println("DEBUG: Total records: " + totalRecords + ", Total pages: " + totalPages);

            // Set attributes for JSP
            request.setAttribute("bin", bin);
            request.setAttribute("section", section);
            request.setAttribute("binDetails", productDetails); // Name stays binDetails for JSP compatibility
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);

            // Set filter attributes for JSP
            request.setAttribute("search", search);
            request.setAttribute("material", material);
            request.setAttribute("season", season);
            request.setAttribute("madeIn", madeIn);
            request.setAttribute("allMaterials", allMaterials);
            request.setAttribute("allSeasons", allSeasons);
            request.setAttribute("allMadeIn", allMadeIn);

            System.out.println("DEBUG: All attributes set for JSP");

            // Forward to JSP
            System.out.println("DEBUG: Forwarding to binDetail.jsp");
            request.getRequestDispatcher("binDetail.jsp").forward(request, response);
            System.out.println("DEBUG: Request forwarded successfully");
        } catch (Exception e) {
            System.out.println("DEBUG: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            getServletContext().setAttribute("errorMessage_bin_" + binID, "Error retrieving bin details: " + e.getMessage());
            System.out.println("DEBUG: Redirecting to error.jsp");
            response.sendRedirect("error.jsp");
        }
    }
}
