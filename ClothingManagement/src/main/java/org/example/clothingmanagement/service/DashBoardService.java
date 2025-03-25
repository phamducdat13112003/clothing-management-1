package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.repository.DashBoardDAO;

import java.util.List;
import java.util.Map;

public class DashBoardService {

    private final DashBoardDAO dashBoardDAO = new DashBoardDAO();

    public int getTotalOrders(){
        return dashBoardDAO.getTotalOrders();
    }

    public double getTotalOrderValue(){
        return dashBoardDAO.getTotalOrderValue();
    }
    public int getTotalSuppliers(){
        return dashBoardDAO.getTotalSuppliers();
    }
    public int getTotalEmployees(){
        return dashBoardDAO.getTotalEmployees();
    }
    public Map<String, Double> getTotalPOByMonth() {
        return dashBoardDAO.getTotalPOByMonth();
    }

    public Map<String, Double> getTotalPOByMonth(String startDate, String endDate){
        return dashBoardDAO.getTotalPOByMonth(startDate, endDate);
    }

    public List<ProductDetail> getProductStorageList(String productId){
        return dashBoardDAO.getProductStorageList(productId);
    }

    public BinDetail getTotalQuantityByProductDetailId(String productDetailId){
        return dashBoardDAO.getTotalQuantityByProductDetailId(productDetailId);
    }
    public List<BinDetail> getBinCapacityPercentage(int page, int pageSize){
        return dashBoardDAO.getBinCapacityPercentage(page, pageSize);
    }
    public int getTotalPages(){
        return dashBoardDAO.getTotalPages();
    }

}
