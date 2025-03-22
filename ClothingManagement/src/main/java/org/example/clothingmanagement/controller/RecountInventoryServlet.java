package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.repository.InventoryDocDAO;
import org.example.clothingmanagement.repository.InventoryDocDetailDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "RecountInventoryServlet", value = "/RecountInventoryServlet")
public class RecountInventoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Lấy InventoryDocID từ request
            String inventoryDocId = request.getParameter("inventoryDocId");
            String binId = request.getParameter("binId");
            String employee = request.getParameter("employee");

            // Lấy danh sách ProductDetailID và RecountQuantity
            String[] productDetailIds = request.getParameterValues("productDetailId[]");

            // Chuyển đổi sang danh sách
            List<String> productDetailIdList = new ArrayList<>();
            List<Integer> recountQuantityList = new ArrayList<>();

            for (int i = 0; i < productDetailIds.length; i++) {
                productDetailIdList.add(productDetailIds[i]);
                recountQuantityList.add(-1);
            }

            // Gọi DAO để cập nhật dữ liệu
            InventoryDocDetailDAO.secondCount(productDetailIdList, recountQuantityList,inventoryDocId,employee);
            InventoryDocDAO.updateInventoryDocStatus(inventoryDocId,"Recount");
            // Chuyển hướng sau khi cập nhật thành công
            response.sendRedirect("ViewInventoryDocList");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewInventoryDocList");
        }

    }


}