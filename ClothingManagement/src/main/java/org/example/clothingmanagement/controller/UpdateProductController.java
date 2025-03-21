package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;

@WebServlet(name="UpdateProduct", urlPatterns = "/update-product")
public class UpdateProductController extends HttpServlet {
    private final ProductService ps = new ProductService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productID = req.getParameter("productId");
        String productName = req.getParameter("name");
        Double productPrice = Double.parseDouble(req.getParameter("price"));
        String material = req.getParameter("material");
        String gender = req.getParameter("gender");
        String season = req.getParameter("season");
        int categoryID = Integer.parseInt(req.getParameter("categoryID"));
        int minQuantity = Integer.parseInt(req.getParameter("minQuantity"));
        String madeIn = req.getParameter("madeIn");
        String description = req.getParameter("description");

        Product p = new Product(categoryID,description,gender,madeIn,material,minQuantity,productName,productPrice,season,productID);
        HttpSession session = req.getSession();

        boolean check = ps.updateProduct(p);
        if(check){
            session.setAttribute("alertMessage", "Update Product Success");
            session.setAttribute("alertType", "success");
            resp.sendRedirect("list-product-detail?id="+productID);
        }
        else{
            req.setAttribute("alertMessage", "Update Product Failed");
            req.setAttribute("alertType", "error");
            req.getRequestDispatcher("list-product-detail.jsp").forward(req, resp);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productID = req.getParameter("productId");
        String productName = req.getParameter("name");
        Double productPrice = Double.parseDouble(req.getParameter("price"));
        String material = req.getParameter("material");
        String gender = req.getParameter("gender");
        String season = req.getParameter("season");
        int categoryID = Integer.parseInt(req.getParameter("categoryID"));
        int minQuantity = Integer.parseInt(req.getParameter("minQuantity"));
        String madeIn = req.getParameter("madeIn");
        String description = req.getParameter("description");

        Product p = new Product(categoryID,description,gender,madeIn,material,minQuantity,productName,productPrice,season,productID);
        HttpSession session = req.getSession();

        boolean check = ps.updateProduct(p);
        if(check){
            session.setAttribute("alertMessage", "Update Product Success");
            session.setAttribute("alertType", "success");
            resp.sendRedirect("list-product-detail?id="+productID);
        }
        else{
            req.setAttribute("alertMessage", "Update Product Failed");
            req.setAttribute("alertType", "error");
            req.getRequestDispatcher("list-product-detail.jsp").forward(req, resp);
        }
    }
}
