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
        int page = 1;
        int pageSize = 5;
        BinService binService = new BinService();
        TOService toService = new TOService();
        BinDetailService binDetailService = new BinDetailService();
        SectionService sectionService = new SectionService();
        String sectionId = sectionService.getSectionByBin(binId);
        int totalBins = binService.getTotalBinsBySection(sectionId);
        Section section = null;
        if(sectionService.getSectionById(sectionId).isPresent()){
            section = sectionService.getSectionById(sectionId).get();
        }
        if (binId.isEmpty()) {
            request.setAttribute("message", "Can't get binID");
        } else if (!binService.canDeleteBin(binId)) {
            request.setAttribute("message", "Cannot delete bin because product still exists in bin.");
        } else if (toService.hasProcessingTO(binId)) {
            request.setAttribute("message", "Cannot delete bin because bin is in processing time status.");
        } else {
            boolean isDeletedBin = binService.deleteBin(binId);
            if (isDeletedBin) {
                request.setAttribute("messageSuccess", "Delete Bin Successfully");
            } else {
                request.setAttribute("message", "Delete Failed");
            }
        }
        List<Bin> list = binService.getBinsWithPagination(sectionId, page, pageSize);
        for (Bin b : list) {
            List<BinDetail> listDetail = binDetailService.getAllBinDetailAndProductDetailByBinId(b.getBinID());
            for (BinDetail bd : listDetail) {
                b.setCurrentCapacity(b.getCurrentCapacity() + (bd.getWeight() * bd.getQuantity()));
                b.setAvailableCapacity(b.getMaxCapacity() - b.getCurrentCapacity());
            }
        }
        int totalPages = (int) Math.ceil((double) totalBins / pageSize);
        request.setAttribute("bins", list);
        request.setAttribute("section", section);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./list-bin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
