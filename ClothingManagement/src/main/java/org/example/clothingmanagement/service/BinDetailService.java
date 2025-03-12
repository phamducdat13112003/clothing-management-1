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

    public List<BinDetail> getAllBinDetailAndProductDetail(){
        return bdd.getAllBinDetailAndProductDetail();
    }
}
