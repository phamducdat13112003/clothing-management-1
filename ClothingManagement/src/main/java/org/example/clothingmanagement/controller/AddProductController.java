package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.CategoryService;
import org.example.clothingmanagement.service.ProductService;
import org.example.clothingmanagement.service.SupplierService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AddProduct", urlPatterns = "/add-product")
public class AddProductController extends HttpServlet {
    private final SupplierService ss = new SupplierService();
    private final ProductService ps = new ProductService();
    private final CategoryService cs = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = cs.selectAll();

        List<Supplier> suppliers = ss.getAllSuppliers();
        req.setAttribute("categories", categories);
        req.setAttribute("suppliers", suppliers);
        req.getRequestDispatcher("add-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productName = capitalizeWords(req.getParameter("productName").trim());
        Double price = Double.parseDouble(req.getParameter("price"));
        Integer categoryID = Integer.parseInt(req.getParameter("categoryID"));
        String material = req.getParameter("material");
        String gender = req.getParameter("gender");
        String seasons = req.getParameter("seasons");
        int minQuantity = Integer.parseInt(req.getParameter("minQuantity"));
        String description = req.getParameter("description");
        String supplierID = req.getParameter("supplierID");
        String madeIn = req.getParameter("madeIn");
        String createdBy = req.getParameter("employeeId");
        LocalDate createdDate = LocalDate.parse(req.getParameter("createdDate"));

        //auto generate new product id
        Product p = new Product();
        String finalId;
        if (ps.getTheLastProduct().isPresent()) {
            p = ps.getTheLastProduct().get();
            String id = p.getId();
            String prefix = id.substring(0, 1); // 'P'
            String numberPart = id.substring(1); // '001'
            // Chuyển phần số sang dạng số nguyên và tăng lên 1
            int number = Integer.parseInt(numberPart);
            number++;
            // Chuyển lại thành chuỗi với số đã tăng, bổ sung số không nếu cần thiết
            String nextNumberPart = String.format("%03d", number);
            // Kết hợp lại phần chữ 'P' và phần số
            finalId = prefix + nextNumberPart;
        } else {
            finalId = "P001";
        }

        Product product = new Product(finalId, productName, price, seasons, supplierID, material, madeIn, gender, description, categoryID, minQuantity, createdBy, 0, createdDate);
        boolean checkDup = ps.checkDup(product);
        if (!checkDup) {
            boolean check = ps.addProduct(product);
            if (check) {
                HttpSession session = req.getSession();
                session.setAttribute("alertMessage", "Successfully.");
                session.setAttribute("alertType", "success");
                resp.sendRedirect(req.getContextPath() + "/product-list");
            } else {
                req.setAttribute("alertMessage", "Failed.");
                req.setAttribute("alertType", "error");
                req.getRequestDispatcher("/add-product.jsp").forward(req, resp);
            }
        }
        else{
            req.setAttribute("alertMessage", "Duplicated.");
            req.setAttribute("alertType", "error");
            req.getRequestDispatcher("/add-product.jsp").forward(req, resp);
        }


    }

    public static String capitalizeWords(String str) {
        String[] words = str.split("\\s+");  // Tách chuỗi theo khoảng trắng
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            // Viết hoa chữ cái đầu và ghép lại thành chuỗi
            capitalizedString.append(word.substring(0, 1).toUpperCase())  // Viết hoa chữ cái đầu
                    .append(word.substring(1).toLowerCase())  // Giữ nguyên phần còn lại của từ
                    .append(" ");  // Thêm khoảng trắng
        }

        return capitalizedString.toString().trim();  // Loại bỏ khoảng trắng dư thừa ở cuối
    }
}
