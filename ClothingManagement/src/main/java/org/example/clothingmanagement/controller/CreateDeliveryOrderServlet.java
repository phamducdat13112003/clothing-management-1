package org.example.clothingmanagement.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.clothingmanagement.service.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CreateDeliveryOrderServlet", value = "/createdeliveryorder")
public class CreateDeliveryOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String poid = request.getParameter("poid");
        String binid = request.getParameter("binid");

        BinDetailService binDetailService = new BinDetailService();
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        BinService binService = new BinService();
        ProductService productService = new ProductService();
        ProductDetailService productDetailService = new ProductDetailService();

        try {
            double maxCapacity = binService.getMaxCapacityByBinID(binid);
            double currentWeight = binService.getWeightOfBinByBinID(binid);
            List<Map<String, Object>> productList = productService.getListProductByPoID(poid);
            String lastBinDetailId = binDetailService.getLastBinDetailId(binid);
            int lastIndex = extractLastIndex(lastBinDetailId, binid);

            double totalWeightAll = 0;
            float newPrice = 0;
            int totalPoDetailQuantity = 0;
            for (Map<String, Object> product : productList) {
                String productId = product.get("ProductID").toString();
                String productDetailID = (String) product.get("ProductDetailID");
                int PoDetailQuantity = (Integer) product.get("PoDetailQuantity");
                double productWeight = (Double) product.get("Weight");
                int ProductDetailQuantity = (Integer) product.get("ProductDetailQuantity");
                int TotalQuantity = productDetailService.getTotalQuantityByProductID(productId);
                float ProductPrice = ((Number) product.get("ProductPrice")).floatValue();
                float PoDetailPrice = ((Number) product.get("PoDetailPrice")).floatValue();
                float totalnewprice =PoDetailPrice * PoDetailQuantity;
                newPrice += totalnewprice;
                totalPoDetailQuantity += PoDetailQuantity;
                float oldprice = ProductPrice * TotalQuantity;
                float updateprice = (newPrice + oldprice)/ (totalPoDetailQuantity + TotalQuantity);
                System.out.println(updateprice);
                int UpdateQuantity = PoDetailQuantity + ProductDetailQuantity;
                double totalWeight = productWeight * PoDetailQuantity;
                totalWeightAll += totalWeight;
                // Kiểm tra trước khi thêm
//                while (PoDetailQuantity > 0) {
//                    double availableCapacity = maxCapacity - currentWeight;
//
//                    if (availableCapacity <= 0) {
//                        // Tạo bin mới nếu bin hiện tại đã đầy
//                        binid = binService.createNewBin();
//                        maxCapacity = binService.getMaxCapacityByBinID(binid);
//                        currentWeight = 0;
//                        lastBinDetailId = binDetailService.getLastBinDetailId(binid);
//                        lastIndex = extractLastIndex(lastBinDetailId, binid);
//                        availableCapacity = maxCapacity;
//                    }
//
//                    // Xác định số lượng có thể thêm vào bin hiện tại
//                    int maxAddableQuantity = (int) (availableCapacity / productWeight);
//                    int quantityToAdd = Math.min(maxAddableQuantity, PoDetailQuantity);
//
//                    if (quantityToAdd > 0) {
//                        // Kiểm tra xem sản phẩm đã tồn tại trong bin chưa
//                        String existingBinDetailId = binDetailService.findBinDetailIdByBinAndProduct(binid, productDetailID);
//                        if (existingBinDetailId != null) {
//                            binDetailService.updateBinDetailQuantity(existingBinDetailId, quantityToAdd);
//                        } else {
//                            lastIndex++;
//                            String newBinDetailId = binid + "-" + String.format("%02d", lastIndex);
//                            binDetailService.addBinDetail(newBinDetailId, binid, productDetailID, quantityToAdd);
//                        }s
//
//                        // Cập nhật số lượng trong ProductDetail
//                        productDetailService.updateQuantityProduct(productDetailID, ProductDetailQuantity + quantityToAdd);
//
//                        // Cập nhật trọng lượng hiện tại của bin
//                        currentWeight += productWeight * quantityToAdd;
//
//                        // Trừ số lượng đã xử lý
//                        PoDetailQuantity -= quantityToAdd;
//                    }
//                }
                if (currentWeight + totalWeightAll > maxCapacity) {
                    request.getSession().setAttribute("transferdofail", "⚠️ Cannot add to bin, exceeds " + maxCapacity + "kg limit!");
                    request.getRequestDispatcher("deliveryorderdetail?poID=" + poid).forward(request, response);
                    return;
                }
                // Cập nhật số lượng trong Product detail
                String existingBinDetailId = binDetailService.findBinDetailIdByBinAndProduct(binid, productDetailID);
                if (existingBinDetailId != null) {
                    // Nếu tồn tại, cập nhật số lượng
                    binDetailService.updateBinDetailQuantity(existingBinDetailId, PoDetailQuantity);
                } else {
                    // Nếu vẫn trong giới hạn, cập nhật bin
                    lastIndex++;
                    String newBinDetailId = binid + "-" + String.format("%02d", lastIndex);
                    binDetailService.addBinDetail(newBinDetailId, binid, productDetailID, PoDetailQuantity);
                }
                productDetailService.updateQuantityProduct(productDetailID, UpdateQuantity);
                // Cập nhật giá tiền trong product
                productService.updatePriceOfProductByProductID(productId, updateprice);
                // Cập nhật trọng lượng hiện tại của bin
                currentWeight += totalWeight;
            }

            // Cập nhật trạng thái PO nếu thành công
            purchaseOrderService.updateStatusPO(poid, "Done");
            request.getSession().setAttribute("transferdosuccessfully", "Import good to warehouse successfully: " + poid);
            request.getRequestDispatcher("viewdeliveryorder").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error processing delivery order", e);
        }
    }

    private int extractLastIndex(String lastBinDetailId, String binid) {
        if (lastBinDetailId != null && lastBinDetailId.startsWith(binid + "-")) {
            try {
                return Integer.parseInt(lastBinDetailId.substring(binid.length() + 1));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Lỗi khi parse binDetailId: " + lastBinDetailId, e);
            }
        }
        return 0;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
