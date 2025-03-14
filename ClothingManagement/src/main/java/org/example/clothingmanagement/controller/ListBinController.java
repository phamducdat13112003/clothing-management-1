package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.repository.SectionDAO;
import org.example.clothingmanagement.service.BinDetailService;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.ProductDetailService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="ListBin",urlPatterns = "/list-bin")
public class ListBinController extends HttpServlet {
    BinDetailService bds = new BinDetailService();
    BinService bs = new BinService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sectionId = req.getParameter("id");

        List<Bin> bins = bs.getBinsBySectionId(sectionId);

        for(Bin b : bins){
            List<BinDetail> list = bds.getAllBinDetailAndProductDetailByBinId(b.getBinID());
            for(BinDetail bd : list){
                b.setCurrentCapacity(b.getCurrentCapacity()+(bd.getWeight()*bd.getQuantity()));
                b.setAvailableCapacity(b.getMaxCapacity()-b.getCurrentCapacity());
            }
        }


        req.setAttribute("bins",bins);
        req.getRequestDispatcher("/list-bin.jsp").forward(req, resp);

    }


}
