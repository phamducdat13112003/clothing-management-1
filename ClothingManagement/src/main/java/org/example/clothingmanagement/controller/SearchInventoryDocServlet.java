package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothingmanagement.entity.InventoryDoc;
import org.example.clothingmanagement.service.InventoryDocService;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@WebServlet(name = "SearchInventoryDocServlet", value = "/SearchInventoryDocServlet")
public class SearchInventoryDocServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, IOException, ServletException {
        // Lấy dữ liệu từ request
        String query = request.getParameter("query").trim();

        // Kiểm tra khoảng trống và độ dài
        if (query == null || query.trim().isEmpty() || query.length() > 20) {
            response.getWriter().println("<script>alert('Please Enter Valid Input'); window.history.back();</script>");
            return;
        }

        // Gọi DAO để tìm kiếm inventorydoc
        InventoryDocService dao = new InventoryDocService();
        List<InventoryDoc> listDoc = dao.searchInventoryDoc(query,query);
        Map<String, String> getCreatedByToEmployeeNameMap = dao.getCreatedByToEmployeeNameMap();

        // Kiểm tra kết quả và chuyển hướng
        if (listDoc.isEmpty()) {
            response.getWriter().println("<script>alert('Not Found!'); window.history.back();</script>");
        } else {
            request.setAttribute("getCreatedByToEmployeeNameMap", getCreatedByToEmployeeNameMap);
            request.setAttribute("listDoc", listDoc);
            request.getRequestDispatcher("inventoryDocList.jsp").forward(request, response);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }


}