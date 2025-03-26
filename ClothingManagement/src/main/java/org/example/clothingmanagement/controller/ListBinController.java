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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        String sectionId = req.getParameter("id");
        List<Bin> bins = bs.getBinsWithPagination(sectionId, page, pageSize);
        for(Bin b : bins){
            List<BinDetail> list = bds.getAllBinDetailAndProductDetailByBinId(b.getBinID());
            for(BinDetail bd : list){
                b.setCurrentCapacity(b.getCurrentCapacity()+(bd.getWeight()*bd.getQuantity()));
                b.setAvailableCapacity(b.getMaxCapacity()-b.getCurrentCapacity());
            }
        }
        int totalProduct = bs.getBinsBySectionIdWithoutPagination(sectionId).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        Section section = null;
        if(ss.getSectionById(sectionId).isPresent()){
            section = ss.getSectionById(sectionId).get();
        }
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("section",section);
        req.setAttribute("bins",bins);
        req.getRequestDispatcher("/list-bin.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sectionId = req.getParameter("id");
        String nameSearch = req.getParameter("search") != null ? req.getParameter("search").trim() : "";
        String pageParam = req.getParameter("page");

        int page = 1;
        int pageSize = 5;
        if(!nameSearch.isEmpty()){
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
        }

        List<Bin> bins = bs.searchBinWithPagination(sectionId,nameSearch, page, pageSize);
        for(Bin b : bins){
            List<BinDetail> list = bds.getAllBinDetailAndProductDetailByBinId(b.getBinID());
            for(BinDetail bd : list){
                b.setCurrentCapacity(b.getCurrentCapacity()+(bd.getWeight()*bd.getQuantity()));
                b.setAvailableCapacity(b.getMaxCapacity()-b.getCurrentCapacity());
            }
        }
        int totalProduct = bs.searchBinWithoutPagination(sectionId,nameSearch).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        Section section = null;
        if(ss.getSectionById(sectionId).isPresent()){
            section = ss.getSectionById(sectionId).get();
        }
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("section",section);
        req.setAttribute("bins",bins);
        req.getRequestDispatcher("/list-bin.jsp").forward(req, resp);
    }

}
