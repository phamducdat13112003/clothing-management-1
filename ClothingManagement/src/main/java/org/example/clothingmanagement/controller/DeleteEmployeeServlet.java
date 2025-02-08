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
        if(employeeId != null){
            try {
                Employee employee = employeeService.getEmployeeByID(Integer.parseInt(employeeId));
                boolean isDeleted= employeeService.deleteEmployee(Integer.parseInt(employeeId));
                int accountId = employee.getAccountID();
                boolean isAccountDeleted = accountService.deleteAccount(accountId);
                if((isDeleted) && (isAccountDeleted)){
                    List<Employee> list= employeeService.getAllEmployees();
                    request.setAttribute("message", "Employee deleted");
                    request.setAttribute("list", list);
                }else{
                    List<Employee> list= employeeService.getAllEmployees();
                    request.setAttribute("message", "Failed to delete employee");
                    request.setAttribute("list", list);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        request.getRequestDispatcher("./manageEmployee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
