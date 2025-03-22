package org.example.clothingmanagement.controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.SneakyThrows;
import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.service.BinService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetBinsBySectionServlet", value = "/getbinsbysection")
public class GetBinsBySectionServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        BinService binService = new BinService();
        String sectionId = request.getParameter("sectionId");
        System.out.println(sectionId);
        //hàm getBinsBySectionId này dùng của Đức
        List<Bin> bins = binService.getBinAndWeightBySectionId(sectionId);
        System.out.println(bins);
        Gson gson = new Gson();
        String json = gson.toJson(bins);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}