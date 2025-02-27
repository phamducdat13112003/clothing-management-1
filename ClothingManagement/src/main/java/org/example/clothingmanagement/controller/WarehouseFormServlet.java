package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.Warehouse;
import org.example.clothingmanagement.repository.WarehouseDAO;

import java.io.IOException;

@WebServlet("/warehouseForm")
public class WarehouseFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("update".equals(action)) {
            // lay id de hien thi thong tin warehouse de update
            String warehouseId = request.getParameter("warehouseId");
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            Warehouse warehouse = warehouseDAO.getWarehouseById(Integer.parseInt(warehouseId));
            request.setAttribute("warehouse", warehouse);

            // set form action -> update
            request.setAttribute("formAction", "/ClothingManagement_war/warehouseForm?action=update");
        } else {
            // set form action -> create
            request.setAttribute("formAction", "/ClothingManagement_war/warehouseForm?action=create");
        }
        request.getRequestDispatcher("/updateWarehouse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createWarehouse(request, response);
        } else if ("update".equals(action)) {
            updateWarehouse(request, response);
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
        int warehouseId = Integer.parseInt(request.getParameter("warehouseId"));
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
}
