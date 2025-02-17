package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.service.EmployeeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "SearchEmployeeServlet", value = "/searchemployee")
public class SearchEmployeeServlet extends HttpServlet {

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
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmployeeService employeeService = new EmployeeService();
        String nameSearch = request.getParameter("search") != null ? request.getParameter("search").trim() : "";
        String pageParam = request.getParameter("page");
        List<Employee> list= null;

        int totalEmployees = 0;
        int page = 1;
        int pageSize = 5;
        if(nameSearch.isEmpty()){
            try {
                totalEmployees = employeeService.getTotalEmployeeCount();
                list = employeeService.getEmployeesWithPagination(page, pageSize);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            try {
                totalEmployees = employeeService.getTotalEmployeeCount(nameSearch);
                list = employeeService.searchEmployee(nameSearch, page, pageSize);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);
        request.setAttribute("list", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", nameSearch);
        request.getRequestDispatcher("./manageEmployee.jsp").forward(request, response);
    }
}
