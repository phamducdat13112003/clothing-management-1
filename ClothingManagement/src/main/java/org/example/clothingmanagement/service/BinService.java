package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Bin;
import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.repository.BinDAO;
import org.example.clothingmanagement.repository.SectionDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BinService {

    private final BinDAO binDAO = new BinDAO();

    public List<Bin> getAllBins(){
        return binDAO.getAllBins();
    }

    public double getMaxCapacityByBinID(String binID) {
        return binDAO.getMaxCapacityByBinID(binID);
    }

    public List<BinDetail> getBinDetailByBinID(String binID, int page, int pageSize){
        return binDAO.getBinDetailByBinID(binID, page, pageSize);
    }

    public boolean canDeleteBin(String binID) {
        return binDAO.canDeleteBin(binID);
    }
    public int getTotalBinsBySection(String sectionId){
        return binDAO.getTotalBinsBySection(sectionId);
    }

    public List<Bin> getBinsWithPagination(String sectionId, int page, int pageSize){
        return binDAO.getBinsWithPagination(sectionId, page, pageSize);
    }

    public List<Bin> searchBinWithPagination(String sectionId, String nameSearch, int page, int pageSize){
        return binDAO.searchBinWithPagination(sectionId, nameSearch, page, pageSize);
    }

    public List<Bin> searchBinWithoutPagination(String sectionId, String nameSearch){
        return binDAO.searchBinWithoutPagination(sectionId, nameSearch);
    }

    public List<Bin> getBinsBySectionId(String sectionId, int page, int pageSize){
        return binDAO.getBinsBySectionId(sectionId, page, pageSize);
    }

    public int countBinDetailByBinID(String binID){
        return binDAO.countBinDetailByBinID(binID);
    }

    public int countAllBins(){
        return binDAO.countAllBins();
    }

    public List<BinDetail> searchBinDetail(String nameSearch, String binID, int page, int pageSize){
        return binDAO.searchBinDetail(nameSearch, binID, page, pageSize);
    }

    public Optional<Bin> getBinByBinId(String binID){
        return binDAO.getBinByBinId(binID);
    }

    public List<Bin> getAllBin(){
        List<Bin> bins = binDAO.getAllBin();
        for(Bin bin : bins){
            bin.setCurrentCapacity(0.0);
            bin.setAvailableCapacity(bin.getMaxCapacity());
        }
        return bins;
    }

    public boolean deleteBin(String binId) {
        return binDAO.deleteBin(binId);
    }

    public int countBinDetail(String nameSearch, String binID){
        return binDAO.countBinDetail(nameSearch, binID);
    }

    public List<Bin> getBinsBySectionIdWithoutPagination(String sectionId){
        return binDAO.getBinsBySectionIdWithoutPagination(sectionId);
    }

    public static void main(String[] args){
        BinService bs = new BinService();
        List<Bin> list = bs.getBinsBySectionId("RP001",1,5);
        for(Bin b : list){
            System.out.println(b);
        }
    }
}
