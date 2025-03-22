package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.BinDetailService;
import org.example.clothingmanagement.service.BinService;
import org.example.clothingmanagement.service.ProductDetailService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet(name="ViewBinDetail",urlPatterns = "/view-bin-detail")
public class ViewBinDetailController extends HttpServlet {
    private final BinDetailService bds = new BinDetailService();
    private final BinService bs = new BinService();
    private final ProductDetailService pds = new ProductDetailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String binId = req.getParameter("id");

        int page=1;
        int pageSize = 5;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        List<BinDetail> list = bds.getBinDetailsWithPagination(binId,page,pageSize);
        List<ProductDetail> productDetails = pds.getAllProductDetails();
        for(BinDetail bd : list) {
            for(ProductDetail pd : productDetails) {
                if(bd.getProductDetailId().equalsIgnoreCase(pd.getId())){
                    bd.setProductDetail(pd);
                }
            }
        }

        int totalProduct = bds.getBinDetailsByBinId(binId).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        Bin bin = null;
        if(bs.getBinByBinId(binId).isPresent()){
            bin = bs.getBinByBinId(binId).get();
        }
        req.setAttribute("list", list);
        req.setAttribute("bin", bin);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("view-bin-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO sửa lại doget lấy dữ liệu của product detail truyền vào các thuộc tính phụ của bindetail chứ không gửi hashmap nữa ( lí do là vì ko muốn hiển thị quá nhiều id, thay bằng name)
        String binId = req.getParameter("id");
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

        List<BinDetail> list = bds.searchBinDetailWithPagination(binId,nameSearch,page,pageSize);
        List<ProductDetail> productDetails = pds.getAllProductDetails();
        for(BinDetail bd : list) {
            for(ProductDetail pd : productDetails) {
                if(bd.getProductDetailId().equalsIgnoreCase(pd.getId())){
                    bd.setProductDetail(pd);
                }
            }
        }

        int totalProduct = bds.searchBinDetailWithoutPagination(binId,nameSearch).size();
        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
        Bin bin = null;
        if(bs.getBinByBinId(binId).isPresent()){
            bin = bs.getBinByBinId(binId).get();
        }

        req.setAttribute("list", list);
        req.setAttribute("bin", bin);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("view-bin-detail.jsp").forward(req, resp);

    }
}
