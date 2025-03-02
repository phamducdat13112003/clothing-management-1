package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.repository.ProductDAO;
import org.example.clothingmanagement.service.ProductService;

import java.io.IOException;

@WebServlet(name="RecoverProduct", urlPatterns = "/recover-product")
public class RecoverProductController extends HttpServlet {
    private final ProductService ps =  new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String id = req.getParameter("id");
        boolean result = ps.recoverProduct(id);
        if (result) {
            session.setAttribute("alertMessage", "Recover Product Success");
            session.setAttribute("alertType", "success");
            resp.sendRedirect("product-list");
        }else {
            req.setAttribute("alertMessage", "Recover Product Failed");
            req.setAttribute("alertType", "error");
            req.getRequestDispatcher("product-list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
