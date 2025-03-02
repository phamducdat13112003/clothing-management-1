package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.repository.ProductDAO;
import org.example.clothingmanagement.service.ProductDetailService;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;

@WebServlet(name="DeleteProductDetail", urlPatterns = "/delete-product-detail")
public class DeleteProductDetailController extends HttpServlet {
    private final ProductDetailService pds = new ProductDetailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String id = req.getParameter("id");
        String productId = req.getParameter("productid");
        boolean result = pds.deleteProductDetail(id);


        if (result) {
            session.setAttribute("alertMessage", "Delete Product Success");
            session.setAttribute("alertType", "success");
            resp.sendRedirect("list-product-detail?id="+productId);
        }
        else {
            req.setAttribute("alertMessage", "Update Product Failed");
            req.setAttribute("alertType", "error");
            req.getRequestDispatcher("list-product-detail.jsp").forward(req, resp);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


}
