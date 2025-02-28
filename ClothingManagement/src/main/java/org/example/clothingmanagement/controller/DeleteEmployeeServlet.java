package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.service.AccountService;
import org.example.clothingmanagement.service.EmployeeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DeleteEmployeeServlet", value = "/deleteemployee")
public class DeleteEmployeeServlet extends HttpServlet {

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
        String employeeId= request.getParameter("employeeId");
        EmployeeService employeeService = new EmployeeService();
        AccountService accountService = new AccountService();
        int page = 1;
        int pageSize = 5;
        int totalEmployees = 0;
        List<Employee> list =null;
        if(employeeId != null){
            try {
                boolean isDeleted= employeeService.deleteEmployee(employeeId);
                boolean isAccountDeleted = true;
                if (employeeService.hasAccount(employeeId)) {
                    isAccountDeleted = accountService.deleteAccountWhenDeleteEmployee(employeeId);
                }
                if((isDeleted) && (isAccountDeleted)){
                     list= employeeService.getEmployeesWithPagination(page, pageSize);
                    totalEmployees = employeeService.getTotalEmployeeCount();
                    request.setAttribute("messageSuccess", "Employee deleted");
                }else{
                     list= employeeService.getEmployeesWithPagination(page, pageSize);
                     totalEmployees = employeeService.getTotalEmployeeCount();
                    request.setAttribute("message", "Failed to delete employee");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);
        request.setAttribute("list", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("./manageEmployee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
