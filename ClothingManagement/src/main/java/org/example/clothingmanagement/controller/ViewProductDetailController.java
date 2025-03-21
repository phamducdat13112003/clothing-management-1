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
import java.nio.file.Files;
import java.nio.file.Path;
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

        // get image
        String urlImage = "";
        Part part = req.getPart("img");
        String contentType = part.getContentType();
        long fileSize = part.getSize(); // Kích thước tệp ảnh (bytes)
        if (!isImageFile(contentType)) {
            req.setAttribute("message", "Only image files (JPG, PNG, GIF) are allowed.");
        } else if (fileSize > 2 * 1024 * 1024) { // Kiem tra nếu lớn hơn 2MB
            req.setAttribute("message", "Image size must not exceed 2MB.");
        } else {
            String realPath = req.getServletContext().getRealPath("img/ProductDetail"); //where the photo is saved
            String source = Path.of(part.getSubmittedFileName()).getFileName().toString(); //get the original filename of the file then
            // convert it to a string, get just the filename without including the full path.
            if (!source.isEmpty()) {
                String filename = null;

                filename = id + ".png";

                if (!Files.exists(Path.of(realPath))) { // check folder /images/Equipment is existed
                    Files.createDirectories(Path.of(realPath));
                }
                part.write(realPath + "/" + filename); //Save the uploaded file to the destination folder with a new filename.
                urlImage = ("img/ProductDetail/" + filename); //Set the path to the image file
            }
        }

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
    private boolean isImageFile(String contentType) {
        String[] validImageTypes = {"image/jpeg", "image/png", "image/gif"};
        for (String validType : validImageTypes) {
            if (validType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}
