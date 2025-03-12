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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="ListBin",urlPatterns = "/list-bin")
public class ListBinController extends HttpServlet {
    BinDetailService bds = new BinDetailService();
    BinService bs = new BinService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BinDetail> binDetailList = bds.getAllBinDetailAndProductDetail();
        List<Bin> binList = bs.getAllBin();
        HashMap<Bin,BinDetail> map = new HashMap<>();
        for(Bin b : binList){
            for(BinDetail bd : binDetailList){
                if(b.getBinID().equals(bd.getBinId())){
                    map.put(b,bd);
                }
            }
        }

        for (Map.Entry<Bin, BinDetail> entry : map.entrySet()){
            entry.getKey().setCurrentCapacity(entry.getValue().getWeight()*entry.getValue().getQuantity());
            entry.getKey().setAvailableCapacity(entry.getKey().getMaxCapacity()-entry.getKey().getCurrentCapacity());
        }

        req.setAttribute("map",map);
        req.getRequestDispatcher("/list-bin.jsp").forward(req, resp);

    }


}
