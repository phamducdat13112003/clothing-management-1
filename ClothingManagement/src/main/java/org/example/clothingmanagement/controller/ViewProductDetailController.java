package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.service.ProductDetailService;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 2 * 1024 * 1024,   // 2MB
        maxRequestSize = 4 * 1024 * 1024 // 4MB
)
@WebServlet(name = "ViewProductDetail", urlPatterns = "/view-product-detail")
public class ViewProductDetailController extends HttpServlet {
    ProductDetailService pds = new ProductDetailService();
    ProductService ps = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ProductDetail pd = new ProductDetail();
        Product p = new Product();
        if (pds.getOptionalProductDetailByProductDetailId(id).isPresent()) {
            pd = pds.getOptionalProductDetailByProductDetailId(id).get();
            if (ps.getProductById(pd.getProductId()).isPresent()) {
                p = ps.getProductById(pd.getProductId()).get();
            } else {
                p = null;
            }
            req.setAttribute("p", p);
            req.setAttribute("pd", pd);
            req.getRequestDispatcher("view-product-detail.jsp").forward(req, resp);
        } else {
            pd = null;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Double weight = Double.parseDouble(req.getParameter("weight"));
        ProductDetail editPD = new ProductDetail(id, "", weight);
        Part part = req.getPart("img");
        if (part != null && part.getSize() > 0) { // Check if part is not null and has content
            String contentType = part.getContentType();
            if (!isImageFile(contentType)) {
                req.setAttribute("message", "Only image files (JPG, PNG, GIF) are allowed.");
                req.getRequestDispatcher("view-product-detail").include(req, resp);
                return;
            }
            String realPath = req.getServletContext().getRealPath("img/ProductDetail"); //where the photo is saved
            String source = Path.of(part.getSubmittedFileName()).getFileName().toString(); //get the original filename of the file then
            // convert it to a string, get just the filename without including the full path

            if (!source.isEmpty()) {
                String filename = id + ".png";
                if (!Files.exists(Path.of(realPath))) { // check folder /img/ProductDetail  is existed
                    Files.createDirectories(Path.of(realPath));
                }
                part.write(realPath + "/" + filename); //Save the uploaded file to the destination folder with a new filename.
                editPD.setImage("img/ProductDetail/" + filename + "?" + System.currentTimeMillis()); //Set the path to the image file
            }
        } else {
            ProductDetail existPD = null;
            try {
                existPD = pds.getProductDetailByProductDetailID(id);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            editPD.setImage(existPD.getImage());
        }
        boolean success = pds.updateProductDetail(editPD);

        if (success) {
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

