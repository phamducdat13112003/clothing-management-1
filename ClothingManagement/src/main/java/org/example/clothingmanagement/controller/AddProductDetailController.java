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
import java.sql.SQLException;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 2 * 1024 * 1024,   // 2MB
        maxRequestSize = 4 * 1024 * 1024 // 4MB
)

@WebServlet(name = "AddProductDetail", urlPatterns = "/add-product-detail")
public class AddProductDetailController extends HttpServlet {
    private final ProductDetailService pds = new ProductDetailService();
    private final ProductService ps = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Product product = new Product();
        if (ps.getProductById(id).isPresent()) {
            product = ps.getProductById(id).get();
        } else {
            product = new Product();
        }
        req.setAttribute("product", product);
        req.getRequestDispatcher("/add-product-detail.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        Integer quantity = 0;
        String productDetailId = "";
        Double weight = Double.parseDouble(req.getParameter("weight"));
        String color = req.getParameter("color");
        if (color.matches("^[a-zA-Z]+$")) {
            color = color.toLowerCase();
            color = color.substring(0, 1).toUpperCase() + color.substring(1);
        } else {
            color = "XXX";
        }

        String size = req.getParameter("size");


        // auto generate productDetailId
        ProductDetail pd = new ProductDetail();
        if (pds.getLastProductDetail(productId).isPresent()) {
            pd = pds.getLastProductDetail(productId).get();
            String[] list = pd.getId().split("-");
            String code = list[0];
            String number = list[1];
            int num = Integer.parseInt(number);
            num += 1; // Tăng số lên 1
            String newStr = String.format("%03d", num); // Đảm bảo số có 3 chữ số
            productDetailId = code + "-" + newStr; // Nối chuỗi code và newStr
        } else {
            productDetailId = productId + "-" + "001";
        }
        int status = 0;

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

                filename = productDetailId + ".png";

                if (!Files.exists(Path.of(realPath))) { // check folder /images/Equipment is existed
                    Files.createDirectories(Path.of(realPath));
                }
                part.write(realPath + "/" + filename); //Save the uploaded file to the destination folder with a new filename.
                urlImage = ("img/ProductDetail/" + filename); //Set the path to the image file
            }
        }

        boolean checkExists = true;
        List<ProductDetail> listPd = pds.getColorNSize(productId);
        for (ProductDetail productDetailPd : listPd) {
            if (productDetailPd.getSize().equalsIgnoreCase(size) && productDetailPd.getColor().equalsIgnoreCase(color)) {
                checkExists = false;
            }
        }
        if (checkExists) {
            ProductDetail productDetail = new ProductDetail(weight, status, size, quantity, productId, urlImage, productDetailId, color);
            boolean check = pds.insertProductDetail(productDetail);
            if (check) {
                HttpSession session = req.getSession();
                session.setAttribute("alertMessage", "Successfully.");
                session.setAttribute("alertType", "success");
                resp.sendRedirect(req.getContextPath() + "/list-product-detail?id=" + productId + "");
            } else {
                req.setAttribute("alertMessage", "Failed.");
                req.setAttribute("alertType", "error");
                req.getRequestDispatcher("/add-product-detail.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("alertMessage", "This product is already existed in the database.");
            req.setAttribute("alertType", "alreadyExisted");
            req.getRequestDispatcher("/add-product-detail.jsp").forward(req, resp);
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
