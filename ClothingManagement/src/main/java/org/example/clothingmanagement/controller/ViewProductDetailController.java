package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.ProductDetailService;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@WebServlet(name="ViewProductDetail",urlPatterns = "/view-product-detail")
public class ViewProductDetailController extends HttpServlet {
    ProductDetailService pds = new ProductDetailService();
    ProductService ps = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ProductDetail pd = new ProductDetail();
        Product p = new Product();
        if(pds.getOptionalProductDetailByProductDetailId(id).isPresent()){
            pd = pds.getOptionalProductDetailByProductDetailId(id).get();
            if(ps.getProductById(pd.getProductId()).isPresent()){
                p = ps.getProductById(pd.getProductId()).get();
            }
            else{
                p = null;
            }
            req.setAttribute("p", p);
            req.setAttribute("pd", pd);
            req.getRequestDispatcher("view-product-detail.jsp").forward(req, resp);
        }
        else{
            pd=null;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
//        Part filePart = req.getPart("img"); // Lấy phần tệp tải lên từ input có name="img"
//        String image = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Lấy tên tệp
        Double weight = Double.parseDouble(req.getParameter("weight"));
        ProductDetail pd = new ProductDetail(id,"image",weight);
        boolean check = pds.updateProductDetail(pd);
        if (check) {
            HttpSession session = req.getSession();
            session.setAttribute("alertMessage", "Update Successfully.");
            session.setAttribute("alertType", "success");
            resp.sendRedirect(req.getContextPath() + "/product-detail-list");
        } else {
            req.setAttribute("alertMessage", "Failed to update.");
            req.setAttribute("alertType", "error");
            req.getRequestDispatcher("/view-product-detail.jsp").forward(req, resp);
        }

    }
}
