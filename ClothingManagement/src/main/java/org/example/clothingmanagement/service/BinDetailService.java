package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.repository.BinDetailDAO;

import java.util.List;

public class BinDetailService {
    private final BinDetailDAO bdd = new BinDetailDAO();

    public List<BinDetail> getBinDetailsByBinId(String binId) {
        return bdd.getBinDetailsByBinId(binId);
    }

    public List<BinDetail> getAllBinDetails() {
        return bdd.getAllBinDetails();
    }

    public List<BinDetail> getAllBinDetailAndProductDetailByBinId(String binId) {
        return bdd.getAllBinDetailAndProductDetailByBinId(binId);
    }

    public String getLastBinDetailId(String binId) throws Exception {
        return bdd.getLastBinDetailId(binId);
    }

    public List<BinDetail> getBinDetailsWithPagination(String binId, int page, int pageSize) {
        return bdd.getBinDetailsWithPagination(binId, page, pageSize);
    }

    public boolean addBinDetail(String binDetailId, String binId, String productDetailId, int quantity) throws Exception {
        return bdd.addBinDetail(binDetailId, binId, productDetailId, quantity);
    }

    public List<BinDetail> searchBinDetailWithPagination(String binId, String nameSearch, int page, int pageSize) {
        return bdd.searchBinDetailWithPagination(binId, nameSearch, page, pageSize);
    }

    public List<BinDetail> searchBinDetailWithoutPagination(String binId, String nameSearch) {
        return bdd.searchBinDetailWithoutPagination(binId, nameSearch);
    }

    public boolean deleteProductFromBin(String binId, String productDetailId) {
        return bdd.deleteProductFromBin(binId, productDetailId);
    }

    public boolean canDeleteProduct(String binId, String productDetailId) {
        return bdd.canDeleteProduct(binId, productDetailId);
    }

    public List<ProductDetail> getProductsInBin(String binId) {
        return bdd.getProductsInBin(binId);
    }

    public String findBinDetailIdByBinAndProduct(String binId, String productDetailId) throws Exception {
        return bdd.findBinDetailIdByBinAndProduct(binId, productDetailId);
    }

    public void updateBinDetailQuantity(String binDetailId, int additionalQuantity) throws Exception {
        bdd.updateBinDetailQuantity(binDetailId, additionalQuantity);
    }
    public static void main(String[] args) {
        BinDetailService binDetailService = new BinDetailService();
        List<BinDetail> list = binDetailService.searchBinDetailWithPagination("RP001-001", "blue", 1, 5);
        for (BinDetail bd : list) {
            System.out.println(bd);
        }
    }
}
