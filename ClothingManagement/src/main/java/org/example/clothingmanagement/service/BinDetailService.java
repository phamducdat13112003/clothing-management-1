package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
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
    public String getLastBinDetailId(String binId) throws Exception{
        return bdd.getLastBinDetailId(binId);
    }

    public List<BinDetail> getBinDetailsWithPagination(String binId, int page, int pageSize) {
        return bdd.getBinDetailsWithPagination(binId, page, pageSize);
    }

    public static void main(String[] args){
        BinDetailService binDetailService = new BinDetailService();
        List<BinDetail> list = binDetailService.getBinDetailsWithPagination("FW001-001",1,5);
        for(BinDetail bd : list){
            System.out.println(bd);
        }
    }
}
