package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Warehouse;
import org.example.clothingmanagement.repository.WarehouseDAO;

import java.io.IOException;

@WebServlet("/warehouse")
public class ManageWarehouseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listWarehouses(request, response);
                break;
            case "view":
                viewWarehouse(request, response);
                break;
            case "delete":
                deleteWarehouse(request, response);
                break;
            default:
                response.sendRedirect("404.jsp");
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "create"; // Nếu không có action thì mặc định là "create"
        }

        switch (action) {
            case "create":
                createWarehouse(request, response);
                break;
            case "update":
                updateWarehouse(request, response);
                break;
            default:
                response.sendRedirect("404.jsp");
                break;
        }
    }

    // Liệt kê tất cả các warehouse
    private void listWarehouses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        request.setAttribute("warehouses", warehouseDAO.getAllWareHouse());
        request.getRequestDispatcher("/manageWarehouse.jsp").forward(request, response);
    }

    private void viewWarehouse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int warehouseID = Integer.parseInt(request.getParameter("warehouseId"));
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        Warehouse warehouse = warehouseDAO.getWarehouseById(warehouseID);

        if (warehouse != null) {
            request.setAttribute("warehouse", warehouse);
            request.getRequestDispatcher("/updateWarehouse.jsp").forward(request, response);
        } else {
            response.sendRedirect("404.jsp");
        }
    }

    // Thêm mới warehouse
    private void createWarehouse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String warehouseName = request.getParameter("warehouseName");
        String address = request.getParameter("address");
        int branchId = Integer.parseInt(request.getParameter("branchId"));

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(warehouseName);
        warehouse.setAddress(address);
        warehouse.setBranchId(branchId);

        WarehouseDAO warehouseDAO = new WarehouseDAO();
        warehouseDAO.createWarehouse(warehouse);

        response.sendRedirect("warehouse?action=list"); // Redirect đến danh sách warehouse
    }

    // Cập nhật thông tin warehouse
    private void updateWarehouse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
        String warehouseName = request.getParameter("warehouseName");
        String address = request.getParameter("address");
        int branchId = Integer.parseInt(request.getParameter("branchId"));

        Warehouse warehouse = new Warehouse();
        //warehouse.setWarehouseId(warehouseId);
        warehouse.setWarehouseName(warehouseName);
        warehouse.setAddress(address);
        warehouse.setBranchId(branchId);

        WarehouseDAO warehouseDAO = new WarehouseDAO();
        warehouseDAO.updateWarehouse(warehouse);

        response.sendRedirect("warehouse?action=list"); // Redirect đến danh sách warehouse
    }

    // Xóa một warehouse
    private void deleteWarehouse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int warehouseID = Integer.parseInt(request.getParameter("warehouseId"));
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        try {
            warehouseDAO.deleteWarehouse(warehouseID); // Try deleting the warehouse
            response.sendRedirect("warehouse?action=list"); // Redirect to warehouse list if successful
        } catch (RuntimeException e) {
            // Handle error, e.g., foreign key constraint violation
            response.setContentType("text/html");
            response.getWriter().write("<h3>Error: " + e.getMessage() + "</h3>");
            response.getWriter().write("<a href='warehouse?action=list'>Go back to the list</a>");
        }
    }
}

