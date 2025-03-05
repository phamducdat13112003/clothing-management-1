package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.service.EmployeeService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EmployeeDetailServlet", value = "/employeedetail")
public class EmployeeDetailServlet extends HttpServlet {

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
        String employeeID = request.getParameter("employeeId");
        EmployeeService employeeService = new EmployeeService();
        if (employeeID != null) {
            try {
                Employee employee= employeeService.getEmployeeByID(employeeID);
                request.setAttribute("employee", employee);
                request.getRequestDispatcher("./employeeInformationDetail.jsp").forward(request, response);
            }catch(Exception e) {
                throw new RuntimeException("No find employee with ID: " + employeeID, e);
            }
        }else{
            response.sendRedirect("./manageEmployee.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
