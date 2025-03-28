package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.repository.SectionDAO;
import org.example.clothingmanagement.service.BinDetailService;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.ProductDetailService;
import org.example.clothingmanagement.service.SectionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="ListBin",urlPatterns = "/list-bin")
public class ListBinController extends HttpServlet {
    BinDetailService bds = new BinDetailService();
    BinService bs = new BinService();
    SectionService ss = new SectionService();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Xử lý tham số
        int page = parsePageParameter(req);
        String sectionId = req.getParameter("id");
        String searchTerm = req.getParameter("search") != null ? req.getParameter("search").trim() : "";

        // Lấy danh sách bins
        List<Bin> bins;
        int totalBins;

        if (searchTerm.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm
            bins = bs.getBinsWithPagination(sectionId, page, PAGE_SIZE);
            totalBins = bs.getBinsBySectionIdWithoutPagination(sectionId).size();
        } else {
            // Nếu có từ khóa tìm kiếm
            bins = bs.searchBinWithPagination(sectionId, searchTerm, page, PAGE_SIZE);
            totalBins = bs.searchBinWithoutPagination(sectionId, searchTerm).size();
        }


        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalBins / PAGE_SIZE);

        // Lấy thông tin section
        Section section = ss.getSectionById(sectionId).orElse(null);

        // Đặt các thuộc tính để truyền sang view
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("section", section);
        req.setAttribute("bins", bins);
        req.setAttribute("searchTerm", searchTerm);

        req.getRequestDispatcher("/section-detail.jsp").forward(req, resp);
    }

    // Phương thức hỗ trợ parse tham số trang
    private int parsePageParameter(HttpServletRequest req) {
        try {
            return req.getParameter("page") != null ?
                    Integer.parseInt(req.getParameter("page")) : 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set character encoding to handle UTF-8 for POST requests
        req.setCharacterEncoding("UTF-8");

        // Retrieve search parameters from POST request
        String sectionId = req.getParameter("id");
        String searchTerm = req.getParameter("search");
        searchTerm = searchTerm != null ? searchTerm.trim() : "";

        // Default to first page for POST requests
        int page = 1;

        // Lấy danh sách bins
        List<Bin> bins;
        int totalBins;

        if (searchTerm.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm
            bins = bs.getBinsWithPagination(sectionId, page, PAGE_SIZE);
            totalBins = bs.getBinsBySectionIdWithoutPagination(sectionId).size();
        } else {
            // Nếu có từ khóa tìm kiếm
            bins = bs.searchBinWithPagination(sectionId, searchTerm, page, PAGE_SIZE);
            totalBins = bs.searchBinWithoutPagination(sectionId, searchTerm).size();
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalBins / PAGE_SIZE);

        // Lấy thông tin section
        Section section = ss.getSectionById(sectionId).orElse(null);

        // Đặt các thuộc tính để truyền sang view
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("section", section);
        req.setAttribute("bins", bins);
        req.setAttribute("search", searchTerm);

        // Forward to the JSP
        req.getRequestDispatcher("/section-detail.jsp").forward(req, resp);
    }

}
