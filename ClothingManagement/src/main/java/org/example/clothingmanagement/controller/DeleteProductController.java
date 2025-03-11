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

@WebServlet(name="DeleteProduct", urlPatterns = "/delete-product")
public class DeleteProductController extends HttpServlet {
    private final ProductService ps =  new ProductService();
    private final ProductDetailService pds = new ProductDetailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String id = req.getParameter("id");
        boolean checkStatus = ps.deleteProduct(id);
        boolean checkStatusPD = pds.updateAllProductDetail(id);
        if (checkStatus && checkStatusPD) {
            session.setAttribute("alertMessage", "Delete Product Success");
            session.setAttribute("alertType", "success");
            resp.sendRedirect("product-list");
        }else {
            req.setAttribute("alertMessage", "Update Product Failed");
            req.setAttribute("alertType", "error");
            req.getRequestDispatcher("product-list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
