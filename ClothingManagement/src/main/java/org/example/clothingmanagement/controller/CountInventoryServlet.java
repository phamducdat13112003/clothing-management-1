package org.example.clothingmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.InventoryDocDetail;
import org.example.clothingmanagement.repository.InventoryDocDAO;
import org.example.clothingmanagement.repository.InventoryDocDetailDAO;
import org.example.clothingmanagement.service.BinDetailService;
import org.example.clothingmanagement.service.InventoryDocDetailService;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet(name = "CountInventoryServlet", value = "/CountInventoryServlet")
public class CountInventoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
          String binId = request.getParameter("binId");
          String inventoryDocId = request.getParameter("inventoryDocId");
          String employee = request.getParameter("employee");
          String status = request.getParameter("status");

          List <BinDetail> listDetail= InventoryDocDAO.getBinDetailByBinID(binId);
          List<InventoryDocDetail> listInvenDoc = InventoryDocDetailDAO.getInventoryDocDetailsByDocID(inventoryDocId);
        Map<String, String> productDetailToProductName = InventoryDocDAO.getProductDetailToProductNameMap();
        request.setAttribute("productDetailToProductName", productDetailToProductName);

        List<BinDetail> filteredList = new ArrayList<>();

        for (BinDetail binDetail : listDetail) {
            boolean isDuplicate = false;

            for (InventoryDocDetail invenDetail : listInvenDoc) {
                if (binDetail.getProductDetailId().equals(invenDetail.getProductDetailId()) &&
                        binDetail.getQuantity() == invenDetail.getRecountQuantity()) {
                    isDuplicate = true;
                    break;  // Nếu tìm thấy trùng, không cần kiểm tra tiếp
                }
            }
            if (!isDuplicate) {
                filteredList.add(binDetail);
            }
        }

        List<InventoryDocDetail> filterList = new ArrayList<>();

        for (InventoryDocDetail invenDetail : listInvenDoc) {
            boolean isDuplicate = false;

            for (BinDetail binDetail : listDetail) {
                if (invenDetail.getProductDetailId().equals(binDetail.getProductDetailId()) &&
                        invenDetail.getRecountQuantity() == binDetail.getQuantity()) {
                    isDuplicate = true;
                    break;  // Nếu tìm thấy trùng, không cần kiểm tra tiếp
                }
            }

            // Chỉ thêm vào danh sách mới nếu KHÔNG trùng
            if (!isDuplicate) {
                filterList.add(invenDetail);
            }
        }

        if(filteredList.isEmpty()){
            filteredList=listDetail;
        }


        if (!filterList.isEmpty()) {
            String counterID = filterList.get(0).getCounterId();
            String reCounterID = filterList.get(0).getRecounterId();

            if (status.equalsIgnoreCase("Pending") && !counterID.equals(employee)) {
                response.getWriter().write("<script>alert('None of your Business!'); history.back();</script>");
                return; // Dừng xử lý tiếp
            }

            if (status.equalsIgnoreCase("Recount") && !reCounterID.equals(employee)) {
                response.getWriter().write("<script>alert('None of your Business!'); history.back();</script>");
                return; // Dừng xử lý tiếp
            }
        }

          request.setAttribute("listDetail", filteredList);
          request.setAttribute("binId", binId);
          request.setAttribute("inventoryDocId", inventoryDocId);
          request.getRequestDispatcher("checkInventory.jsp").forward(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Lấy InventoryDocID từ request
            String inventoryDocId = request.getParameter("inventoryDocId");
            String binId= request.getParameter("binId");
            HttpSession session = request.getSession();
            String accountId = (String) session.getAttribute("account_id");
            String employeeId = InventoryDocDAO.getEmployeeIDByAccountID(accountId);

            // Lấy danh sách ProductDetailID và RecountQuantity
            String[] productDetailIds = request.getParameterValues("productdetailId[]");
            String[] recountQuantitiesStr = request.getParameterValues("realQuantity[]");


            // Chuyển đổi sang danh sách
            List<String> productDetailIdList = new ArrayList<>();
            List<Integer> recountQuantityList = new ArrayList<>();
            for (int i = 0; i < productDetailIds.length; i++) {
                productDetailIdList.add(productDetailIds[i]);
                recountQuantityList.add(Integer.parseInt(recountQuantitiesStr[i]));
            }
            if(InventoryDocDAO.canAddProductsToBin(binId,productDetailIdList,recountQuantityList)) {
                // Gọi DAO để cập nhật dữ liệu
                InventoryDocDetailDAO.firstCount(productDetailIdList, recountQuantityList, inventoryDocId);
                InventoryDocDAO.updateInventoryDocStatus(inventoryDocId, "Counted");
                // Chuyển hướng sau khi cập nhật thành công
                response.sendRedirect("ViewInventoryDocList");
            }
            else{
                response.getWriter().write("<script>alert('Over the Maxcapacity'); history.back();</script>");
                return; // Dừng xử lý tiếp
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewInventoryDocList");
        }


    }


}