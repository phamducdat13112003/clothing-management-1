package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.service.EmployeeService;
import org.example.clothingmanagement.service.SupplierService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DeleteSupplierServlet", value = "/deletesupplier")
public class DeleteSupplierServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddAcc</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddAcc at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String supplierId= request.getParameter("supplierId");
        SupplierService supplierService = new SupplierService();
        int page = 1;
        int pageSize = 5;
        int totalSuppliers = 0;
        List<Supplier> list =null;
        if(supplierId != null){
            try {
                boolean isDeleted= supplierService.deleteSupplier(supplierId);
                if((isDeleted)){
                    list= supplierService.getSuppliersWithPagination(page, pageSize);
                    totalSuppliers= supplierService.getTotalSupplierCount();
                    request.setAttribute("messageSuccess", "Supplier deleted");
                }else{
                    list= supplierService.getSuppliersWithPagination(page, pageSize);
                    totalSuppliers= supplierService.getTotalSupplierCount();
                    request.setAttribute("message", "Failed to delete supplier");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        int totalPages = (int) Math.ceil((double) totalSuppliers / pageSize);
        request.setAttribute("list", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./manageSupplier.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
